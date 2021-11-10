package de.team33.samples.patterns.features.e1;

import de.team33.patterns.features.e1.FeatureHub;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FeatureMap<H> implements FeatureHub<H> {

    @SuppressWarnings("rawtypes")
    private final Map<Key, Object> backing = new ConcurrentHashMap<>(0);
    private final H host;

    public FeatureMap(final H host) {
        this.host = host;
    }

    @Override
    public final <R> R get(final Key<H, R> key) {
        return key.apply(host);
    }
}
