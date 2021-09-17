package de.team33.patterns.properties.e1;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.stream.Stream;

class Fields {

    static Stream<Field> flatStreamOf(final Class<?> cls) {
        return Stream.of(cls.getDeclaredFields());
    }

    static Stream<Field> deepStreamOf(final Class<?> cls) {
        return Stream.concat(superStreamOf(cls), flatStreamOf(cls));
    }

    private static Stream<Field> superStreamOf(final Class<?> cls) {
        return Optional.ofNullable(cls.getSuperclass())
                       .map(Fields::deepStreamOf)
                       .orElseGet(Stream::empty);
    }
}
