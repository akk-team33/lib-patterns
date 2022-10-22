package de.team33.patterns.random.tarvos;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;

final class Types {

    private static final Map<Type, Collection<Type>> MATCHING = new ConcurrentHashMap<>(0);

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
        return Stream.of(Primary.values())
                     .filter(primary -> valueType.equals(primary.type))
                     .findAny()
                     .orElse(Primary.OBJECT).value;
    }

    private static Set<Type> matchingWith(final Type found) {
        return Stream.of(Primary.values())
                     .map(primary -> Arrays.asList(primary.type, primary.boxed))
                     .filter(matching -> matching.contains(found))
                     .findAny()
                     .map(HashSet::new)
                     .map(Collections::unmodifiableSet)
                     .orElseGet(() -> Collections.singleton(found));
    }

    static boolean isMatching(final Type desired, final Type found) {
        return MATCHING.computeIfAbsent(found, Types::matchingWith)
                       .contains(desired);
    }

    private enum Primary {

        BOOLEAN(boolean.class, Boolean.class, false),
        BYTE(byte.class, Byte.class, (byte) 0),
        SHORT(short.class, Short.class, (short) 0),
        INT(int.class, Integer.class, 0),
        LONG(long.class, Long.class, 0L),
        FLOAT(float.class, Float.class, 0.0f),
        DOUBLE(double.class, Double.class, 0.0),
        CHAR(char.class, Character.class, '\0'),
        OBJECT(Object.class, Object.class, null);

        final Type type;
        final Type boxed;
        final Object value;

        <T> Primary(final Class<T> type, final Class<T> boxed, final T value) {
            this.type = type;
            this.boxed = boxed;
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
