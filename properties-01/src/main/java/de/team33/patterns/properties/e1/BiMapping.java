package de.team33.patterns.properties.e1;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.Function;


/**
 * Abstracts a tool which can bidirectionally map properties of an instance of a certain type to and from a
 * {@link Map}. In Addition, it can copy the properties from one instance of the type to another.
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
     * Results in a {@link TargetOperation} for a given origin instance of the associated type,
     * which can copy its properties into another instance of that type.
     */
    TargetOperation<T> copy(T origin);

    /**
     * Defines a builder for the declarative creation of a {@link BiMapping}.
     *
     * @param <T> The type whose properties are to be mapped.
     */
    class Builder<T> {

        @SuppressWarnings("rawtypes")
        private final Map<String, Accessor> backing = new TreeMap<>();

        @SuppressWarnings({"rawtypes", "unchecked", "AnonymousInnerClass"})
        private static <T> BiMapping<T> newMapping(final Map accessors) {
            return new BiMapping<T>() {
                @Override
                public TargetOperation<Map<String, Object>> map(final T origin) {
                    return MappingUtil.mappingOperation(accessors, origin);
                }

                @Override
                public TargetOperation<T> remap(final Map<?, ?> origin) {
                    return MappingUtil.reMappingOperation(accessors, origin);
                }

                @Override
                public TargetOperation<T> copy(final T origin) {
                    return MappingUtil.copyOperation(accessors, origin);
                }
            };
        }

        /**
         * Adds a link between name and getter method, which represents a property of the associated type.
         *
         * @return {@code this}.
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
