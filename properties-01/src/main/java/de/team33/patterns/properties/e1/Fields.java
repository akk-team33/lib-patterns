package de.team33.patterns.properties.e1;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Optional;
import java.util.stream.Stream;

final class Fields {

    private static final int SYNTHETIC = 0x00001000;
    private static final int NOT_SIGNIFICANT = Modifier.STATIC | Modifier.TRANSIENT | SYNTHETIC;

    private Fields() {
    }

    static Stream<Field> streamDeclaredFlat(final Class<?> cls) {
        return Stream.of(cls.getDeclaredFields());
    }

    static Stream<Field> streamDeclaredDeep(final Class<?> cls) {
        return Stream.concat(superStreamOf(cls), streamDeclaredFlat(cls));
    }

    static <T> Property<T> newProperty(final Class<T> tClass, final Field field) {
        return new PropertyImpl<>(tClass, field);
    }

    private static Stream<Field> superStreamOf(final Class<?> cls) {
        return Optional.ofNullable(cls.getSuperclass())
                       .map(Fields::streamDeclaredDeep)
                       .orElseGet(Stream::empty);
    }

    static boolean isSignificant(final Field field) {
        return isSignificant(field.getModifiers());
    }

    private static boolean isSignificant(final int modifiers) {
        return 0 == (modifiers & NOT_SIGNIFICANT);
    }

    private static class PropertyImpl<T> implements Property<T> {

        private static final String CANNOT_GET_VALUE = "cannot get value of field from a given subject:%n" +
                "- field: %s%n" +
                "- class of subject: %s%n" +
                "- value of subject: %s%n";

        private static final String CANNOT_SET_VALUE = "cannot set value of field to a given subject:%n" +
                "- field: %s%n" +
                "- value: %s%n" +
                "- class of subject: %s%n" +
                "- value of subject: %s%n";

        private final Class<T> context;
        private final Field field;

        PropertyImpl(final Class<T> context, final Field field) {
            this.context = context;
            this.field = field;
        }

        private static String prefix(final Class<?> declaring, final Class<?> context) {
            return (declaring == context) ? "" : ("." + prefix(declaring, context.getSuperclass()));
        }

        @Override
        public final String name() {
            return prefix(field.getDeclaringClass(), context) + field.getName();
        }

        @Override
        public final Object valueOf(final T subject) {
            try {
                return field.get(subject);
            } catch (final Exception e) {
                throw new IllegalArgumentException(
                        String.format(CANNOT_GET_VALUE, field, subject.getClass(), subject), e);
            }
        }

        @Override
        public final void setValue(final T subject, final Object value) {
            try {
                field.set(subject, value);
            } catch (final Exception e) {
                throw new IllegalArgumentException(
                        String.format(CANNOT_SET_VALUE, field, value, subject.getClass(), subject), e);
            }
        }
    }
}
