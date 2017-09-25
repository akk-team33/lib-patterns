package net.team33.patterns.reflect;

import net.team33.patterns.reflect.fields.Filter;
import net.team33.patterns.reflect.fields.Modder;
import net.team33.patterns.reflect.fields.ToName;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.team33.patterns.reflect.FieldUtil.get;
import static net.team33.patterns.reflect.FieldUtil.set;

public class Fields<T> {

    private final T subject;
    private final Map<String, Field> backing;

    private Fields(final T subject, final Map<String, Field> backing) {
        this.subject = subject;
        this.backing = backing;
    }

    /**
     * Supplies a {@link Function} to get the {@link Fields} for an instance of a given class.
     */
    public static <T> Function<T, Fields<T>> of(final Class<T> subjectClass) {
        return builder(subjectClass).build();
    }

    /**
     * Supplies a {@link Builder} for {@link Function}s to get the {@link Fields} for an instance of a given class.
     */
    public static <T> Builder<T> builder(final Class<T> subjectClass) {
        return new Builder<>(subjectClass);
    }

    /**
     * Copies all provided fields from the associated {@code subject} to an {@code other} instance.
     *
     * @return The {@code other} instance.
     */
    public final T copyTo(final T other) {
        return copy(subject, other);
    }

    /**
     * Copies all provided fields from an {@code other} instance of T to the associated {@code subject}.
     *
     * @return The {@code other} instance.
     */
    public final T copyFrom(final T other) {
        return copy(other, subject);
    }

    private T copy(final T origin, final T target) {
        backing.values().stream().forEach(field -> set(field, target, get(field, origin)));
        return target;
    }

    /**
     * Maps all provided fields from the associated {@code subject} to a given {@code target}.
     *
     * @return The {@code target}
     */
    public final <M extends Map<? super String, Object>> M mapTo(final M target) {
        backing.forEach((name, field) -> target.put(name, get(field, subject)));
        return target;
    }

    /**
     * Maps all provided fields from a given {@code origin} to the associated {@code subject}.
     *
     * @return The {@code subject}
     */
    public final T mapFrom(final Map<String, Object> origin) {
        backing.forEach((name, field) -> set(field, subject, origin.get(name)));
        return subject;
    }

    /**
     * Determines if all values of the provided fields are equal
     * between the associated {@code subject} and an {@code other} instance.
     * It is intended to be used for an implementation of the equals()-method of the subjectClass.
     */
    public final boolean equalsTo(final T other) {
        return backing.values().stream()
                .allMatch(field -> Objects.equals(get(field, subject), get(field, other)));
    }

    /**
     * Calculates the hash code from the provided fields for the associated {@code subject}.
     * It is intended to be used for an implementation of the hashCode()-method of the subjectClass.
     */
    public final int toHashCode() {
        return backing.values().stream()
                .map(field -> Objects.hashCode(get(field, subject)))
                .reduce(0, (x, y) -> (x >>> 7) ^ y);
    }

    /**
     * {@inheritDoc}
     *
     * This implementation calculates the string representation from the provided fields for the associated
     * {@code subject}. It is intended to be used for an implementation of the toString()-method of the subjectClass.
     */
    @Override
    public final String toString() {
        return backing.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + get(entry.getValue(), subject))
                .collect(Collectors.joining(", ", "{", "}"));
    }

    public enum ToStream implements Function<Class<?>, Stream<Field>> {

        /**
         * Delivers all {@link Field}s straightly declared by a given {@link Class}
         */
        SIMPLE {
            @Override
            public Stream<Field> apply(final Class<?> subject) {
                return Stream.of(subject.getDeclaredFields());
            }
        },

        /**
         * Delivers all {@link Field}s declared by a given {@link Class} or any of its ancestors.
         */
        HIERARCHICAL {
            @Override
            public Stream<Field> apply(final Class<?> subject) {
                return (null == subject)
                        ? Stream.empty()
                        : Stream.concat(apply(subject.getSuperclass()), Stream.of(subject.getDeclaredFields()));
            }
        }
    }

    @SuppressWarnings({"FieldHasSetterButNoGetter", "unused"})
    public static class Builder<T> {

        private final Class<T> subjectClass;
        private Function<Class<?>, Stream<Field>> toStream = ToStream.SIMPLE;
        private Predicate<Field> filter = Filter.SIGNIFICANT;
        private Function<Field, String> toName = ToName.SIMPLE;

        private Builder(final Class<T> subjectClass) {
            this.subjectClass = subjectClass;
        }

        public final Builder<T> setToStream(final Function<Class<?>, Stream<Field>> toStream) {
            this.toStream = toStream;
            return this;
        }

        public final Builder<T> setFilter(final Predicate<Field> filter) {
            this.filter = filter;
            return this;
        }

        public final Builder<T> setToName(final Function<Field, String> toName) {
            this.toName = toName;
            return this;
        }

        public final Builder<T> setClassToName(final Function<Class<?>, Function<Field, String>> classToName) {
            this.toName = classToName.apply(subjectClass);
            return this;
        }

        public final Function<T, Fields<T>> build() {
            final Map<String, Field> backing = toStream.apply(subjectClass)
                    .filter(filter)
                    .peek(Modder.SET_ACCESSIBLE)
                    .collect(Collectors.toMap(toName, field -> field));
            return subject -> new Fields<>(subject, backing);
        }
    }
}
