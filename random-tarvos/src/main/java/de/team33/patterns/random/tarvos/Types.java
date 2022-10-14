package de.team33.patterns.random.tarvos;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;

final class Types {

    private Types() {
    }

    static Naming naming(final Type type) {
        final Class<? extends Type> typeClass = type.getClass();
        return Stream.of(Naming.values())
                     .filter(value -> value.typeClass.isAssignableFrom(typeClass))
                     .findAny()
                     .orElseThrow(() -> new NoSuchElementException(format("No entry found for type <%s>",
                                                                          typeClass)));
    }

    static Object defaultValue(final Type valueType) {
        return Stream.of(DefaultValue.values())
                     .filter(v -> valueType.equals(v.type))
                     .findAny()
                     .orElse(DefaultValue.OBJECT).value;
    }

    enum DefaultValue {
        BOOLEAN(boolean.class, false),
        BYTE(byte.class, (byte) 0),
        SHORT(short.class, (short) 0),
        INT(int.class, 0),
        LONG(long.class, 0L),
        FLOAT(float.class, 0.0f),
        DOUBLE(double.class, 0.0),
        CHAR(char.class, '\0'),
        OBJECT(Object.class, null);

        private final Class<?> type;
        private final Object value;

        <T> DefaultValue(final Class<T> type, final T value) {
            this.type = type;
            this.value = value;
        }
    }

    enum Naming {

        CLASS(Class.class, Class::getSimpleName, type -> ""),
        PARAMETERIZED(ParameterizedType.class, Naming::toSimpleName, Naming::toParameters),
        OTHER(Type.class, Type::getTypeName, type -> "");

        private final Class<?> typeClass;
        @SuppressWarnings("rawtypes")
        private final Function toSimpleName;
        @SuppressWarnings("rawtypes")
        private final Function toParameters;

        <T extends Type> Naming(final Class<T> typeClass,
                                final Function<T, String> toSimpleName,
                                final Function<T, String> toParameters) {
            this.typeClass = typeClass;
            this.toSimpleName = toSimpleName;
            this.toParameters = toParameters;
        }

        private static String toParameters(final ParameterizedType type) {
            return Arrays.stream(type.getActualTypeArguments())
                         .map(pType -> naming(pType).simpleName(pType))
                         .collect(Collectors.joining(", ", "<", ">"));
        }

        private static String toSimpleName(final ParameterizedType type) {
            final Type rawType = type.getRawType();
            return naming(rawType).simpleName(rawType);
        }

        final String simpleName(final Type type) {
            //noinspection unchecked
            return (String) toSimpleName.apply(type);
        }

        final String parameterizedName(final Type type) {
            //noinspection unchecked
            return simpleName(type) + toParameters.apply(type);
        }
    }
}
