package de.team33.patterns.lazy.narvi;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("WeakerAccess")
public abstract class LazyPool<H> {

    @SuppressWarnings("rawtypes")
    private final Map<Key, Object> backing = new ConcurrentHashMap<>(0);

    protected abstract H host();

    @SuppressWarnings("unchecked")
    public final <R> R get(final Key<H, R> key) {
        return (R) backing.computeIfAbsent(key, this::newValue);
    }

    private <R> R newValue(final Key<H, R> key) {
        return key.init(host());
    }

    public final void reset(final Key<?, ?> key) {
        backing.remove(key);
    }

    public final void reset() {
        backing.clear();
    }

    public interface Key<H, R> {

        R init(H host);
    }
}
