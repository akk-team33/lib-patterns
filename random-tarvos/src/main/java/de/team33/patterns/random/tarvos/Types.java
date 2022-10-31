package de.team33.patterns.random.tarvos;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static java.util.Collections.unmodifiableMap;

final class Types {

    private static final Map<Type, Collection<Type>> MATCHING = newMatching();

    private Types() {
    }

    private static Map<Type, Collection<Type>> newMatching() {
        final Map<Type, Collection<Type>> result = new HashMap<>();
        for (final Primary primary : Primary.values()) {
            result.put(primary.type, primary.matching);
            result.put(primary.boxed, primary.matching);
        }
        return unmodifiableMap(result);
    }

    static Naming naming(final Type type) {
        final Class<? extends Type> typeClass = type.getClass();
        return Stream.of(Naming.values())
                     .filter(value -> value.typeClass.isAssignableFrom(typeClass))
                     .findAny()
                     .orElseThrow(() -> new NoSuchElementException(format("No entry found for type <%s>",
                                                                          typeClass)));
    }

    static Object defaultValue(final Type type) {
        return Stream.of(Primary.values())
                     .filter(primary -> type.equals(primary.type))
                     .findAny()
                     .map(primary -> primary.value)
                     .orElse(null);
    }

    static boolean isMatching(final Type desired, final Type found) {
        return Optional.ofNullable(MATCHING.get(found))
                       .orElseGet(() -> singleton(found))
                       .contains(desired);
    }

    @SuppressWarnings("PackageVisibleField")
    private enum Primary {

        BOOLEAN(boolean.class, Boolean.class, false),
        BYTE(byte.class, Byte.class, (byte) 0),
        SHORT(short.class, Short.class, (short) 0),
        INT(int.class, Integer.class, 0),
        LONG(long.class, Long.class, 0L),
        FLOAT(float.class, Float.class, 0.0f),
        DOUBLE(double.class, Double.class, 0.0),
        CHAR(char.class, Character.class, '\0');

        final Type type;
        final Type boxed;
        final List<Type> matching;
        final Object value;

        <T> Primary(final Class<T> type, final Class<T> boxed, final T value) {
            this.type = type;
            this.boxed = boxed;
            this.value = value;
            this.matching = asList(type, boxed);
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
