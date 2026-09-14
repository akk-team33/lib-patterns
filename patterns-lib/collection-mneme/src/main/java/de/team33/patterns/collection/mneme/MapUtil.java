package de.team33.patterns.collection.mneme;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;

final class MapUtil {

    private MapUtil() {}

    /**
     * Determines whether two instances of {@link Map.Entry} are equal as specified in
     * {@link Map.Entry#equals(Object)}.
     *
     * @throws NullPointerException if <em>left</em> or <em>right</em> is {@code null}.
     */
    static boolean equals(final Map.Entry<?, ?> left, final Map.Entry<?, ?> right) {
        return Objects.equals(left.getKey(), right.getKey()) && Objects.equals(left.getValue(), right.getValue());
    }

    /**
     * Returns a hash code for a given <em>entry</em> as specified in
     * {@link Map.Entry#hashCode()}.
     *
     * @throws NullPointerException if <em>entry</em> is {@code null}.
     */
    static int hashCode(final Map.Entry<?, ?> entry) {
        return Objects.hashCode(entry.getKey()) ^ Objects.hashCode(entry.getValue());
    }

    /**
     * Returns a string representation for a given <em>entry</em>,
     * as demonstrated in {@link AbstractMap.SimpleEntry#toString()}.
     *
     * @throws NullPointerException if <em>entry</em> is {@code null}.
     */
    static String toString(final Map.Entry<?, ?> entry) {
        return "%s=%s".formatted(entry.getKey(), entry.getValue());
    }
}
