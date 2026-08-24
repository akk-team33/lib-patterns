package de.team33.patterns.lazy.narvi;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @see <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/lazy-janus/">lazy-janus</a>
 * @see <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/lazy-janus/apidocs/">lazy-janus/apidocs</a>
 * @deprecated consider class Features from module
 * <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/lazy-janus/apidocs/">lazy-janus</a>
 * as a replacement.
 */
@Deprecated
@SuppressWarnings({"WeakerAccess", "AbstractClassWithOnlyOneDirectInheritor"})
public abstract class LazyFeatures<H> {

    @SuppressWarnings("rawtypes")
    private final Map<Key, Lazy> backing = Collections.synchronizedMap(new HashMap<>(0));

    /**
     * @deprecated see {@link LazyFeatures}.
     */
    @Deprecated
    protected abstract H host();

    /**
     * @deprecated see {@link LazyFeatures}.
     */
    @Deprecated
    public final <R> Optional<R> peek(final Key<? super H, ? extends R> key) {
        @SuppressWarnings("unchecked")
        final Lazy<R> lazy = backing.get(key);
        return Optional.ofNullable(lazy).map(Lazy::get);
    }

    /**
     * @deprecated see {@link LazyFeatures}.
     */
    @Deprecated
    public final <R> R get(final Key<? super H, ? extends R> key) {
        @SuppressWarnings("unchecked")
        final Lazy<R> lazy = backing.computeIfAbsent(key, this::newLazy);
        return lazy.get();
    }

    private <R> Lazy<R> newLazy(final Key<? super H, ? extends R> key) {
        return Lazy.init(() -> key.init(host()));
    }

    /**
     * @deprecated see {@link LazyFeatures}.
     */
    @Deprecated
    public final void reset(final Key<?, ?> key) {
        backing.remove(key);
    }

    /**
     * @deprecated see {@link LazyFeatures}.
     */
    @Deprecated
    public final void reset() {
        backing.clear();
    }

    /**
     * @see <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/lazy-janus/">lazy-janus</a>
     * @see <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/lazy-janus/apidocs/">lazy-janus/apidocs</a>
     * @deprecated consider class Features.Key from module
     * <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/lazy-janus/apidocs/">lazy-janus</a>
     * as a replacement.
     */
    @Deprecated
    @SuppressWarnings("InterfaceWithOnlyOneDirectInheritor")
    @FunctionalInterface
    public interface Key<H, R> {

        /**
         * @deprecated see {@link Key}.
         */
        @Deprecated
        R init(H host);
    }
}
