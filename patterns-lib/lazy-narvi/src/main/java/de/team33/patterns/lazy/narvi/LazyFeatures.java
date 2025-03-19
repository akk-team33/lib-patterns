package de.team33.patterns.lazy.narvi;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings({"WeakerAccess", "AbstractClassWithOnlyOneDirectInheritor"})
public abstract class LazyFeatures<H> {

    @SuppressWarnings("rawtypes")
    private final Map<Key, Lazy> backing = new ConcurrentHashMap<>(0);

    protected abstract H host();

    @SuppressWarnings("unchecked")
    public final <R> R get(final Key<H, R> key) {
        final Lazy<R> lazy = backing.computeIfAbsent(key, this::newLazy);
        return lazy.get();
    }

    private <R> Lazy<R> newLazy(final Key<? super H, ? extends R> key) {
        return Lazy.init(() -> key.init(host()));
    }

    public final void reset(final Key<?, ?> key) {
        backing.remove(key);
    }

    public final void reset() {
        backing.clear();
    }

    @SuppressWarnings("InterfaceWithOnlyOneDirectInheritor")
    @FunctionalInterface
    public interface Key<H, R> {

        R init(H host);
    }
}
