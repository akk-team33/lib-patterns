package de.team33.patterns.collection.mneme;

import de.team33.patterns.streamable.galatea.Buffer;
import de.team33.patterns.streamable.galatea.Streamable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;

/**
 * An {@link ImmutableMap} implementation
 * that preserves the encounter order of its source (if any) and may contain {@code null} as key or value.
 * <p>
 * The encounter order (if any) is reflected by {@link #entrySet()}, {@link #keySet()} and {@link #values()}.
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
 * @see #of(Entry)
 * @see #by(Map)
 * @see #by(Streamable)
 */
public final class FinalMap<K, V> extends ImmutableMap<K, V> {

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

    private FinalMap(final Streamable<? extends Entry<? extends K, ? extends V>> source) {
        this(stage(source));
    }

    private static <K, V> Map<K, V> stage(final Streamable<? extends Entry<? extends K, ? extends V>> source) {
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
     * Returns a {@link FinalMap} containing a single <em>entry</em>.
     *
     * @param <K> the type of keys in the resulting map.
     * @param <V> the type of values in the resulting map.
     */
    public static <K, V> FinalMap<K, V> of(final Map.Entry<? extends K, ? extends V> entry) {
        return new FinalMap<>(Map.ofEntries(entry));
    }

    /**
     * Returns a {@link FinalMap} created from the given <em>source</em>.
     *
     * @param <K> the type of keys in the resulting map.
     * @param <V> the type of values in the resulting map.
     */
    public static <K, V> FinalMap<K, V> by(final Streamable<? extends Map.Entry<? extends K, ? extends V>> source) {
        return new FinalMap<>(Streamable.cast(source));
    }

    /**
     * @deprecated use {@link #by(Streamable)} instead.
     */
    @Deprecated
    public static <K, V> FinalMap<K, V> of(final Streamable<? extends Map.Entry<? extends K, ? extends V>> source) {
        return by(source);
    }

    /**
     * Returns a {@link FinalMap} created from the given <em>source</em>.
     *
     * @param <K> the type of keys in the resulting map.
     * @param <V> the type of values in the resulting map.
     */
    public static <T, K, V> FinalMap<K, V> by(final Streamable<T> source,
                                              final Function<? super T, ? extends K> toKey,
                                              final Function<? super T, ? extends V> toValue) {
        return by(source, FinalEntry.mapping(toKey, toValue));
    }

    /**
     * @deprecated use {@link #by(Streamable, Function, Function)} instead.
     */
    @Deprecated
    public static <T, K, V> FinalMap<K, V> of(final Streamable<T> source,
                                              final Function<? super T, ? extends K> toKey,
                                              final Function<? super T, ? extends V> toValue) {
        return by(source, toKey, toValue);
    }

    /**
     * Returns a {@link FinalMap} created from the given <em>source</em>.
     *
     * @param <K> the type of keys in the resulting map.
     * @param <V> the type of values in the resulting map.
     */
    public static <T, K, V>
    FinalMap<K, V> by(final Streamable<T> source,
                      final Function<? super T, ? extends Entry<? extends K, ? extends V>> toEntry) {
        return by(() -> source.stream().map(toEntry));
    }

    /**
     * @deprecated use {@link #by(Streamable, Function)} instead.
     */
    @Deprecated
    public static <T, K, V>
    FinalMap<K, V> of(final Streamable<T> source,
                      final Function<? super T, ? extends Entry<? extends K, ? extends V>> toEntry) {
        return by(source, toEntry);
    }

    /**
     * Returns a {@link FinalMap} created from the given <em>source</em>.
     * <p>
     * Returns <em>source</em> if it is already a {@link FinalMap}.
     *
     * @param <K> the type of keys in the resulting map.
     * @param <V> the type of values in the resulting map.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <K, V> FinalMap<K, V> by(final Map<? extends K, ? extends V> source) {
        if (source instanceof final FinalMap finalMap) {
            return finalMap;
        } else {
            return new FinalMap<>(source);
        }
    }

    /**
     * @deprecated use {@link #by(Map)} instead.
     */
    @Deprecated
    public static <K, V> FinalMap<K, V> of(final Map<? extends K, ? extends V> source) {
        return by(source);
    }

    /**
     * Returns a {@link Collector} to {@linkplain Stream#collect(Collector) collect} map entries
     * into a new {@link FinalMap}.
     *
     * @param <K> the type of keys in the resulting map.
     * @param <V> the type of values in the resulting map.
     */
    public static <K, V> Collector<Entry<? extends K, ? extends V>, ?, FinalMap<K, V>> collector() {
        return Buffer.collector(FinalMap::new);
    }

    /**
     * Returns a {@link Set} view of the mappings contained in <em>this</em> map.
     * <p>
     * An {@link ImmutableMap} returns an {@link ImmutableSet}.
     * <p>
     * This implementation ensures that each element of the resulting set is a {@link FinalEntry}.
     */
    @Override
    public final ImmutableSet<Map.Entry<K, V>> entrySet() {
        // Already is immutable ...
        // noinspection AssignmentOrReturnOfFieldWithMutableType
        return entries;
    }
}
