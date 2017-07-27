package net.team33.patterns.reflections;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.stream.Stream;

import static net.team33.patterns.reflections.FieldUtil.get;
import static net.team33.patterns.reflections.FieldUtil.set;

public interface FieldSet<T> {

    /**
     * Supplies a {@link Stream} of provided {@link Field Fields}.
     *
     * These Fields must be {@linkplain Field#setAccessible(boolean) accessible}.
     */
    Stream<Field> fields();

    /**
     * Copies all provided fields from an {@code original} to a {@code target}.
     *
     * @return The {@code target}
     */
    default T copy(final T original, final T target) {
        fields().forEach(field -> set(field, target, get(field, original)));
        return target;
    }

    /**
     * Determines if all values of the provided fields are equal between two subjects.
     */
    default boolean equals(final T left, final T right) {
        return fields()
                .allMatch(field -> Objects.equals(get(field, left), get(field, right)));
    }

    /**
     * Calculates a hash code from the provided fields for a given {@code subject}.
     */
    default int hashCode(final T subject) {
        return fields()
                .map(field -> Objects.hashCode(get(field, subject)))
                .reduce(0, (x, y) -> (x >>> 7) ^ y);
    }
}
