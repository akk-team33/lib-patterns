package de.team33.patterns.records.triton;

import de.team33.patterns.collection.mneme.FinalEntry;
import de.team33.patterns.enums.pan.Values;
import de.team33.patterns.json.charon.*;
import de.team33.patterns.records.metis.Metis;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.Predicate;
import java.util.stream.IntStream;

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

    static Json.Value map(final Object source) {
        if (null == source) {
            return Json.NULL;
        } else {
            final Class<?> sourceClass = source.getClass();
            return VALUES.findAny(value -> value.responsibility.test(sourceClass))
                         .map(value -> value.mapper)
                         .map(mapper -> mapper.map(source))
                         .orElseThrow(() -> new IllegalArgumentException(
                                 "cannot map " + sourceClass.getCanonicalName()));
        }
    }

    private static Json.Value mapBoolean(final boolean source) {
        return Json.valueOf(source);
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
        return mapBigDecimal(BigDecimal.valueOf(source));
    }

    private static JsonNumber mapFloat(final float source) {
        return mapBigDecimal(new BigDecimal(Float.toString(source)));
    }

    private static JsonNumber mapDouble(final double source) {
        return mapBigDecimal(new BigDecimal(Double.toString(source)));
    }

    private static JsonNumber mapBigInteger(final BigInteger source) {
        return mapBigDecimal(new BigDecimal(source));
    }

    private static JsonNumber mapBigDecimal(final BigDecimal source) {
        return new JsonNumber(source);
    }

    private static Json.Value mapEnum(final Enum<?> source) {
        return mapString(source.name());
    }

    private static JsonString mapChar(final char source) {
        return mapString(Character.toString(source));
    }

    private static JsonString mapStringable(final Object source) {
        return mapString(Stringable.encode(source));
    }

    private static JsonString mapString(final String source) {
        return new JsonString(source);
    }

    private static JsonArray mapArray(final Object array) {
        return IntStream.range(0, Array.getLength(array))
                        .mapToObj(index -> Array.get(array, index))
                        .map(Generalizer::map)
                        .collect(JsonArray.collector());
    }

    private static JsonObject mapRecord(final Record source) {
        return Metis.toMap(source).entrySet().stream()
                    .map(entry -> FinalEntry.of(entry.getKey(), map(entry.getValue())))
                    .collect(JsonObject.collector());
    }

    @FunctionalInterface
    interface Mapper<T> {

        Json.Value mapT(T source);

        @SuppressWarnings("unchecked")
        default Json.Value map(final Object source) {
            return mapT((T) source);
        }
    }
}
