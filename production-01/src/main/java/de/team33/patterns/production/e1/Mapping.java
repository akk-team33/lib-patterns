package de.team33.patterns.production.e1;

import java.util.Map;
import java.util.function.Function;

/**
 * Abstracts a tool which can map properties of an instance of a certain type in a certain way into a {@link Map}
 * and vice versa.
 *
 * @param <T> The type whose properties are to be mapped.
 */
public interface Mapping<T> {

    public static <T> Mapping<T> combine(final Function<T, Map<String, Object>> toMap,
                                         final Function<Map<String, Object>, T> mapTo) {
        return new Mapping<T>() {
            @Override
            public Map<String, Object> map(final T origin) {
                return toMap.apply(origin);
            }

            @Override
            public T remap(final Map<?, ?> origin) {
                return mapTo.apply(origin);
            }
        };
    }

    Map<String, Object> map(T origin);

    T remap(Map<?, ?> origin);
}
