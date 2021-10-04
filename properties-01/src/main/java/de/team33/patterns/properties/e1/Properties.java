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

public final class Properties<T> {

    private final List<Property<T>> backing;

    private Properties(final List<Property<T>> backing) {
        this.backing = Collections.unmodifiableList(new ArrayList<>(backing));
    }

    public static <T> Properties<T> of(final Class<T> tClass, final Mode mode) {
        return new Properties<>(mode.stream(tClass)
                                    .collect(Collectors.toList()));
    }

    public final Map<String, Object> toMap(final T subject) {
        return pass(subject, new TreeMap<>());
    }

    private BiConsumer<TreeMap<String, Object>, Property<T>> accumulator(final T subject) {
        return (map, property) -> map.put(property.name(), property.valueOf(subject));
    }

    private static <T> void put(final Map<String, Object> map, final Property<T> property) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    /**
     * Passes the properties of a given origin to a given target {@link Map}.
     *
     * @return the target {@link Map}.
     */
    public final <M extends Map<String, Object>> M pass(final T origin, final M target) {
        for (final Property<T> property : backing) {
            target.put(property.name(), property.valueOf(origin));
        }
        return target;
    }

    /**
     * Passes the entries of a given origin {@link Map} as properties to a given target.
     *
     * @return the target.
     */
    public final T pass(final Map<?, ?> origin, final T target) {
        for (final Property<T> property : backing) {
            property.setValue(target, origin.get(property.name()));
        }
        return target;
    }

    /**
     * Passes the properties of a given origin to a given target.
     *
     * @return the target.
     */
    public final T pass(final T origin, final T target) {
        for (final Property<T> property : backing) {
            property.setValue(target, property.valueOf(origin));
        }
        return target;
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

    public enum Mode {

        BY_FIELDS_FLAT(Streaming::bySignificantFieldsFlat),
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
}
