package de.team33.patterns.properties.e1;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Optional;
import java.util.stream.Stream;

class Fields {

    private static final int SYNTHETIC = 0x00001000;
    private static final int NOT_SIGNIFICANT = Modifier.STATIC | Modifier.TRANSIENT | SYNTHETIC;

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

    static boolean isSignificant(final Field field) {
        return isSignificant(field.getModifiers());
    }

    private static boolean isSignificant(final int modifiers) {
        return 0 == (modifiers & NOT_SIGNIFICANT);
    }
}
