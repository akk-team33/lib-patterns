package de.team33.patterns.properties.e2;

import java.util.Map;
import java.util.function.Function;

public class Helper<T> {

    private final Function<? super T, ? extends Map<String, Object>> mapper;

    @SuppressWarnings("BoundedWildcard")
    public Helper(final Function<T, Map<String, Object>> mapper) {
        this.mapper = mapper;
    }

    public final boolean equals(final T subject, final T other) {
        return toMap(subject).equals(toMap(other));
    }

    public final int hashCode(final T subject) {
        return toMap(subject).hashCode();
    }

    public final String toString(final T subject) {
        return toMap(subject).toString();
    }

    public final Map<String, Object> toMap(final T subject) {
        return mapper.apply(subject);
    }
}
