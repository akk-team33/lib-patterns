package de.team33.patterns.collection.mneme;

import java.util.AbstractMap;
import java.util.Map;

/**
 * An immutable, abstract {@link Map.Entry} implementation that definitely does not support
 * {@link Map.Entry#setValue(Object)}.
 *
 * @param <K> the type of key.
 * @param <V> the type of value.
 */
public abstract class ImmutableEntry<K, V> implements Map.Entry<K, V> {

    /**
     * Returns an {@link ImmutableEntry} backed by the given <em>entry</em>.
     *
     * @param <K> the type of key in the result.
     * @param <V> the type of value in the result.
     */
    public static <K, V> ImmutableEntry<K, V> proxy(final Map.Entry<? extends K, ? extends V> entry) {
        return new Proxy<>(entry);
    }

    /**
     * Compares the given <em>object</em> with <em>this</em> map entry for equality
     * as specified in {@link Map.Entry#equals(Object)}.
     */
    @Override
    public final boolean equals(final Object object) {
        return (this == object) || ((object instanceof final Map.Entry<?, ?> other) && MapUtil.equals(this, other));
    }

    /**
     * Returns the hash code for <em>this</em> map entry
     * as specified in {@link Map.Entry#hashCode()}.
     */
    @Override
    public final int hashCode() {
        return MapUtil.hashCode(this);
    }

    /**
     * Returns a string representation for <em>this</em> map entry,
     * as demonstrated in {@link AbstractMap.SimpleEntry#toString()}.
     */
    @Override
    public final String toString() {
        return MapUtil.toString(this);
    }

    /**
     * Not supported.
     *
     * @throws UnsupportedOperationException in any case
     */
    @Override
    public final V setValue(final V value) {
        throw new UnsupportedOperationException("ImmutableEntry does not support setValue()");
    }

    private static final class Proxy<K, V> extends ImmutableEntry<K, V> {

        private final Map.Entry<? extends K, ? extends V> core;

        private Proxy(final Map.Entry<? extends K, ? extends V> core) {
            this.core = core;
        }

        @Override
        public K getKey() {
            return core.getKey();
        }

        @Override
        public V getValue() {
            return core.getValue();
        }
    }
}
