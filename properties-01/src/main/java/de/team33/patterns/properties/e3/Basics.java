package de.team33.patterns.properties.e3;

import java.util.Map;
import java.util.function.Function;

public class Basics<T> {

    private final Function<T, Map<String, Object>> mapping;

    public Basics(final Function<T, Map<String, Object>> mapping) {
        this.mapping = mapping;
    }

    public final int hashCode(final T subject) {
        return toMap(subject).hashCode();
    }

    public final boolean equals(final T subject, final T other) {
        return toMap(subject).equals(toMap(other));
    }

    public final String toString(final T subject) {
        return toMap(subject).toString();
    }

    public final Map<String, Object> toMap(final T subject) {
        return mapping.apply(subject);
    }
}
