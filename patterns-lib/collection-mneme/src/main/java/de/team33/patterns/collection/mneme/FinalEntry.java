package de.team33.patterns.collection.mneme;

import java.util.AbstractMap;
import java.util.Map;
import java.util.function.Function;

public final class FinalEntry<K, V> extends AbstractMap.SimpleImmutableEntry<K, V> {

    private FinalEntry(final K key, final V value) {
        super(key, value);
    }

    public static <K, V> FinalEntry<K, V> of(final Map.Entry<? extends K, ? extends V> entry) {
        if (entry instanceof final FinalEntry<? extends K, ? extends V> finalEntry) {
            //noinspection unchecked
            return (FinalEntry<K, V>) finalEntry;
        } else {
            return new FinalEntry<>(entry.getKey(), entry.getValue());
        }
    }

    public static <K, V> FinalEntry<K, V> of(final K key, final V value) {
        return new FinalEntry<>(key, value);
    }

    public static <T, K, V> Function<T, Map.Entry<K, V>> mapping(final Function<? super T, ? extends K> toKey,
                                                                 final Function<? super T, ? extends V> toValue) {
        return origin -> of(toKey.apply(origin), toValue.apply(origin));
    }

    public static <K, V> Function<V, Map.Entry<K, V>> mapping(final Function<? super V, ? extends K> toKey) {
        return origin -> of(toKey.apply(origin), origin);
    }
}
