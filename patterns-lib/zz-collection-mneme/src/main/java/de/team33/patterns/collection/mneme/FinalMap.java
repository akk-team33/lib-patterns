package de.team33.patterns.collection.mneme;

import de.team33.patterns.streamable.naiad.Streamable;

import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * An immutable {@link Map} that preserves the key/value/entry encounter order of its source.
 * <p>
 * The iteration order of a {@code FinalMap} is the order in which the distinct keys are encountered in the source.
 *
 * @param <K> the type of keys in this map.
 * @param <V> the type of values in this map.
 */
public final class FinalMap<K, V> extends AbstractMap<K, V> {

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static final FinalMap EMPTY = new FinalMap(Map.of());

    private final FinalSet<Map.Entry<K, V>> entries;

    private FinalMap(final Map<? extends K, ? extends V> source) {
        this.entries = FinalSet.of(() -> source.entrySet()
                                               .stream()
                                               .map(FinalEntry::of));
    }

    /**
     * Returns an empty {@link FinalMap}.
     */
    @SuppressWarnings({"unchecked", "AssignmentOrReturnOfFieldWithMutableType"})
    public static <K, V> FinalMap<K, V> empty() {
        return EMPTY;
    }

    /**
     * Returns a {@link FinalMap} containing a single mapping.
     */
    @SuppressWarnings("MethodOverridesStaticMethodOfSuperclass")
    public static <K, V> FinalMap<K, V> of(final K key, final V value) {
        return new FinalMap<>(Map.of(key, value));
    }

    /**
     * Returns a {@link FinalMap} created from the given <em>source</em>.
     */
    public static <K, V> FinalMap<K, V> of(final Streamable<? extends Map.Entry<? extends K, ? extends V>> source) {
        return new FinalMap<>(source.stream()
                                    .collect(LinkedHashMap::new, FinalMap::putEntry, Map::putAll));
    }

    /**
     * Returns a {@link FinalMap} created from the given <em>source</em>.
     */
    public static <T, K, V> FinalMap<K, V> of(final Streamable<T> source,
                                              final Function<? super T, ? extends K> toKey,
                                              final Function<? super T, ? extends V> toValue) {
        return of(source, FinalEntry.mapping(toKey, toValue));
    }

    /**
     * Returns a {@link FinalMap} created from the given <em>source</em>.
     */
    public static <T, K, V> FinalMap<K, V>
    of(final Streamable<T> source, final Function<? super T, ? extends Entry<? extends K, ? extends V>> toEntry) {
        return of(() -> source.stream().map(toEntry));
    }

    /**
     * Returns a {@link FinalMap} created from the given <em>source</em>.
     */
    public static <K, V> FinalMap<K, V> of(final Map<? extends K, ? extends V> source) {
        return new FinalMap<>(source);
    }

    /**
     * Returns an empty {@link Builder}.
     */
    public static <K, V> Builder<K, V> builder() {
        return new Builder<>(Map.of());
    }

    /**
     * Returns a {@link Builder} initially containing a single mapping.
     */
    public static <K, V> Builder<K, V> builder(final K key, final V value) {
        return new Builder<>(Map.of(key, value));
    }

    /**
     * Returns a {@link Builder} created from the given <em>source</em>.
     */
    public static <K, V> Builder<K, V> builder(final Map<? extends K, ? extends V> source) {
        return new Builder<>(source);
    }

    private static <K, V> void putEntry(final Map<K, V> map, final Map.Entry<? extends K, ? extends V> entry) {
        map.put(entry.getKey(), entry.getValue());
    }

    @Override
    public final Set<Map.Entry<K, V>> entrySet() {
        // Already is immutable ...
        // noinspection AssignmentOrReturnOfFieldWithMutableType
        return entries;
    }

    /**
     * Builder implementation to build target instances of {@link FinalMap}.
     * <p>
     * Use {@link #builder()}, {@link #builder(Object, Object)} or {@link #builder(Map)} to get an instance.
     *
     * @param <K> The key type of the target instance.
     * @param <V> The value type of the target instance.
     */
    public static final class Builder<K, V> {

        private final Map<K, V> backing;

        private Builder(final Map<? extends K, ? extends V> source) {
            backing = new LinkedHashMap<>(source);
        }

        /**
         * Applies the given <em>consumer</em> to a mutable {@link Map} associated with <em>this</em> builder.
         *
         * @return <em>this</em> builder.
         * @throws NullPointerException if <em>consumer</em> is {@code null}.
         */
        @SuppressWarnings("WeakerAccess")
        public final Builder<K, V> setup(final Consumer<? super Map<K, V>> consumer) {
            consumer.accept(backing);
            return this;
        }

        /**
         * Puts a pair of <em>key / value</em> to the instance to be set up.
         *
         * @return <em>this</em> builder.
         */
        public final Builder<K, V> put(final K key, final V value) {
            return setup(map -> map.put(key, value));
        }

        /**
         * Puts multiple pairs of <em>key / value</em> from the given <em>source</em> to the instance to be set up.
         *
         * @return <em>this</em> builder.
         * @throws NullPointerException if <em>source</em> is {@code null}.
         */
        public final Builder<K, V> putAll(final Map<? extends K, ? extends V> source) {
            return setup(map -> map.putAll(source));
        }

        /**
         * Puts multiple pairs of <em>key / value</em> from the given <em>source</em> to the instance to be set up.
         *
         * @return <em>this</em> builder.
         * @throws NullPointerException if <em>source</em> is {@code null}.
         */
        public final Builder<K, V> putAll(final Streamable<? extends Map.Entry<? extends K, ? extends V>> source) {
            return setup(map -> source.forEach(entry -> map.put(entry.getKey(), entry.getValue())));
        }

        /**
         * Puts multiple pairs of <em>key / value</em> from the given <em>source</em> to the instance to be set up.
         *
         * @return <em>this</em> builder.
         * @throws NullPointerException if <em>source</em> is {@code null}.
         */
        public final Builder<K, V> putAll(final Builder<? extends K, ? extends V> source) {
            return setup(map -> map.putAll(source.backing));
        }

        /**
         * Removes a pair of a given <em>key</em> and its associated value from the instance to be set up.
         *
         * @return <em>this</em> builder.
         */
        public final Builder<K, V> remove(final Object key) {
            // noinspection SuspiciousMethodCalls
            return setup(map -> map.remove(key));
        }

        /**
         * Returns a new {@link FinalMap} as build result.
         */
        public final FinalMap<K, V> build() {
            return new FinalMap<>(backing);
        }
    }
}
