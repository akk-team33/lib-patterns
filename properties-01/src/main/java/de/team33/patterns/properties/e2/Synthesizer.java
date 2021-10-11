package de.team33.patterns.properties.e2;

import java.util.Map;

/**
 * Abstracts a tool used to synthesize instances of a specific type by restoring their properties from a normal form.
 * <p>
 * A {@link Map} (String -&gt; Object) is regarded as the normal form.
 *
 * @param <T> The type whose instances can be synthesized.
 */
@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface Synthesizer<T> {

    /**
     * Synthesizes an instance of the underlying type by restoring its properties from a normal form.
     *
     * @param origin A {@link Map} representing the normalized properties.
     * @param target The instance to be synthesized.
     * @return The target instance.
     */
    T synthesize(Map<?, ?> origin, T target);
}
