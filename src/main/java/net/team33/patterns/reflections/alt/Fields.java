package net.team33.patterns.reflections.alt;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.team33.patterns.reflections.FieldUtil.get;
import static net.team33.patterns.reflections.FieldUtil.set;

public abstract class Fields<T> {

    /**
     * Supplies a {@link Stream} of provided {@link Field Fields}.
     *
     * These Fields must be {@linkplain Field#setAccessible(boolean) accessible}.
     */
    protected abstract Stream<Field> fields();

    /**
     * Delivers a name for a given {@link Field} that may differ from {@link Field#getName()}.
     */
    protected abstract String name(final Field field);

    /**
     * Copies all provided fields from an {@code original} to a {@code target}.
     *
     * @return The {@code target}
     */
    public final T copy(final T original, final T target) {
        fields().forEach(field -> set(field, target, get(field, original)));
        return target;
    }

    /**
     * Maps all provided fields from the {@code subject} to a new {@link Map}.
     */
    public final Map<String, Object> toMap(final T origin) {
        return fields().collect(Collectors.toMap(this::name, field -> get(field, origin)));
    }

    /**
     * Supplies a {@link Function} to map all provided fields from a {@link Map} to the given {@code target}
     * and return that {@code target}.
     */
    public final Function<Map<String, Object>, T> mapTo(final T target) {
        return map -> {
            fields().forEach(field -> set(field, target, map.get(name(field))));
            return target;
        };
    }

    /**
     * Determines if all values of the provided fields are equal between two subjects.
     */
    public final boolean equals(final T left, final T right) {
        return fields()
                .allMatch(field -> Objects.equals(get(field, left), get(field, right)));
    }

    /**
     * Calculates a hash code from the provided fields for a given {@code subject}.
     */
    public final int hashCode(final T subject) {
        return fields()
                .map(field -> Objects.hashCode(get(field, subject)))
                .reduce(0, (x, y) -> (x >>> 7) ^ y);
    }
}
