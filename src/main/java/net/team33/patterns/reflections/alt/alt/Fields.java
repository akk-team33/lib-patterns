package net.team33.patterns.reflections.alt.alt;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Fields<T> {

    private static final Function<Field, Field> ACCESSIBLE = field -> {
        field.setAccessible(true);
        return field;
    };
    private final List<Field> backing;

    private Fields(final Class<T> subject, final Predicate<Field> filter) {
        this.backing = stream(subject)
                .filter(filter)
                .map(ACCESSIBLE)
                .collect(Collectors.toList());
    }

    private static Stream<Field> stream(final Field[] fields, final Class<?> superclass) {
        return Stream.concat(stream(superclass), Stream.of(fields));
    }

    /**
     * Retrieves the value of the specified {@code field} of a given {@code target}
     * wrapping an {@link IllegalAccessException} as {@link IllegalArgumentException}.
     *
     * @see Field#get(Object)
     */
    public static Object get(final Field field, final Object subject) {
        try {
            return field.get(subject);
        } catch (final IllegalAccessException caught) {
            throw new IllegalArgumentException(caught.getMessage(), caught);
        }
    }

    /**
     * Sets the specified {@code field} on a given {@code target} to a given {@code value}
     * wrapping an {@link IllegalAccessException} as {@link IllegalArgumentException}.
     *
     * @see Field#set(Object, Object)
     */
    public static void set(final Field field, final Object target, final Object value) {
        try {
            field.set(target, value);
        } catch (final IllegalAccessException caught) {
            throw new IllegalArgumentException(caught.getMessage(), caught);
        }
    }

    /**
     * Retrieves a {@link Stream} of all {@link Field}s declared by a given {@link Class} or its superclasses.
     */
    public static Stream<Field> stream(final Class<?> subject) {
        return (null == subject)
                ? Stream.empty()
                : stream(subject.getDeclaredFields(), subject.getSuperclass());
    }

    /**
     * Retrieves a new instance of {@link Fields} for a given {@link Class}
     * using {@link Filter#SIGNIFICANT} as filter.
     */
    public static <T> Fields<T> of(final Class<T> subject) {
        return builder(subject).build();
    }

    /**
     * Retrieves a new {@link Builder} for {@link Fields}.
     */
    public static <T> Builder<T> builder(final Class<T> subject) {
        return new Builder<>(subject);
    }

    /**
     * Copies all relevant fields from an {@code original} to a {@code target}.
     *
     * @return The {@code target}
     */
    public final T copy(final T original, final T target) {
        backing.stream()
                .forEach(field -> set(field, target, get(field, original)));
        return target;
    }

    /**
     * Determines if all values of this fields are equal between two subjects.
     */
    public final boolean equals(final T left, final T right) {
        return backing.stream()
                .allMatch(field -> Objects.equals(get(field, left), get(field, right)));
    }

    /**
     * Retrieves a hash code for a given {@code subject}.
     */
    public final int hashCode(final T subject) {
        return backing.stream()
                .map(field -> Objects.hashCode(get(field, subject)))
                .reduce(0, (x, y) -> (x >>> 7) ^ y);
    }

    public enum Filter implements Predicate<Field> {

        /**
         * Defines a filter accepting all including static fields.
         */
        ALL(modifiers -> true),

        /**
         * Defines a filter accepting all but static fields.
         */
        NON_STATIC(modifiers -> !Modifier.isStatic(modifiers)),

        /**
         * Defines a filter accepting all but static or transient fields.
         *
         * Those fields should be significant for a type with value semantic.
         */
        SIGNIFICANT(modifiers -> !(Modifier.isStatic(modifiers) || Modifier.isTransient(modifiers)));

        private final IntPredicate core;

        Filter(final IntPredicate core) {
            this.core = core;
        }

        @Override
        public boolean test(final Field field) {
            return core.test(field.getModifiers());
        }
    }

    @SuppressWarnings("FieldHasSetterButNoGetter")
    public static class Builder<T> {

        private final Class<T> subject;
        private Predicate<Field> filter = Filter.SIGNIFICANT;

        private Builder(final Class<T> subject) {
            this.subject = subject;
        }

        public final Fields<T> build() {
            return new Fields<>(subject, filter);
        }

        public final Builder<T> setFilter(final Predicate<Field> filter) {
            this.filter = filter;
            return this;
        }
    }
}
