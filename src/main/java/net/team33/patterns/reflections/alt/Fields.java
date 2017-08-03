package net.team33.patterns.reflections.alt;

import net.team33.patterns.reflections.FieldUtil;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.team33.patterns.reflections.FieldUtil.get;
import static net.team33.patterns.reflections.FieldUtil.set;

public class Fields<T> {

    private final T subject;
    private final Set<Field> backing;

    private Fields(final T subject, final Set<Field> backing) {
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
     * Copies all provided fields from the {@code subject} to an {@code other} instance.
     *
     * @return The {@code other} instance.
     */
    public final T copyTo(final T other) {
        return copy(subject, other);
    }

    /**
     * Copies all provided fields from an {@code other} instance of T to the {@code subject}.
     *
     * @return The {@code other} instance.
     */
    public final T copy(final T other) {
        return copy(other, subject);
    }

    private T copy(final T origin, final T target) {
        backing.stream().forEach(field -> set(field, target, get(field, origin)));
        return target;
    }

    /**
     * Determines if all values of the provided fields are equal
     * between the {@code subject} and an {@code other} instance.
     */
    public final boolean equalsTo(final T other) {
        return backing.stream()
                .allMatch(field -> Objects.equals(get(field, subject), get(field, other)));
    }

    /**
     * Calculates a hash code from the provided fields for the {@code subject}.
     */
    public final int toHashCode() {
        return backing.stream()
                .map(field -> Objects.hashCode(get(field, subject)))
                .reduce(0, (x, y) -> (x >>> 7) ^ y);
    }

    public static class Builder<T> {

        private final Class<T> subjectClass;
        private Predicate<Field> filter = FieldUtil.SIGNIFICANT;

        private Builder(final Class<T> subjectClass) {
            this.subjectClass = subjectClass;
        }

        public final Builder<T> setFilter(final Predicate<Field> filter) {
            this.filter = filter;
            return this;
        }

        public final Function<T, Fields<T>> build() {
            final Set<Field> backing = Stream.of(subjectClass.getDeclaredFields())
                    .filter(filter)
                    .peek(FieldUtil.SET_ACCESSIBLE)
                    .collect(Collectors.toSet());
            return subject -> new Fields<>(subject, backing);
        }
    }
}
