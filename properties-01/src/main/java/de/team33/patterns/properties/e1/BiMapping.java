package de.team33.patterns.properties.e1;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.Function;


/**
 * A combination of {@link Mapping} and {@link ReMapping} that additionally allows copying properties
 * between two instances of the associated type.
 *
 * @param <T> The type whose properties are to be mapped.
 */
@FunctionalInterface
public interface BiMapping<T> extends Mapping<T>, ReMapping<T> {

    /**
     * Creates a new {@link Builder} and adds a link between name and getter method, which represents a
     * property of the associated type.
     *
     * @param <T> The type whose properties are to be mapped.
     */
    static <T, V> Builder<T> add(final String name, final Function<T, V> getter, final BiConsumer<T, V> setter) {
        return new Builder<T>().add(name, getter, setter);
    }

    Map<String, Accessor<T>> accessors();

    @Override
    default Map<String, Function<T, Object>> getters() {
        return new HashMap<>(accessors());
    }

    @Override
    default Map<String, BiConsumer<T, Object>> setters() {
        return new HashMap<>(accessors());
    }

    default TargetOperation<T> copy(final T origin) {
        return new CopyOperation<>(accessors(), origin);
    }


    /**
     * Defines a builder for the declarative creation of a {@link Mapping}.
     *
     * @param <T> The type whose properties are to be mapped.
     */
    class Builder<T> {

        private final Map<String, Accessor<T>> backing = new TreeMap<>();

        private static <T> BiMapping<T> newMapping(final Map<String, Accessor<T>> backing) {
            return () -> backing;
        }

        /**
         * Adds a link between name and getter method, which represents a property of the associated type.
         *
         * @return This.
         */
        public final <V> Builder<T> add(final String name,
                                        final Function<T, V> getter,
                                        final BiConsumer<T, V> setter) {
            backing.put(name, combine(getter, setter));
            return this;
        }

        @SuppressWarnings({"rawtypes", "unchecked", "AnonymousInnerClass"})
        private Accessor<T> combine(final Function getter, final BiConsumer setter) {
            return new Accessor<T>() {
                @Override
                public void accept(final T t, final Object o) {
                    setter.accept(t, o);
                }

                @Override
                public Object apply(final T t) {
                    return getter.apply(t);
                }
            };
        }

        /**
         * Builds and returns a new {@link Mapping}.
         */
        public final BiMapping<T> build() {
            return newMapping(Collections.unmodifiableMap(new TreeMap<>(backing)));
        }
    }
}
