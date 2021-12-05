package de.team33.test.patterns.production.shared;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.function.Function;
import java.util.stream.Stream;

final class Fields {

    private static final int SYNTHETIC = 0x00001000;
    private static final int NOT_SIGNIFICANT = Modifier.STATIC | Modifier.TRANSIENT | SYNTHETIC;

    private Fields() {
    }

    static <T> Mapping<T> mapping(final Class<T> tClass) {
        return new Mapping<T>(stream(tClass, Mode.DEEP).filter(Fields::isSignificant)
                                                       .map(Fields::setAccessible));
    }

    static Stream<Field> stream(final Class<?> subjectClass, final Mode mode) {
        return mode.streaming.apply(subjectClass);
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

    @SuppressWarnings("MethodOnlyUsedFromInnerClass")
    private static Stream<Field> streamDeep(final Class<?> subjectClass) {
        return (null == subjectClass)
                ? Stream.empty()
                : Stream.concat(streamDeep(subjectClass.getSuperclass()), streamStraight(subjectClass));
    }

    private static Stream<Field> streamStraight(final Class<?> subjectClass) {
        return Stream.of(subjectClass.getDeclaredFields());
    }

    enum Mode {

        STRAIGHT(Fields::streamStraight),
        DEEP(Fields::streamDeep);

        private final Function<Class<?>, Stream<Field>> streaming;

        Mode(final Function<Class<?>, Stream<Field>> streaming) {
            this.streaming = streaming;
        }
    }
}
