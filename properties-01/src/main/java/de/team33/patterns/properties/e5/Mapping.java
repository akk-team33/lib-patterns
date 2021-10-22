package de.team33.patterns.properties.e5;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

/**
 * Abstracts a tool which can map properties of an instance of a certain type in a certain way into a {@link Map}.
 *
 * @param <T> The type whose properties are to be mapped.
 */
@FunctionalInterface
public interface Mapping<T> {

    /**
     * Creates a new {@link Builder} and adds a link between name and getter method, which represents a property
     * of the associated type.
     *
     * @param <T> The type whose properties are to be mapped.
     */
    static <T> Builder<T> add(final String name, final Function<T, Object> getter) {
        return new Builder<T>().add(name, getter);
    }

    /**
     * Returns a {@link Map} that links the names of the properties to be mapped with methods that can determine the
     * corresponding values from an instance of the associated type.
     */
    Map<String, Function<T, Object>> getters();

    /**
     * Results in a concrete {@link TargetOperation} for a given origin instance of the associated type,
     * which can map its properties into a {@link Map}.
     */
    default TargetOperation<Map<String, Object>> map(final T origin) {
        return new MappingOperation<>(getters(), origin);
    }

    /**
     * Convenience method that maps the properties of a given origin instance of the associated type to a new,
     * empty {@link Map} and returns it.
     */
    default Map<String, Object> toMap(final T origin) {
        return map(origin).to(new TreeMap<>());
    }

    /**
     * Defines a builder for the declarative creation of a {@link Mapping}.
     *
     * @param <T> The type whose properties are to be mapped.
     */
    class Builder<T> {

        private final Map<String, Function<T, Object>> backing = new TreeMap<>();

        private static <T> Mapping<T> newMapping(final Map<String, Function<T, Object>> backing) {
            return () -> backing;
        }

        /**
         * Adds a link between name and getter method, which represents a property of the associated type.
         *
         * @return This.
         */
        public final Builder<T> add(final String name, final Function<T, Object> getter) {
            backing.put(name, getter);
            return this;
        }

        /**
         * Builds and returns a new {@link Mapping}.
         */
        public final Mapping<T> build() {
            return newMapping(Collections.unmodifiableMap(new TreeMap<>(backing)));
        }
    }
}
