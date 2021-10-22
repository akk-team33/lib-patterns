package de.team33.patterns.properties.e5;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public class Fields {

    private static final int SYNTHETIC = 0x00001000;
    private static final int NOT_SIGNIFICANT = Modifier.STATIC | Modifier.TRANSIENT | SYNTHETIC;

    private Fields() {
    }

    private static Stream<Field> streamDeclared(final Class<?> cls) {
        return Stream.of(cls.getDeclaredFields());
    }

    private static Stream<Field> streamDeclaredDeep(final Class<?> cls) {
        return Stream.concat(superStreamOf(cls), streamDeclared(cls));
    }

    private static Stream<Field> superStreamOf(final Class<?> cls) {
        return Optional.ofNullable(cls.getSuperclass())
                       .map(Fields::streamDeclaredDeep)
                       .orElseGet(Stream::empty);
    }

    private static boolean isSignificant(final Field field) {
        return isSignificant(field.getModifiers());
    }

    private static boolean isSignificant(final int modifiers) {
        return 0 == (modifiers & NOT_SIGNIFICANT);
    }

    private static void setAccessible(final Field field) {
        field.setAccessible(true);
    }

    @SuppressWarnings("AnonymousInnerClass")
    private static <T> Accessor<T> newAccessor(final Field field) {
        return new Accessor<T>() {
            @Override
            public void accept(final T t, final Object o) {
                try {
                    field.set(t, o);
                } catch (final IllegalAccessException e) {
                    throw new IllegalArgumentException(e.getMessage(), e);
                }
            }

            @Override
            public Object apply(final T t) {
                try {
                    return field.get(t);
                } catch (final IllegalAccessException e) {
                    throw new IllegalArgumentException("not yet implemented", e);
                }
            }
        };
    }

    public static <T> BiMapping<T> mapping(final Class<T> tClass, final Mode mode) {
        final Map<String, Accessor<T>> accessors = mode.streaming.apply(tClass)
                                                                 .collect(toMap(Field::getName, Fields::newAccessor));
        return () -> accessors;
    }

    /**
     * Defines modes for determining relevant fields of a class that represent its properties.
     */
    public enum Mode {

        /**
         * In this mode, all fields are taken into account that were defined directly with the relevant class
         * and that are not static, transient or synthetic.
         */
        STRAIGHT(type -> streamDeclared(type).filter(Fields::isSignificant)
                                             .peek(Fields::setAccessible)),

        /**
         * In this mode, all fields are taken into account that were defined with the relevant class or one of its
         * superclasses and that are not static, transient or synthetic.
         */
        DEEP(type -> streamDeclaredDeep(type).filter(Fields::isSignificant)
                                             .peek(Fields::setAccessible));

        private final Function<Class<?>, Stream<Field>> streaming;

        Mode(final Function<Class<?>, Stream<Field>> streaming) {
            this.streaming = streaming;
        }
    }
}
