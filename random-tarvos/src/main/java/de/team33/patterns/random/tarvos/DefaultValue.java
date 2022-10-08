package de.team33.patterns.random.tarvos;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * Represents values with which variable fields are initialized depending on their type, unless this is done explicitly.
 * In most cases, more precisely for all object types, the value is simply {@code null}.
 * It is different for all primitive types.
 */
enum DefaultValue {

    /**
     * The default Value of a primitive {@code boolean} field.
     */
    BOOLEAN(boolean.class, false),

    /**
     * The default Value of a primitive {@code byte} field.
     */
    BYTE(byte.class, (byte) 0),

    /**
     * The default Value of a primitive {@code short} field.
     */
    SHORT(short.class, (short) 0),

    /**
     * The default Value of a primitive {@code int} field.
     */
    INT(int.class, 0),

    /**
     * The default Value of a primitive {@code long} field.
     */
    LONG(long.class, 0L),

    /**
     * The default Value of a primitive {@code float} field.
     */
    FLOAT(float.class, 0.0f),

    /**
     * The default Value of a primitive {@code double} field.
     */
    DOUBLE(double.class, 0.0d),

    /**
     * The default Value of a primitive {@code char} field.
     */
    CHAR(char.class, '\0'),

    /**
     * The default Value of a non-primitive field.
     */
    OBJECT(Object.class, null);

    private final Class<?> type;
    private final Object value;

    <T> DefaultValue(final Class<T> type, final T value) {
        this.type = type;
        this.value = value;
    }

    static DefaultValue of(final Class<?> type) {
        return Stream.of(values())
                     .filter(value -> value.type.equals(type))
                     .findAny()
                     .orElse(OBJECT);
    }

    final boolean is(final Object sample) {
        return Objects.equals(sample, value);
    }

    final Object value() {
        return value;
    }
}
