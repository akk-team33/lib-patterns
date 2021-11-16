package de.team33.test.patterns.production.shared;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.stream.Stream;

final class Fields {

    private static final int SYNTHETIC = 0x00001000;
    private static final int NOT_SIGNIFICANT = Modifier.STATIC | Modifier.TRANSIENT | SYNTHETIC;

    private Fields() {
    }

    static <T> Mapping<T> mapping(final Class<T> tClass) {
        return new Mapping<T>(stream(tClass).filter(Fields::isSignificant)
                                            .map(Fields::setAccessible));
    }

    static boolean isSignificant(final Field field) {
        return isSignificant(field.getModifiers());
    }

    private static boolean isSignificant(final int modifiers) {
        return 0 == (modifiers & NOT_SIGNIFICANT);
    }

    static Field setAccessible(final Field field) {
        field.setAccessible(true);
        return field;
    }

    static <T> Stream<Field> stream(final Class<T> subjectClass) {
        return (null == subjectClass)
                ? Stream.empty()
                : Stream.concat(stream(subjectClass.getSuperclass()),
                                Stream.of(subjectClass.getDeclaredFields()));
    }
}
