package de.team33.patterns.collection.mneme;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * An immutable, abstract {@link Map} implementation that definitely does not support any optional map method.
 *
 * @param <K> the type of keys in that map.
 * @param <V> the type of values in that map.
 */
public abstract class ImmutableMap<K, V> extends AbstractMap<K, V> {

    /**
     * Returns an {@link ImmutableMap} backed by the given <em>map</em>,
     * preserving the encounter order of the <em>map</em>, if any.
     *
     * @param <K> the type of keys in the result.
     * @param <V> the type of values in the result.
     */
    public static <K, V> ImmutableMap<K, V> proxy(final Map<? extends K, ? extends V> map) {
        return new Proxy<>(map);
    }

    /**
     * Returns a {@link Set} view of the mappings contained in <em>this</em> map.
     * <p>
     * An {@link ImmutableMap} returns an {@link ImmutableSet}.
     * <p>
     * An implementation must ensure that each element of the resulting set is an immutable entry,
     * ideally an {@link ImmutableEntry}.
     */
    @Override
    public abstract ImmutableSet<Map.Entry<K, V>> entrySet();

    /**
     * Returns a {@link Set} view of the keys contained in <em>this</em> map.
     * <p>
     * An {@link ImmutableMap} returns an {@link ImmutableSet}.
     */
    @Override
    public final ImmutableSet<K> keySet() {
        return ImmutableSet.proxy(super.keySet());
    }

    /**
     * Not supported.
     *
     * @throws UnsupportedOperationException in any case
     */
    @Override
    public final V put(final K key, final V value) {
        throw new UnsupportedOperationException("ImmutableMap does not support add(K, V)");
    }

    /**
     * Not supported.
     *
     * @throws UnsupportedOperationException in any case
     */
    @Override
    public final void putAll(final Map<? extends K, ? extends V> m) {
        throw new UnsupportedOperationException("ImmutableMap does not support putAll(Map)");
    }

    /**
     * Not supported.
     *
     * @throws UnsupportedOperationException in any case
     */
    @Override
    public final V putIfAbsent(final K key, final V value) {
        throw new UnsupportedOperationException("ImmutableMap does not support putIfAbsent(K, V)");
    }

    /**
     * Not supported.
     *
     * @throws UnsupportedOperationException in any case
     */
    @Override
    public final V merge(final K key, final V value, final BiFunction<? super V, ? super V, ? extends V> remapping) {
        throw new UnsupportedOperationException("ImmutableMap does not support merge(K, V, BiFunction)");
    }

    /**
     * Not supported.
     *
     * @throws UnsupportedOperationException in any case
     */
    @Override
    public final V replace(final K key, final V value) {
        throw new UnsupportedOperationException("ImmutableMap does not support replace(K, V)");
    }

    /**
     * Not supported.
     *
     * @throws UnsupportedOperationException in any case
     */
    @Override
    public final boolean replace(final K key, final V oldValue, final V newValue) {
        throw new UnsupportedOperationException("ImmutableMap does not support replace(K, V, V)");
    }

    /**
     * Not supported.
     *
     * @throws UnsupportedOperationException in any case
     */
    @Override
    public final void replaceAll(final BiFunction<? super K, ? super V, ? extends V> function) {
        throw new UnsupportedOperationException("ImmutableMap does not support replaceAll(BiFunction)");
    }

    /**
     * Not supported.
     *
     * @throws UnsupportedOperationException in any case
     */
    @Override
    public final V remove(final Object key) {
        throw new UnsupportedOperationException("ImmutableMap does not support remove(Object)");
    }

    /**
     * Not supported.
     *
     * @throws UnsupportedOperationException in any case
     */
    @Override
    public final boolean remove(final Object key, final Object value) {
        throw new UnsupportedOperationException("ImmutableMap does not support remove(Object, Object)");
    }

    /**
     * Not supported.
     *
     * @throws UnsupportedOperationException in any case
     */
    @Override
    public final V computeIfAbsent(final K key, final Function<? super K, ? extends V> mappingFunction) {
        throw new UnsupportedOperationException("ImmutableMap does not support computeIfAbsent(K, Function)");
    }

    /**
     * Not supported.
     *
     * @throws UnsupportedOperationException in any case
     */
    @Override
    public final V computeIfPresent(final K key, final BiFunction<? super K, ? super V, ? extends V> remapping) {
        throw new UnsupportedOperationException("ImmutableMap does not support computeIfPresent(K, BiFunction)");
    }

    /**
     * Not supported.
     *
     * @throws UnsupportedOperationException in any case
     */
    @Override
    public final V compute(final K key, final BiFunction<? super K, ? super V, ? extends V> remapping) {
        throw new UnsupportedOperationException("ImmutableMap does not support compute(K, BiFunction)");
    }

    /**
     * Not supported.
     *
     * @throws UnsupportedOperationException in any case
     */
    @Override
    public final void clear() {
        throw new UnsupportedOperationException("ImmutableMap does not support clear()");
    }

    /**
     * Returns a {@link Collection} view of the values contained in <em>this</em> map.
     * <p>
     * An {@link ImmutableMap} returns an {@link ImmutableCollection}.
     */
    @Override
    public final ImmutableCollection<V> values() {
        return ImmutableCollection.proxy(super.values());
    }

    private static final class Proxy<K, V> extends ImmutableMap<K, V> {

        private final ImmutableSet<Map.Entry<K, V>> entries;

        private Proxy(final Map<? extends K, ? extends V> core) {
            this.entries = new ProxySet<>(core.entrySet());
        }

        @Override
        public ImmutableSet<Map.Entry<K, V>> entrySet() {
            // Already is immutable ...
            // noinspection AssignmentOrReturnOfFieldWithMutableType
            return entries;
        }
    }

    private static final class ProxySet<K, V> extends ImmutableSet<Map.Entry<K, V>> {

        private final Set<? extends Map.Entry<? extends K, ? extends V>> core;

        private ProxySet(final Set<? extends Map.Entry<? extends K, ? extends V>> core) {
            this.core = core;
        }

        @Override
        public ImmutableIterator<Map.Entry<K, V>> iterator() {
            return new ProxyIterator<>(core.iterator());
        }

        @Override
        public int size() {
            return core.size();
        }
    }

    private static final class ProxyIterator<K, V> extends ImmutableIterator<Map.Entry<K, V>> {

        private final Iterator<? extends Map.Entry<? extends K, ? extends V>> core;

        private ProxyIterator(final Iterator<? extends Map.Entry<? extends K, ? extends V>> core) {
            this.core = core;
        }

        @Override
        public boolean hasNext() {
            return core.hasNext();
        }

        @Override
        public Map.Entry<K, V> next() {
            return ImmutableEntry.proxy(core.next());
        }
    }
}
