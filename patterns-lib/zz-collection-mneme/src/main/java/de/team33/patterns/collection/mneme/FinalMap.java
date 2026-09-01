package de.team33.patterns.collection.mneme;

import de.team33.patterns.streamable.naiad.Streamable;
import de.team33.patterns.streamable.naiad.Streamer;

import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * An immutable {@link Map} implementation
 * that preserves the encounter order of its source and may contain {@code null} as key or values.
 * <p>
 * To build an instance you may use a {@link Streamer}, example:
 * <pre>
 * final FinalMap&lt;Integer, String&gt; map = Streamer.of(FinalEntry.of(0, "zero"))
 *                                               .add(FinalEntry.of(1, "one"))
 *                                               .add(FinalEntry.of(2, "two"))
 *                                               .map(FinalMap::of);
 * </pre>
 *
 * @param <K> the type of keys in this map.
 * @param <V> the type of values in this map.
 * @see Streamer#of(Object)
 * @see Streamer#add(Object)
 * @see Streamer#map(Function)
 * @see FinalEntry
 * @see FinalEntry#of(Object, Object)
 * @see #of(Streamable)
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

    private static <K, V> void putEntry(final Map<K, V> map, final Map.Entry<? extends K, ? extends V> entry) {
        map.put(entry.getKey(), entry.getValue());
    }

    @Override
    public final Set<Map.Entry<K, V>> entrySet() {
        // Already is immutable ...
        // noinspection AssignmentOrReturnOfFieldWithMutableType
        return entries;
    }
}
