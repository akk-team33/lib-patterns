package de.team33.patterns.collection.mneme;

import java.util.AbstractMap;
import java.util.Map;
import java.util.function.Function;

/**
 * An immutable {@link Map.Entry} implementation that may contain {@code null} as key or value.
 *
 * @param <K> the type of the <em>key</em>
 * @param <V> the type of the <em>value</em>
 */
public final class FinalEntry<K, V> extends AbstractMap.SimpleImmutableEntry<K, V> {

    private FinalEntry(final K key, final V value) {
        super(key, value);
    }

    /**
     * Returns a new {@link FinalEntry} from the given <em>key</em> and <em>value</em>.
     *
     * @param <K> the type of the <em>key</em>
     * @param <V> the type of the <em>value</em>
     */
    public static <K, V> FinalEntry<K, V> of(final K key, final V value) {
        return new FinalEntry<>(key, value);
    }

    /**
     * Returns the given <em>entry</em> as a {@link FinalEntry}.
     * <p>
     * If the given <em>entry</em> is already a {@link FinalEntry}, it is returned unchanged.
     * Otherwise, a new {@link FinalEntry} created from the <em>key</em> and <em>value</em>
     * of the given <em>entry</em> is returned.
     *
     * @param <K> the type of the <em>key</em> of a resulting entry
     * @param <V> the type of the <em>value</em> of a resulting entry
     */
    public static <K, V> FinalEntry<K, V> of(final Map.Entry<? extends K, ? extends V> entry) {
        if (entry instanceof final FinalEntry<? extends K, ? extends V> finalEntry) {
            //noinspection unchecked
            return (FinalEntry<K, V>) finalEntry;
        } else {
            return new FinalEntry<>(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Returns a <em>mapping</em> {@link Function} that converts a given {@link Map.Entry}
     * to a {@link FinalEntry} with the same <em>key</em> and <em>value</em>,
     * and returns it as a plain {@link Map.Entry}.
     *
     * @param <K> the type of the <em>key</em> of a resulting entry
     * @param <V> the type of the <em>value</em> of a resulting entry
     */
    public static <K, V> Function<Map.Entry<? extends K, ? extends V>, Map.Entry<K, V>> mapping() {
        return FinalEntry::of;
    }

    /**
     * Returns a <em>mapping</em> {@link Function} that uses the given <em>toKey</em> and <em>toValue</em>
     * {@link Function}s to obtain a <em>key</em> and a <em>value</em> from a given original input,
     * combines the <em>key</em> and <em>value</em> to a new {@link FinalEntry},
     * and returns it as a plain {@link Map.Entry}.
     *
     * @param <T> the type of the original input
     * @param <K> the type of the <em>key</em> of a resulting entry
     * @param <V> the type of the <em>value</em> of a resulting entry
     */
    public static <T, K, V> Function<T, Map.Entry<K, V>> mapping(final Function<? super T, ? extends K> toKey,
                                                                 final Function<? super T, ? extends V> toValue) {
        return origin -> of(toKey.apply(origin), toValue.apply(origin));
    }

    /**
     * Returns a <em>mapping</em> {@link Function} that uses the given <em>toKey</em> {@link Function}
     * to obtain a <em>key</em> from a given <em>value</em>, combines the <em>key</em> and <em>value</em>
     * to a new {@link FinalEntry}, and returns it as a plain {@link Map.Entry}.
     *
     * @param <K> the type of the <em>key</em> of a resulting entry
     * @param <V> the type of the <em>value</em> of a resulting entry
     */
    public static <K, V> Function<V, Map.Entry<K, V>> mapping(final Function<? super V, ? extends K> toKey) {
        return value -> of(toKey.apply(value), value);
    }
}
