package de.team33.patterns.records.triton;

import de.team33.patterns.enums.pan.Values;
import de.team33.patterns.typing.proteus.Type;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

@SuppressWarnings("MethodMayBeStatic")
final class Resolver {

    private final Type<?> targetType;
    private final Mapping mapping;

    private Resolver(final Type<?> targetType) {
        this.targetType = targetType;
        this.mapping = Mapping.of(targetType);
        if (null == mapping) {
            throw new IllegalArgumentException("cannot resolve " + targetType);
        }
    }

    static Object resolve(final Type<?> targetType, final JsonValue value) {
        return new Resolver(targetType).apply(value);
    }

    private Object apply(final JsonValue value) {
        if (JsonValue.NULL == value) {
            return mapNull();
        } else if (mapping.isApplicable(value)) {
            return mapping.method(this)
                          .apply(value);
        }
        throw new IllegalArgumentException(
                "illegal Json type: %s - expected: %s".formatted(value.getClass(), mapping.jsonClass));
    }

    @SuppressWarnings({"ReturnOfNull", "SameReturnValue"})
    private Object mapNull() {
        if (targetType.core().isPrimitive()) {
            throw new IllegalArgumentException("not nullable: " + targetType);
        }
        return null;
    }

    private boolean mapBoolean(final JsonBoolean source) {
        return source.value();
    }

    private byte mapByte(final JsonNumber source) {
        return source.value().byteValueExact();
    }

    private short mapShort(final JsonNumber source) {
        return source.value().shortValueExact();
    }

    private int mapInt(final JsonNumber source) {
        return source.value().intValueExact();
    }

    private long mapLong(final JsonNumber source) {
        return source.value().longValueExact();
    }

    private float mapFloat(final JsonNumber source) {
        return source.value().floatValue();
    }

    private double mapDouble(final JsonNumber source) {
        return source.value().doubleValue();
    }

    private char mapChar(final JsonString source) {
        final String cString = source.value();
        if (1 == cString.length()) {
            return cString.charAt(0);
        }
        throw new IllegalArgumentException("illegal character: '%s'".formatted(cString));
    }

    private BigInteger mapBigInteger(final JsonNumber source) {
        return source.value().toBigIntegerExact();
    }

    private BigDecimal mapBigDecimal(final JsonNumber source) {
        return source.value();
    }

    private String mapString(final JsonString source) {
        return source.value();
    }

    private Object mapStringable(final JsonString source) {
        return Stringable.decode(targetType.core(), source.value());
    }

    private Enum<?> mapEnum(final JsonString source) {
        return mapEnum(source.value());
    }

    private Enum<?> mapEnum(final String name) {
        return Stream.of(targetType.core().getEnumConstants())
                     .map(Enum.class::cast)
                     .filter(e -> e.name().equals(name))
                     .findAny()
                     .orElseThrow(() -> enumNotFound(name));
    }

    private IllegalStateException enumNotFound(final String name) {
        return new IllegalStateException(
                "Cannot find enum value %s of type %s".formatted(name, targetType));
    }

    private Object mapArray(final JsonArray source) {
        return new ArrayMapper().map(source);
    }

    private Object mapRecord(final JsonObject source) {
        return new RecordMapper().map(source);
    }

    private enum Mapping {

        BOOLEAN(isOneOf(boolean.class, Boolean.class), JsonBoolean.class, Resolver::mapBoolean),
        BYTE(isOneOf(byte.class, Byte.class), JsonNumber.class, Resolver::mapByte),
        SHORT(isOneOf(short.class, Short.class), JsonNumber.class, Resolver::mapShort),
        INT(isOneOf(int.class, Integer.class), JsonNumber.class, Resolver::mapInt),
        LONG(isOneOf(long.class, Long.class), JsonNumber.class, Resolver::mapLong),
        FLOAT(isOneOf(float.class, Float.class), JsonNumber.class, Resolver::mapFloat),
        DOUBLE(isOneOf(double.class, Double.class), JsonNumber.class, Resolver::mapDouble),
        BIG_INT(BigInteger.class::equals, JsonNumber.class, Resolver::mapBigInteger),
        BIG_DEC(BigDecimal.class::equals, JsonNumber.class, Resolver::mapBigDecimal),
        CHAR(isOneOf(char.class, Character.class), JsonString.class, Resolver::mapChar),
        STRING(String.class::equals, JsonString.class, Resolver::mapString),
        ENUM(Class::isEnum, JsonString.class, Resolver::mapEnum),
        ARRAY(Class::isArray, JsonArray.class, Resolver::mapArray),
        RECORD(Class::isRecord, JsonObject.class, Resolver::mapRecord),
        STRINGABLE(Stringable::supports, JsonString.class, Resolver::mapStringable);

        private static final Values<Mapping> VALUES = Values.of(Mapping.class);

        private final Predicate<Type<?>> responsibility;
        private final Class<? extends JsonValue> jsonClass;
        @SuppressWarnings("rawtypes")
        private final Method method;

        <T extends JsonValue> Mapping(final Predicate<Class<?>> responsibility,
                                      final Class<T> jsonClass,
                                      final Method<T> method) {
            this.responsibility = type -> responsibility.test(type.core());
            this.jsonClass = jsonClass;
            this.method = method;
        }

        private static Predicate<Class<?>> isOneOf(final Class<?>... classes) {
            final Set<Class<?>> expected = Set.of(classes);
            return expected::contains;
        }

        static Mapping of(final Type<?> targetClass) {
            return VALUES.findAny(mapping -> mapping.responsibility.test(targetClass))
                         .orElse(null);
        }

        final Function<JsonValue, Object> method(final Resolver resolver) {
            return value -> method.map(resolver, value);
        }

        final boolean isApplicable(final JsonValue value) {
            return jsonClass.isInstance(value);
        }

        @FunctionalInterface
        interface Method<T extends JsonValue> {

            Object mapT(Resolver resolver, T value);

            @SuppressWarnings("unchecked")
            default Object map(final Resolver resolver, final JsonValue value) {
                return mapT(resolver, (T) value);
            }
        }
    }

    private final class ArrayMapper {

        private final Type<?> componentType;

        private ArrayMapper() {
            componentType = targetType.actualParameters().get(0);
        }

        final Object map(final JsonArray source) {
            final Object array = Array.newInstance(componentType.core(), source.size());
            for (int index = 0; index < source.size(); ++index) {
                final var component = resolve(componentType, source.get(index));
                Array.set(array, index, component);
            }
            return array;
        }
    }

    private final class RecordMapper {

        @SuppressWarnings("rawtypes")
        private final Description description;

        @SuppressWarnings({"rawtypes", "unchecked"})
        private RecordMapper() {
            this.description = Triton.description((Type) targetType);
        }

        @SuppressWarnings("unchecked")
        final Object map(final JsonObject source) {
            final Map<String, Object> stage =
                    source.stream()
                          .filter(entry -> description.names().contains(entry.name()))
                          .collect(HashMap::new, this::put, Map::putAll);
            return Triton.toRecord(description.type(), stage);
        }

        @SuppressWarnings("BoundedWildcard")
        private void put(final Map<String, Object> map, final JsonObject.Entry entry) {
            final String name = entry.name();
            map.put(name, resolve(description.componentType(name), entry.value()));
        }
    }
}
