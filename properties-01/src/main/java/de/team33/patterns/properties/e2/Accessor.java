package de.team33.patterns.properties.e2;

import java.util.function.BiConsumer;
import java.util.function.Function;

interface Accessor<T, V> extends Function<T, V>, BiConsumer<T, V> {

    static <T, V> Accessor<T, V> combine(final Function<T, V> getter,
                                         final BiConsumer<T, V> setter) {
        return new Accessor<T, V>() {
            @Override
            public V apply(final T t) {
                return getter.apply(t);
            }

            @Override
            public void accept(final T t, final V v) {
                setter.accept(t, v);
            }
        };
    }
}
