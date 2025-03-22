package de.team33.patterns.lazy.narvi;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A tool for managing properties of a host instance, which typically result from other properties of the host
 * and are only actually determined when needed. Once determined, properties are retained until reset.
 *
 * @param <H> The host type.
 * @see #reset(Key)
 * @see #reset()
 */
@SuppressWarnings({"WeakerAccess", "AbstractClassWithOnlyOneDirectInheritor"})
public abstract class LazyFeatures<H> {

    @SuppressWarnings("rawtypes")
    private final Map<Key, Lazy> backing = new ConcurrentHashMap<>(0);

    protected abstract H host();

    @SuppressWarnings("unchecked")
    public final <R> Optional<R> peek(final Key<? super H, ? extends R> key) {
        return Optional.ofNullable((Lazy<R>) backing.get(key))
                       .map(Lazy::get);
    }

    @SuppressWarnings("unchecked")
    public final <R> R get(final Key<? super H, ? extends R> key) {
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
