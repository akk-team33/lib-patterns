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

    static <T> Mapping<T> using(final Function<T, Map<?, ?>> toMap,
                                final Function<Map<?, ?>, T> remap) {
        return new Mapping<T>() {
            @Override
            public Map<?, ?> map(final T origin) {
                return toMap.apply(origin);
            }

            @Override
            public T remap(final Map<?, ?> origin) {
                return remap.apply(origin);
            }
        };
    }

    Map<?, ?> map(T origin);

    T remap(Map<?, ?> origin);
}
