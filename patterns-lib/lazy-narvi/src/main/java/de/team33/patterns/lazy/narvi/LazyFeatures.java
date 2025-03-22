package de.team33.patterns.lazy.narvi;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
    private final Map<Key, Lazy> backing = Collections.synchronizedMap(new HashMap<>(0));

    protected abstract H host();

    public final <R> Optional<R> peek(final Key<? super H, ? extends R> key) {
        @SuppressWarnings("unchecked")
        final Lazy<R> lazy = backing.get(key);
        return Optional.ofNullable(lazy).map(Lazy::get);
    }

    public final <R> R get(final Key<? super H, ? extends R> key) {
        @SuppressWarnings("unchecked")
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
