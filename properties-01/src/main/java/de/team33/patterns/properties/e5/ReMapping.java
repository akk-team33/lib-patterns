package de.team33.patterns.properties.e5;

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
     * Creates a new {@link Mapping.Builder} and adds a link between name and getter method, which represents a
     * property of the associated type.
     *
     * @param <T> The type whose properties are to be re-mapped.
     */
    static <T, V> ReMapping.Builder<T> add(final String name, final BiConsumer<T, V> setter) {
        return new Builder<T>().add(name, setter);
    }

    /**
     * Returns a {@link Map} that links the names of the properties to be re-mapped with methods that can
     * set the corresponding values to an instance of the associated type.
     */
    Map<String, ? extends BiConsumer<T, Object>> backing();

    /**
     * Results in a concrete {@link TargetOperation} for a given origin {@link Map},
     * which can map its entries as properties into an instance of the associated type.
     */
    default TargetOperation<T> map(final Map<?, ?> origin) {
        return new ReMappingOperation<>(backing(), origin);
    }

    /**
     * Defines a builder for the declarative creation of a {@link ReMapping}.
     *
     * @param <T> The type whose properties are to be re-mapped.
     */
    class Builder<T> {

        @SuppressWarnings("rawtypes")
        private final Map<String, BiConsumer> backing = new TreeMap<>();

        @SuppressWarnings("rawtypes")
        private static <T> ReMapping<T> newMapping(final Map backing) {
            //noinspection unchecked
            return () -> backing;
        }

        /**
         * Adds a link between name and setter method, which represents a property of the associated type.
         *
         * @return This.
         */
        public final <V> ReMapping.Builder<T> add(final String name, final BiConsumer<T, V> setter) {
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
