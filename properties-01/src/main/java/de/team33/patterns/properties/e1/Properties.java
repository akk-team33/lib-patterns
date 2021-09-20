package de.team33.patterns.properties.e1;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Properties<T> {

    private final Collection<Property<T>> backing;

    private Properties(final Builder<T> builder) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    private Properties(final Collection<Property<T>> backing) {
        this.backing = backing;
    }

    public static <T> Builder<T> add(final String name, final Function<T, Object> getter) {
        return new Builder<T>().add(name, getter);
    }

    public static <T> Builder<T> add(final String name,
                                     final Function<T, Object> getter,
                                     final BiConsumer<T, Object> setter) {
        return new Builder<T>().add(name, getter, setter);
    }

    public static <T> Stage<T> of(final Class<T> subjectClass) {
        return new Stage<>(subjectClass);
    }

    public final boolean equals(final T subject, final T other) {
        return toMap(subject).equals(toMap(other));
    }

    public final int hashCode(final T subject) {
        return toMap(subject).hashCode();
    }

    public final String toString(final T subject) {
        return toMap(subject).toString();
    }

    private Map<String, Object> toMap(final T subject) {
        return backing.stream()
                      .collect(TreeMap::new, (map, prop) -> map.put(prop.name(), prop.valueOf(subject)), Map::putAll);
    }

    private static class Streaming {

        private static <T> Stream<Property<T>> bySignificantFieldsFlat(final Class<T> tClass) {
            return Fields.flatStreamOf(tClass)
                         .filter(Fields::isSignificant)
                         .peek(field -> field.setAccessible(true))
                         .map(FieldProperty::new);
        }

        private static <T> Stream<Property<T>> bySignificantFieldsDeep(final Class<T> tClass) {
            return Fields.deepStreamOf(tClass)
                         .filter(Fields::isSignificant)
                         .peek(field -> field.setAccessible(true))
                         .map(FieldProperty::new);
        }

        private static <T> Stream<Property<T>> byPublicGetters(final Class<T> tClass) {
            return Methods.streamOf(tClass)
                          .filter(Methods::isGetter)
                          .collect(() -> new Aggregator<T>(), Aggregator::add, Aggregator::addAll)
                          .stream();
        }
    }

    public enum Strategy {

        FIELDS_FLAT(Streaming::bySignificantFieldsFlat),

        FIELDS_DEEP(Streaming::bySignificantFieldsDeep),

        PUBLIC_GETTERS(Streaming::byPublicGetters),

        PUBLIC_GETTERS_AND_SETTERS(null);

        @SuppressWarnings("rawtypes")
        private final Function streaming;

        <T> Strategy(final Function<Class<T>, Stream<Property<T>>> streaming) {
            this.streaming = streaming;
        }

        @SuppressWarnings("unchecked")
        final <T> Stream<Property<T>> stream(final Class<T> subjectClass) {
            return (Stream<Property<T>>) streaming.apply(subjectClass);
        }
    }

    public static class Stage<T> {

        private final Class<T> subjectClass;

        private Stage(final Class<T> subjectClass) {
            this.subjectClass = subjectClass;
        }

        public Properties<T> by(final Strategy strategy) {
            return new Properties<T>(strategy.stream(subjectClass)
                                             .collect(Collectors.toList()));
        }
    }

    public static class Builder<T> {

        public Builder<T> add(final String name, final Function<T, Object> getter) {
            throw new UnsupportedOperationException("not yet implemented");
        }

        public Builder<T> add(final String name, final Function<T, Object> getter, final BiConsumer<T, Object> setter) {
            throw new UnsupportedOperationException("not yet implemented");
        }

        public Properties<T> build() {
            return new Properties<>(this);
        }
    }
}
