package de.team33.patterns.properties.e1a;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Properties<T> {

    private final List<Property<T>> backing;

    private Properties(final List<Property<T>> backing) {
        this.backing = Collections.unmodifiableList(new ArrayList<>(backing));
    }

    public static <T> Properties<T> of(final Class<T> tClass, final Mode mode) {
        return new Properties<>(mode.stream(tClass)
                                    .collect(Collectors.toList()));
    }

    public final Map<String, Object> toMap(final T subject) {
        return backing.stream()
                      .collect(TreeMap::new, accumulator(subject), Map::putAll);
    }

    private BiConsumer<TreeMap<String, Object>, Property<T>> accumulator(final T subject) {
        return (map, property) -> map.put(property.name(), property.valueOf(subject));
    }

    private static <T> void put(final Map<String, Object> map, final Property<T> property) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    /**
     * Maps the properties of a given origin to a given target {@link Map} and returns the {@link Map}.
     */
    public <M extends Map<String, Object>> M map(final T origin, final M target) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    private static final class Streaming {

        static <T> Stream<Property<T>> bySignificantFieldsFlat(final Class<T> tClass) {
            return Fields.flatStreamOf(tClass)
                         .filter(Fields::isSignificant)
                         .peek(field -> field.setAccessible(true))
                         .map(field -> new Fields.Property<>(tClass, field));
        }

        static <T> Stream<Property<T>> bySignificantFieldsDeep(final Class<T> tClass) {
            return Fields.deepStreamOf(tClass)
                         .filter(Fields::isSignificant)
                         .peek(field -> field.setAccessible(true))
                         .map(field -> new Fields.Property<>(tClass, field));
        }

        static <T> Stream<Property<T>> byPublicGetters(final Class<T> tClass) {
            return Stream.of(tClass.getMethods())
                         .filter(Methods::isSignificant)
                         .map(Methods.Info::new)
                         .filter(Methods.Info::isGetter)
                         .collect(() -> new Methods.Collector<T>(),
                                  Methods.Collector::add,
                                  Methods.Collector::addAll)
                         .stream();
        }

        static <T> Stream<Property<T>> byPublicAccesors(final Class<T> tClass) {
            return Stream.of(tClass.getMethods())
                         .filter(Methods::isSignificant)
                         .map(Methods.Info::new)
                         .filter(Methods.Info::isAccessor)
                         .collect(() -> new Methods.Collector<T>(),
                                  Methods.Collector::add,
                                  Methods.Collector::addAll)
                         .stream();
        }
    }

    public enum Mode {

        BY_FIELDS_FLAT(Streaming::bySignificantFieldsFlat),
        BY_FIELDS_DEEP(Streaming::bySignificantFieldsDeep),
        BY_PUBLIC_GETTERS(Streaming::byPublicGetters),
        BY_PUBLIC_ACCESSORS(Streaming::byPublicAccesors);

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
