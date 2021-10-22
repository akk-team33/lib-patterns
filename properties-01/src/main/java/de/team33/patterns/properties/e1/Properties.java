package de.team33.patterns.properties.e1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Summarizes the logical properties of a particular type and provides some operations over them as a whole.
 *
 * <ul>
 *     <li>The names and values of the properties of a given instance of the associated type can be mapped onto a
 *     given {@link Map}.</li>
 *     <li>The values of the entries of a given {@link Map} can be mapped to a given instance of the associated type,
 *     provided that they correspond to its properties.</li>
 *     <li>The values of the properties of a given instance of the associated type can be copied to another instance
 *     of the type.</li>
 * </ul>
 * <p>
 * The primary purpose is to support the implementation of classes with value semantics, the total value of which
 * results directly from the values of their properties. In particular, the implementation of the following basic or
 * helpful methods can be supported:
 *
 * <ul>
 *     <li>A copy constructor</li>
 *     <li>A constructor that takes a map as a parameter</li>
 *     <li>A <em>toMap()</em> method</li>
 *     <li>The <em>equals()</em> method, e.g. using <em>toMap()</em> and comparing the resulting {@link Map}s</li>
 *     <li>The <em>hashCode()</em> method, e.g. using <em>toMap()</em> and ...</li>
 *     <li>The <em>toString()</em> method, e.g. using <em>toMap()</em> and ...</li>
 * </ul>
 *
 * @param <T> The type whose properties are summarizes.
 */
public final class Properties<T> {

    private final List<Property<T>> backing;

    private Properties(final List<Property<T>> backing) {
        this.backing = Collections.unmodifiableList(new ArrayList<>(backing));
    }

    /**
     * Creates a new instance by determining the logical properties of a particular class through reflection.
     * A {@link Mode} defines what exactly should be viewed as properties.
     */
    public static <T> Properties<T> of(final Class<T> tClass, final Mode mode) {
        return new Properties<>(mode.stream(tClass)
                                    .collect(Collectors.toList()));
    }

    public static <T, V> Builder<T> add(final String name,
                                        final Function<T, V> getter,
                                        final BiConsumer<T, ? super V> setter) {
        return new Builder<T>().add(name, getter, setter);
    }

    /**
     * Convenience method to get a {@link Map} from the properties of a given instance of the associated type.
     *
     * @see #map(Object, Map)
     */
    public final Map<String, Object> toMap(final T subject) {
        return map(subject, new TreeMap<>());
    }

    /**
     * Maps the properties of a given origin to a given target {@link Map}.
     *
     * @return the target {@link Map}.
     * @see #toMap(Object)
     */
    public final <M extends Map<String, Object>> M map(final T origin, final M target) {
        for (final Property<T> property : backing) {
            target.put(property.name(), property.valueOf(origin));
        }
        return target;
    }

    /**
     * Maps the entries of a given origin {@link Map} as properties to a given target.
     *
     * @return the target.
     */
    public final T map(final Map<?, ?> origin, final T target) {
        for (final Property<T> property : backing) {
            property.setValue(target, origin.get(property.name()));
        }
        return target;
    }

    /**
     * Copies the properties of a given origin to a given target.
     *
     * @return the target.
     */
    public final T copy(final T origin, final T target) {
        for (final Property<T> property : backing) {
            property.setValue(target, property.valueOf(origin));
        }
        return target;
    }

    /**
     * Defines possible modes for creating new {@link Properties} instances via reflection.
     *
     * @see #of(Class, Mode)
     */
    public enum Mode {

        /**
         * Specifies that all fields that are directly defined by the class in question
         * and that are not static, transient or synthetic are to be understood as logical properties.
         */
        BY_FIELDS_FLAT(Streaming::bySignificantFieldsFlat),

        /**
         * Specifies that all fields that are defined by the class in question or one of its superclasses
         * and that are not static, transient or synthetic are to be understood as logical properties.
         */
        BY_FIELDS_DEEP(Streaming::bySignificantFieldsDeep);

        @SuppressWarnings("rawtypes")
        private final Function streaming;

        <T> Mode(final Function<Class<T>, Stream<Property<T>>> streaming) {
            this.streaming = streaming;
        }

        @SuppressWarnings("unchecked")
        private <T> Stream<Property<T>> stream(final Class<T> tClass) {
            return (Stream<Property<T>>) streaming.apply(tClass);
        }
    }

    public static class Builder<T> {

        public Properties<T> build() {
            throw new UnsupportedOperationException("not yet implemented");
        }

        public <V> Builder<T> add(final String name,
                                  final Function<T, V> getter,
                                  final BiConsumer<T, ? super V> setter) {
            throw new UnsupportedOperationException("not yet implemented");
        }
    }

    private static final class Streaming {

        static <T> Stream<Property<T>> bySignificantFieldsFlat(final Class<T> tClass) {
            return Fields.streamDeclaredFlat(tClass)
                         .filter(Fields::isSignificant)
                         .peek(field -> field.setAccessible(true))
                         .map(field -> Fields.newProperty(tClass, field));
        }

        static <T> Stream<Property<T>> bySignificantFieldsDeep(final Class<T> tClass) {
            return Fields.streamDeclaredDeep(tClass)
                         .filter(Fields::isSignificant)
                         .peek(field -> field.setAccessible(true))
                         .map(field -> Fields.newProperty(tClass, field));
        }
    }
}
