package de.team33.patterns.features.e1;


import java.util.function.Function;

/**
 * Abstracts an instance that manages the <em>features</em> in the context of a specific host.
 *
 * @param <H> The type of the host.
 */
public interface FeatureHub<H> {

    /**
     * Returns the <em>feature</em> that is associated with the given key.
     * <p>
     * When the <em>feature</em> in question is requested for the first time, it is created.
     * Once created, the same <em>feature</em> is always returned.
     *
     * @param key A unique key for a specific feature and also the {@link Function} that can generate that feature
     *            on demand in the context of a specific host.
     *            <p>
     *            In general, the key is expected to have an identity semantic and is defined as a permanent
     *            constant. It is not a good idea to generate the key inline as its identity is then ambiguous.
     * @param <R> The type of the <em>feature</em>.
     */
    <R> R get(Function<? super H, ? extends R> key);
}
