package de.team33.patterns.properties.e2;

import java.util.Map;

/**
 * Abstracts a tool used to analyze instances of a specific type by translating their properties into a normal form.
 * <p>
 * A {@link Map} (String -&gt; Object) is regarded as the normal form.
 *
 * @param <T> The type whose instances can be analyzed.
 */
public interface Analyzer<T> {

    /**
     * Analyzes an instance of the underlying type by normalizing its properties.
     *
     * @param origin The instance to be analyzed.
     * @param target A mutable {@link Map} that is supposed to represent the normalized properties.
     * @param <M> The exact type of the target {@link Map}.
     * @return The target {@link Map}.
     */
    <M extends Map<String, Object>> M analyse(T origin, M target);
}
