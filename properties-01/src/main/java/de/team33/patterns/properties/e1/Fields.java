package de.team33.patterns.properties.e1;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public final class Fields {

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

    @SuppressWarnings("AnonymousInnerClass")
    private static <T> Accessor<T, Object> newAccessor(final Field field) {
        return new Accessor<T, Object>() {
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
                    throw new IllegalArgumentException(e.getMessage(), e);
                }
            }
        };
    }

    @SuppressWarnings("AnonymousInnerClass")
    public static <T> BiMapping<T> mapping(final Class<T> tClass, final Mode mode) {
        final Function<Field, String> naming = mode.namingOf(tClass);
        final Map<String, Accessor<T, Object>> accessors = mode.streaming.apply(tClass)
                                                                         .collect(toMap(naming,
                                                                                        Fields::newAccessor));
        return new BiMapping<T>() {
            @Override
            public TargetOperation<T> copy(final T origin) {
                return MappingUtil.copyOperation(accessors, origin);
            }

            @Override
            public TargetOperation<Map<String, Object>> map(final T origin) {
                return MappingUtil.mappingOperation(accessors, origin);
            }

            @Override
            public TargetOperation<T> remap(final Map<?, ?> origin) {
                return MappingUtil.reMappingOperation(accessors, origin);
            }
        };
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
             type -> field -> Fields.prefixed(type, field));

        private final Function<Class<?>, Stream<Field>> streaming;
        private final Function<Class<?>, ? extends Function<Field, String>> naming;

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
