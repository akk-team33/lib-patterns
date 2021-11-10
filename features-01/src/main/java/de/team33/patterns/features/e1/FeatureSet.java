package de.team33.patterns.features.e1;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * Implements a common {@link FeatureHub}.
 *
 * @param <H> The type of the host.
 */
public class FeatureSet<H> implements FeatureHub<H> {

    @SuppressWarnings("rawtypes")
    private final Map<Function, Object> backing = new ConcurrentHashMap<>(0);
    private final H host;

    /**
     * Initializes a new instance with a given host.
     */
    public FeatureSet(final H host) {
        this.host = host;
    }

    @Override
    public final <R> R get(final Function<? super H, ? extends R> key) {
        // This is the only place where a feature is created and associated with the key
        // and this feature is clearly of type <F>, so that a cast can take place without any problems ...
        // noinspection unchecked
        return (R) backing.computeIfAbsent(key, this::create);
    }

    private <R> R create(final Function<? super H, ? extends R> key) {
        return key.apply(host);
    }
}
