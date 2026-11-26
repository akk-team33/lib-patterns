package de.team33.patterns.records.triton;

import de.team33.patterns.enums.pan.Values;
import de.team33.patterns.json.charon.Json;
import de.team33.patterns.records.metis.Metis;
import de.team33.patterns.typing.proteus.Type;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
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

    static Object resolve(final Type<?> targetType, final Json.Value value) {
        return new Resolver(targetType).apply(value);
    }

    private Object apply(final Json.Value value) {
        if (Json.NULL == value) {
            return mapNull();
        } else if (mapping.isApplicable(value)) {
            return mapping.method(this)
                          .apply(value);
        }
        throw new IllegalArgumentException(
                "illegal Json type: %s - expected: %s".formatted(value.type(), mapping.jsonType));
    }

    @SuppressWarnings({"ReturnOfNull", "SameReturnValue"})
    private Object mapNull() {
        if (targetType.core().isPrimitive()) {
            throw new IllegalArgumentException("not nullable: " + targetType);
        }
        return null;
    }

    private boolean mapBoolean(final Json.Value source) {
        return source.asBoolean();
    }

    private byte mapByte(final Json.Value source) {
        return source.asBigDecimal().byteValueExact();
    }

    private short mapShort(final Json.Value source) {
        return source.asBigDecimal().shortValueExact();
    }

    private int mapInt(final Json.Value source) {
        return source.asBigDecimal().intValueExact();
    }

    private long mapLong(final Json.Value source) {
        return source.asBigDecimal().longValueExact();
    }

    private float mapFloat(final Json.Value source) {
        return source.asBigDecimal().floatValue();
    }

    private double mapDouble(final Json.Value source) {
        return source.asBigDecimal().doubleValue();
    }

    private char mapChar(final Json.Value source) {
        final String cString = source.asString();
        if (1 == cString.length()) {
            return cString.charAt(0);
        }
        throw new IllegalArgumentException("illegal character: '%s'".formatted(cString));
    }

    private BigInteger mapBigInteger(final Json.Value source) {
        return source.asBigDecimal().toBigIntegerExact();
    }

    private BigDecimal mapBigDecimal(final Json.Value source) {
        return source.asBigDecimal();
    }

    private String mapString(final Json.Value source) {
        return source.asString();
    }

    private Object mapStringable(final Json.Value source) {
        return Stringable.decode(targetType.core(), source.asString());
    }

    private Enum<?> mapEnum(final Json.Value source) {
        return mapEnum(source.asString());
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

    private Object mapArray(final Json.Value source) {
        return new ArrayMapper().map(source.asList());
    }

    private Object mapRecord(final Json.Value source) {
        return new RecordMapper().map(source.asMap());
    }

    private enum Mapping {

        BOOLEAN(isOneOf(boolean.class, Boolean.class), Json.Type.BOOLEAN, Resolver::mapBoolean),
        BYTE(isOneOf(byte.class, Byte.class), Json.Type.NUMBER, Resolver::mapByte),
        SHORT(isOneOf(short.class, Short.class), Json.Type.NUMBER, Resolver::mapShort),
        INT(isOneOf(int.class, Integer.class), Json.Type.NUMBER, Resolver::mapInt),
        LONG(isOneOf(long.class, Long.class), Json.Type.NUMBER, Resolver::mapLong),
        FLOAT(isOneOf(float.class, Float.class), Json.Type.NUMBER, Resolver::mapFloat),
        DOUBLE(isOneOf(double.class, Double.class), Json.Type.NUMBER, Resolver::mapDouble),
        BIG_INT(BigInteger.class::equals, Json.Type.NUMBER, Resolver::mapBigInteger),
        BIG_DEC(BigDecimal.class::equals, Json.Type.NUMBER, Resolver::mapBigDecimal),
        CHAR(isOneOf(char.class, Character.class), Json.Type.STRING, Resolver::mapChar),
        STRING(String.class::equals, Json.Type.STRING, Resolver::mapString),
        ENUM(Class::isEnum, Json.Type.STRING, Resolver::mapEnum),
        ARRAY(Class::isArray, Json.Type.ARRAY, Resolver::mapArray),
        RECORD(Class::isRecord, Json.Type.OBJECT, Resolver::mapRecord),
        STRINGABLE(Stringable::supports, Json.Type.STRING, Resolver::mapStringable);

        private static final Values<Mapping> VALUES = Values.of(Mapping.class);

        private final Predicate<Type<?>> responsibility;
        private final Json.Type jsonType;
        private final Method method;

        Mapping(final Predicate<Class<?>> responsibility,
                final Json.Type jsonType,
                final Method method) {
            this.responsibility = type -> responsibility.test(type.core());
            this.jsonType = jsonType;
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

        final Function<Json.Value, Object> method(final Resolver resolver) {
            return value -> method.map(resolver, value);
        }

        final boolean isApplicable(final Json.Value value) {
            return jsonType == value.type();
        }

        @FunctionalInterface
        interface Method {

            Object map(Resolver resolver, Json.Value value);
        }
    }

    private final class ArrayMapper {

        private final Type<?> componentType;

        private ArrayMapper() {
            componentType = targetType.actualParameters().get(0);
        }

        final Object map(final List<? extends Json.Value> values) {
            final int size = values.size();
            final Object array = Array.newInstance(componentType.core(), size);
            for (int index = 0; index < size; ++index) {
                final Object component = resolve(componentType, values.get(index));
                Array.set(array, index, component);
            }
            return array;
        }
    }

    private final class RecordMapper {

        private final Map<String, Type<?>> description;

        @SuppressWarnings({"rawtypes", "unchecked"})
        private RecordMapper() {
            this.description = Metis.description((Type) targetType);
        }

        final Object map(final Map<String, ? extends Json.Value> values) {
            final Map<String, Object> stage =
                    values.entrySet().stream()
                          .filter(entry -> description.containsKey(entry.getKey()))
                          .collect(HashMap::new, this::putNext, Map::putAll);
            //noinspection rawtypes,unchecked
            return Metis.toRecord((Type) targetType, stage);
        }

        private void putNext(final Map<? super String, Object> map,
                             final Map.Entry<String, ? extends Json.Value> entry) {
            final String name = entry.getKey();
            map.put(name, resolve(description.get(name), entry.getValue()));
        }
    }
}
