package de.team33.patterns.production.narvi;

import java.util.Objects;
import java.util.stream.Stream;

enum Implicit {

    BOOLEAN(boolean.class, false),
    BYTE(byte.class, (byte) 0),
    SHORT(short.class, (short) 0),
    INT(int.class, 0),
    LONG(long.class, 0L),
    FLOAT(float.class, 0.0f),
    DOUBLE(double.class, 0.0d),
    CHAR(char.class, '\0'),
    OTHER(Object.class, null);

    private final Class<?> type;
    private final Object trivial;

    <T> Implicit(final Class<T> type, final T trivial) {
        this.type = type;
        this.trivial = trivial;
    }

    static Implicit of(final Class<?> type) {
        return Stream.of(values())
                     .filter(value -> value.type.equals(type))
                     .findAny()
                     .orElse(OTHER);
    }

    final boolean is(final Object value) {
        return Objects.equals(value, trivial);
    }

    final Object value() {
        return trivial;
    }
}
