package de.team33.patterns.collection.mneme;

import de.team33.patterns.streamable.naiad.Streamable;

import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;

/**
 * An immutable {@link Map} implementation
 * that preserves the encounter order of its source and may contain {@code null} as key or value.
 * <p>
 * <p>
 * To build an instance you may use a {@link Stream} and {@link #collector()}, example:
 * <pre>{@code
 * final FinalMap<Long, String> map = Stream.of(FinalEntry.of(0L, "zero"),
 *                                              FinalEntry.of(1L, "one"),
 *                                              FinalEntry.of(2L, "two"))
 *                                          .collect(FinalMap.collector());
 * }</pre>
 *
 * @param <K> the type of keys in that map.
 * @param <V> the type of values in that map.
 * @see #empty()
 * @see #of(Object, Object)
 * @see #of(Map)
 */
public final class FinalMap<K, V> extends AbstractMap<K, V> {

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static final FinalMap EMPTY = new FinalMap(Map.of());

    private final FinalSet<Map.Entry<K, V>> entries;

    private FinalMap(final Map<? extends K, ? extends V> source) {
        final Function<Entry<? extends K, ? extends V>, Entry<K, V>> toFinal = FinalEntry.mapping();
        this.entries = source.entrySet()
                             .stream()
                             .map(toFinal)
                             .collect(FinalSet.collector());
    }

    private FinalMap(final Source<? extends Entry<? extends K, ? extends V>> source) {
        this(stage(source));
    }

    private static <K, V> Map<K, V> stage(final Source<? extends Entry<? extends K, ? extends V>> source) {
        return source.stream().collect(LinkedHashMap::new, FinalMap::putEntry, Map::putAll);
    }

    private static <K, V> void putEntry(final Map<K, V> map, final Map.Entry<? extends K, ? extends V> entry) {
        map.put(entry.getKey(), entry.getValue());
    }

    /**
     * Returns an empty {@link FinalMap}.
     *
     * @param <K> the formal type of keys in the resulting map.
     * @param <V> the formal type of values in the resulting map.
     */
    @SuppressWarnings({"unchecked", "AssignmentOrReturnOfFieldWithMutableType"})
    public static <K, V> FinalMap<K, V> empty() {
        return EMPTY;
    }

    /**
     * Returns a {@link FinalMap} containing a single mapping.
     *
     * @param <K> the type of keys in the resulting map.
     * @param <V> the type of values in the resulting map.
     */
    @SuppressWarnings("MethodOverridesStaticMethodOfSuperclass")
    public static <K, V> FinalMap<K, V> of(final K key, final V value) {
        return new FinalMap<>(Map.of(key, value));
    }

    /**
     * Returns a {@link FinalMap} created from the given <em>source</em>.
     *
     * @param <K> the type of keys in the resulting map.
     * @param <V> the type of values in the resulting map.
     */
    public static <K, V> FinalMap<K, V> of(final Streamable<? extends Map.Entry<? extends K, ? extends V>> source) {
        return new FinalMap<>(Source.cast(source::stream));
    }

    /**
     * Returns a {@link FinalMap} created from the given <em>source</em>.
     *
     * @param <K> the type of keys in the resulting map.
     * @param <V> the type of values in the resulting map.
     */
    public static <T, K, V> FinalMap<K, V> of(final Streamable<T> source,
                                              final Function<? super T, ? extends K> toKey,
                                              final Function<? super T, ? extends V> toValue) {
        return of(source, FinalEntry.mapping(toKey, toValue));
    }

    /**
     * Returns a {@link FinalMap} created from the given <em>source</em>.
     *
     * @param <K> the type of keys in the resulting map.
     * @param <V> the type of values in the resulting map.
     */
    public static <T, K, V> FinalMap<K, V>
    of(final Streamable<T> source, final Function<? super T, ? extends Entry<? extends K, ? extends V>> toEntry) {
        return of(() -> source.stream().map(toEntry));
    }

    /**
     * Returns a {@link FinalMap} created from the given <em>source</em>.
     *
     * @param <K> the type of keys in the resulting map.
     * @param <V> the type of values in the resulting map.
     */
    public static <K, V> FinalMap<K, V> of(final Map<? extends K, ? extends V> source) {
        return new FinalMap<>(source);
    }

    /**
     * Returns a {@link Collector} to {@linkplain Stream#collect(Collector) collect} map entries
     * into a new {@link FinalMap}.
     *
     * @param <K> the type of keys in the resulting map.
     * @param <V> the type of values in the resulting map.
     */
    public static <K, V> Collector<Entry<? extends K, ? extends V>, ?, FinalMap<K, V>> collector() {
        return Stage.collector(FinalMap::new);
    }

    @Override
    public final Set<Map.Entry<K, V>> entrySet() {
        // Already is immutable ...
        // noinspection AssignmentOrReturnOfFieldWithMutableType
        return entries;
    }
}
