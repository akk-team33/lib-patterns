package de.team33.patterns.records.triton;

import de.team33.patterns.enums.pan.Values;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

class Generalizer {

    private final Mapping mapping;

    private Generalizer(final Class<?> sourceClass) {
        this.mapping = Mapping.of(sourceClass);
        if (null == mapping) {
            throw new IllegalArgumentException("cannot map " + sourceClass.getCanonicalName());
        }
    }

    static JsonValue map(final Object source) {
        if (null == source) {
            return JsonValue.NULL;
        } else {
            return new Generalizer(source.getClass()).apply(source);
        }
    }

    private static Object getRecordValue(final Record source, final Method accessor) {
        accessor.setAccessible(true);
        try {
            return accessor.invoke(source);
        } catch (final IllegalAccessException | InvocationTargetException e) {
            // Difficult to test ...
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    private JsonValue apply(final Object source) {
        return mapping.method(this)
                      .apply(source);
    }

    private JsonBoolean mapBoolean(final boolean source) {
        return new JsonBoolean(source);
    }

    private JsonNumber mapByte(final byte source) {
        return mapLong(source);
    }

    private JsonNumber mapShort(final short source) {
        return mapLong(source);
    }

    private JsonNumber mapInt(final int source) {
        return mapLong(source);
    }

    private JsonNumber mapLong(final long source) {
        return new JsonNumber(BigDecimal.valueOf(source));
    }

    private JsonNumber mapFloat(final float source) {
        return new JsonNumber(new BigDecimal(Float.toString(source)));
    }

    private JsonNumber mapDouble(final double source) {
        return new JsonNumber(new BigDecimal(Double.toString(source)));
    }

    private JsonNumber mapBigInteger(final BigInteger source) {
        return new JsonNumber(new BigDecimal(source));
    }

    private JsonNumber mapBigDecimal(final BigDecimal source) {
        return new JsonNumber(source);
    }

    private JsonValue mapEnum(final Enum<?> source) {
        return new JsonString(source.name());
    }

    private JsonString mapChar(final char source) {
        return new JsonString(Character.toString(source));
    }

    private JsonString mapString(final String source) {
        return new JsonString(source);
    }

    private JsonString mapStringable(final Object source) {
        return new JsonString(Stringable.encode(source));
    }

    private JsonArray mapArray(final Object array) {
        final JsonArray.Builder builder = JsonArray.builder();
        final int length = Array.getLength(array);
        for (int index = 0; index < length; ++index) {
            final JsonValue jsonValue = Generalizer.map(Array.get(array, index));
            builder.add(jsonValue);
        }
        return builder.build();
    }

    private JsonObject mapRecord(final Record source) {
        final var builder = JsonObject.builder();
        final var stage = Triton.toMap(source);
        for (final Map.Entry<String, Object> entry : stage.entrySet()) {
            builder.put(entry.getKey(), Generalizer.map(entry.getValue()));
        }
        return builder.build();
    }

    private enum Mapping {

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

        private static final Values<Mapping> VALUES = Values.of(Mapping.class);

        private final Predicate<Class<?>> responsibility;
        @SuppressWarnings("rawtypes")
        private final Method method;

        <T> Mapping(final Class<T> normalClass, final Method<T> method) {
            this(normalClass::isAssignableFrom, method);
        }

        <T> Mapping(final Predicate<Class<?>> responsibility, final Method<T> method) {
            this.responsibility = responsibility;
            this.method = method;
        }

        static Mapping of(final Class<?> sourceClass) {
            return VALUES.findAny(value -> value.responsibility.test(sourceClass))
                         .orElse(null);
        }

        final Function<Object, JsonValue> method(final Generalizer mapper) {
            return source -> method.map(mapper, source);
        }

        @FunctionalInterface
        interface Method<T> {

            JsonValue mapT(Generalizer mapper, T source);

            @SuppressWarnings("unchecked")
            default JsonValue map(final Generalizer mapper, final Object source) {
                return mapT(mapper, (T) source);
            }
        }
    }
}
