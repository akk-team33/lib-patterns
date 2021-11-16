package de.team33.patterns.properties.e2;

import de.team33.patterns.properties.e1.TargetOperation;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.Function;


/**
 * A combination of {@link Mapping} and {@link ReMapping} for instances of an associated type.
 *
 * @param <T> The type whose properties are to be mapped.
 */
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


    /**
     * Defines a builder for the declarative creation of a {@link BiMapping}.
     *
     * @param <T> The type whose properties are to be mapped.
     */
    class Builder<T> {

        @SuppressWarnings("rawtypes")
        private final Map<String, Accessor> backing = new TreeMap<>();

        @SuppressWarnings({"unchecked", "rawtypes"})
        private static <T> BiMapping<T> newMapping(final Map<String, Accessor> accessors) {
            return new BiMapping<T>() {
                @Override
                public TargetOperation<Map<String, Object>> map(final T origin) {
                    return new MappingOperation(accessors, origin);
                }

                @Override
                public TargetOperation<T> remap(final Map<?, ?> origin) {
                    return new ReMappingOperation(accessors, origin);
                }
            };
        }

        /**
         * Adds a link between name and getter method, which represents a property of the associated type.
         *
         * @return This.
         */
        public final <V> Builder<T> add(final String name,
                                        final Function<T, V> getter,
                                        final BiConsumer<T, V> setter) {
            backing.put(name, Accessor.combine(getter, setter));
            return this;
        }

        /**
         * Builds and returns a new {@link Mapping}.
         */
        public final BiMapping<T> build() {
            return newMapping(Collections.unmodifiableMap(new TreeMap<>(backing)));
        }
    }
}
