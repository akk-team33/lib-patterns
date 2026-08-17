package de.team33.patterns.lazy.lambda;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class Features {

    @SuppressWarnings("rawtypes")
    private final Map<Key, Lazy> cache = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    private <T> Lazy<T> lazy(final Key<T> key, final Supplier<T> supplier) {
        return cache.computeIfAbsent(key, any -> Lazy.init(supplier));
    }

    public final <T> T get(final Key<T> key, final Supplier<T> supplier) {
        return lazy(key, supplier).get();
    }

    @SuppressWarnings("MarkerInterface")
    public interface Key<T> {}
}
