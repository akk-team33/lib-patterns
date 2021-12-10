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
public final class Fields {

    private static final int SYNTHETIC = 0x00001000;
    private static final int NOT_SIGNIFICANT = Modifier.STATIC | Modifier.TRANSIENT | SYNTHETIC;
    private static final Converter CONVERTER =
            Converter.using(cause -> new IllegalArgumentException(cause.getMessage(), cause));

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

    private static Field setAccessible(final Field field) {
        field.setAccessible(true);
        return field;
    }

    private static String prefixed(final Class<?> context, final Field field) {
        if (field.getDeclaringClass().equals(context)) {
            return field.getName();
        } else {
            return "." + prefixed(context.getSuperclass(), field);
        }
    }

    private static <T> Accessor<T, Object> newAccessor(final Field field) {
        return Accessor.combine(CONVERTER.function(field::get), CONVERTER.biConsumer(field::set));
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
        FLAT(type -> streamDeclared(type).filter(Fields::isSignificant)
                                         .map(Fields::setAccessible),
             type -> Field::getName),

        /**
         * In this mode, all fields are taken into account that were defined with the relevant class or one of its
         * superclasses and that are not static, transient or synthetic.
         */
        DEEP(type -> streamDeclaredDeep(type).filter(Fields::isSignificant)
                                             .map(Fields::setAccessible),
             type -> field -> prefixed(type, field));

        private final Function<Class<?>, Stream<Field>> streaming;
        private final Function<Class<?>, ? extends Function<Field, String>> naming;

        @SuppressWarnings("BoundedWildcard")
        Mode(final Function<Class<?>, Stream<Field>> streaming,
             final Function<Class<?>, ? extends Function<Field, String>> naming) {
            this.streaming = streaming;
            this.naming = naming;
        }

        private Function<Field, String> namingOf(final Class<?> context) {
            return naming.apply(context);
        }
    }
}
