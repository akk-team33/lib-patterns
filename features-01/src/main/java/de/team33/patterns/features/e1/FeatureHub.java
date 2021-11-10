package de.team33.patterns.features.e1;


import java.util.function.Function;

public interface FeatureHub<H> {

    <R> R get(Key<H, R> key);

    /**
     * Represents a unique key for a specific <em>feature</em> and is also the {@link Function} that can generate that
     * <em>feature</em> on demand in the context of a specific host.
     * <p>
     * In general, it is expected that an instance of {@link Key} has an identity semantic and is defined as a
     * permanent constant. It is not a good idea to generate a {@link Key} inline as its identity is then ambiguous.
     *
     * @param <H> The type of host that forms the context of the <em>feature</em>.
     * @param <R> The type of the <em>feature</em>.
     */
    interface Key<H, R> extends Function<H, R> {}
}
