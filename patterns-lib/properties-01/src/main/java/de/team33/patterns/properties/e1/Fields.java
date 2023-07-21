package de.team33.patterns.properties.e1;

import de.team33.patterns.exceptional.e1.Converter;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

/**
 * A utility for handling {@link Field}s. In particular, it can generate a {@link BiMapping} based on the
 * {@link Field}s of a specific class.
 *
 * @see #mapping(Class, Mode)
 */
@Deprecated
public final class Fields {

    private Fields() {
    }

    private static <T> Accessor<T, Object> newAccessor(final Field field) {
        return Accessor.combine(Mutual.CONVERTER.function(field::get), Mutual.CONVERTER.biConsumer(field::set));
    }

    /**
     * Results in a {@link BiMapping} based on the {@link Field}s of a given {@link Class}.
     * Which {@link Field}s are taken into account depends on the specified {@code mode}.
     *
     * @param <T> The type that is represented by the given {@link Class}.
     */
    public static <T> BiMapping<T> mapping(final Class<T> tClass, final Mode mode) {
        return new AccMapping<>(mode.streaming.apply(tClass)
                                              .collect(toMap(mode.namingOf(tClass),
                                                             Fields::newAccessor)));
    }

    /**
     * Defines modes for determining relevant fields of a class that represent its properties.
     */
    public enum Mode {

        /**
         * In this mode, all fields are taken into account that were defined directly with the relevant class
         * and that are not static, transient or synthetic.
         */
        FLAT(type -> Mutual.streamDeclared(type).filter(Mutual::isSignificant)
                           .map(Mutual::setAccessible),
             type -> Field::getName),

        /**
         * In this mode, all fields are taken into account that were defined with the relevant class or one of its
         * superclasses and that are not static, transient or synthetic.
         */
        DEEP(type -> Mutual.streamDeclaredDeep(type).filter(Mutual::isSignificant)
                           .map(Mutual::setAccessible),
             type -> field -> Mutual.prefixed(type, field));

        final Function<Class<?>, Stream<Field>> streaming;
        private final Function<Class<?>, ? extends Function<Field, String>> naming;

        @SuppressWarnings("BoundedWildcard")
        Mode(final Function<Class<?>, Stream<Field>> streaming,
             final Function<Class<?>, ? extends Function<Field, String>> naming) {
            this.streaming = streaming;
            this.naming = naming;
        }

        Function<Field, String> namingOf(final Class<?> context) {
            return naming.apply(context);
        }
    }

    private static final class Mutual {

        static final int SYNTHETIC = 0x00001000;
        static final int NOT_SIGNIFICANT = Modifier.STATIC | Modifier.TRANSIENT | SYNTHETIC;
        static final Converter CONVERTER =
                Converter.using(cause -> new IllegalArgumentException(cause.getMessage(), cause));

        static boolean isSignificant(final Field field) {
            return isSignificant(field.getModifiers());
        }

        static boolean isSignificant(final int modifiers) {
            return 0 == (modifiers & NOT_SIGNIFICANT);
        }

        static Stream<Field> streamDeclared(final Class<?> cls) {
            return Stream.of(cls.getDeclaredFields());
        }

        static Stream<Field> streamDeclaredDeep(final Class<?> cls) {
            return Stream.concat(superStreamOf(cls), streamDeclared(cls));
        }

        static Stream<Field> superStreamOf(final Class<?> cls) {
            return Optional.ofNullable(cls.getSuperclass())
                           .map(Mutual::streamDeclaredDeep)
                           .orElseGet(Stream::empty);
        }

        static Field setAccessible(final Field field) {
            field.setAccessible(true);
            return field;
        }

        static String prefixed(final Class<?> context, final Field field) {
            if (field.getDeclaringClass().equals(context)) {
                return field.getName();
            } else {
                return "." + prefixed(context.getSuperclass(), field);
            }
        }
    }
}
