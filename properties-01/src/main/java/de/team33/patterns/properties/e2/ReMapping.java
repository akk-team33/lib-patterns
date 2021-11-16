package de.team33.patterns.properties.e2;

import de.team33.patterns.properties.e1.TargetOperation;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiConsumer;


/**
 * Abstracts a tool which can re-map properties of an instance of a certain type in a certain way from a
 * {@link Map}.
 *
 * @param <T> The type whose properties are to be re-mapped.
 */
@FunctionalInterface
public interface ReMapping<T> {

    /**
     * Creates a new {@link ReMapping.Builder} and adds a link between name and setter method, which represents a
     * property of the associated type.
     *
     * @param <T> The type whose properties are to be re-mapped.
     */
    static <T, V> Builder<T> add(final String name, final BiConsumer<T, V> setter) {
        return new Builder<T>().add(name, setter);
    }

    /**
     * Results in a {@link TargetOperation} for a given origin {@link Map},
     * which can map its entries as properties into an instance of the associated type.
     */
    TargetOperation<T> remap(final Map<?, ?> origin);

    /**
     * Defines a builder for the declarative creation of a {@link ReMapping}.
     *
     * @param <T> The type whose properties are to be re-mapped.
     */
    class Builder<T> {

        @SuppressWarnings("rawtypes")
        private final Map<String, BiConsumer> backing = new TreeMap<>();

        @SuppressWarnings({"rawtypes", "unchecked"})
        private static <T> ReMapping<T> newMapping(final Map backing) {
            final Map<String, BiConsumer<T, Object>> setters = Collections.unmodifiableMap(new TreeMap<>(backing));
            return origin -> new ReMappingOperation<>(setters, origin);
        }

        /**
         * Adds a link between name and setter method, which represents a property of the associated type.
         *
         * @return This.
         */
        public final <V> Builder<T> add(final String name, final BiConsumer<T, V> setter) {
            backing.put(name, setter);
            return this;
        }

        /**
         * Builds and returns a new {@link ReMapping}.
         */
        public final ReMapping<T> build() {
            return newMapping(Collections.unmodifiableMap(new TreeMap<>(backing)));
        }
    }
}
