package de.team33.patterns.records.triton;

import de.team33.patterns.enums.pan.Values;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import java.util.function.Predicate;

enum Generalizer {

    BOOLEAN(Boolean.class, Generalizer::mapBoolean),
    BYTE(Byte.class, Generalizer::mapByte),
    SHORT(Short.class, Generalizer::mapShort),
    INT(Integer.class, Generalizer::mapInt),
    LONG(Long.class, Generalizer::mapLong),
    FLOAT(Float.class, Generalizer::mapFloat),
    DOUBLE(Double.class, Generalizer::mapDouble),
    BIG_INT(BigInteger.class, Generalizer::mapBigInteger),
    BIG_DEC(BigDecimal.class, Generalizer::mapBigDecimal),
    CHAR(Character.class, Generalizer::mapChar),
    STRING(String.class, Generalizer::mapString),
    ENUM(Enum.class, Generalizer::mapEnum),
    ARRAY(Class::isArray, Generalizer::mapArray),
    RECORD(Record.class, Generalizer::mapRecord),
    STRINGABLE(Stringable::supports, Generalizer::mapStringable);

    private static final Values<Generalizer> VALUES = Values.of(Generalizer.class);

    private final Predicate<Class<?>> responsibility;
    @SuppressWarnings("rawtypes")
    private final Mapper mapper;

    <T> Generalizer(final Class<T> normalClass, final Mapper<T> mapper) {
        this(normalClass::isAssignableFrom, mapper);
    }

    <T> Generalizer(final Predicate<Class<?>> responsibility, final Mapper<T> mapper) {
        this.responsibility = responsibility;
        this.mapper = mapper;
    }

    static JsonValue map(final Object source) {
        if (null == source) {
            return JsonValue.NULL;
        } else {
            final Class<?> sourceClass = source.getClass();
            return VALUES.findAny(value -> value.responsibility.test(sourceClass))
                         .map(value -> value.mapper)
                         .map(mapper -> mapper.map(source))
                         .orElseThrow(() -> new IllegalArgumentException(
                                 "cannot map " + sourceClass.getCanonicalName()));
        }
    }

    private static JsonBoolean mapBoolean(final boolean source) {
        return new JsonBoolean(source);
    }

    private static JsonNumber mapByte(final byte source) {
        return mapLong(source);
    }

    private static JsonNumber mapShort(final short source) {
        return mapLong(source);
    }

    private static JsonNumber mapInt(final int source) {
        return mapLong(source);
    }

    private static JsonNumber mapLong(final long source) {
        return new JsonNumber(BigDecimal.valueOf(source));
    }

    private static JsonNumber mapFloat(final float source) {
        return new JsonNumber(new BigDecimal(Float.toString(source)));
    }

    private static JsonNumber mapDouble(final double source) {
        return new JsonNumber(new BigDecimal(Double.toString(source)));
    }

    private static JsonNumber mapBigInteger(final BigInteger source) {
        return new JsonNumber(new BigDecimal(source));
    }

    private static JsonNumber mapBigDecimal(final BigDecimal source) {
        return new JsonNumber(source);
    }

    private static JsonValue mapEnum(final Enum<?> source) {
        return new JsonString(source.name());
    }

    private static JsonString mapChar(final char source) {
        return new JsonString(Character.toString(source));
    }

    private static JsonString mapString(final String source) {
        return new JsonString(source);
    }

    private static JsonString mapStringable(final Object source) {
        return new JsonString(Stringable.encode(source));
    }

    private static JsonArray mapArray(final Object array) {
        final JsonArray.Builder builder = JsonArray.builder();
        final int length = Array.getLength(array);
        for (int index = 0; index < length; ++index) {
            final JsonValue jsonValue = map(Array.get(array, index));
            builder.add(jsonValue);
        }
        return builder.build();
    }

    private static JsonObject mapRecord(final Record source) {
        final var builder = JsonObject.builder();
        final var stage = Triton.toMap(source);
        for (final Map.Entry<String, Object> entry : stage.entrySet()) {
            builder.put(entry.getKey(), map(entry.getValue()));
        }
        return builder.build();
    }

    @FunctionalInterface
    interface Mapper<T> {

        JsonValue mapT(T source);

        @SuppressWarnings("unchecked")
        default JsonValue map(final Object source) {
            return mapT((T) source);
        }
    }
}
