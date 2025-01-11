package de.team33.patterns.normal.iocaste;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.function.Function;

import static java.util.Objects.requireNonNull;

/**
 * Represents a basic normalized data object.
 * <p>
 * Can be used as a normalized representation of primitive values, their wrapper classes, or strings.
 */
public class Basic {

    private final SubType type;
    private final String value;

    private Basic(final SubType type, final Object value) {
        this.type = type;
        this.value = requireNonNull(value, "A Basic value of null is not supported.").toString();
    }

    public static Basic of(final String value) {
        return new Basic(SubType.STRING, value);
    }

    public static Basic of(final Integer value) {
        return new Basic(SubType.INTEGER, value);
    }

    @Override
    public final boolean equals(final Object obj) {
        return (this == obj) || ((obj instanceof Basic) && value.equals(((Basic) obj).value));
    }

    @Override
    public final int hashCode() {
        return value.hashCode();
    }

    @Override
    public final String toString() {
        return value;
    }

    public final Boolean toBoolean() {
        return Boolean.valueOf(value);
    }

    public final Integer toInteger() {
        return Integer.valueOf(value);
    }

    private enum SubType {

        STRING(Casting.TO_STRING_TO_INTEGER),
        INTEGER(Integer.class::cast);

        private final Function<Object, Integer> toInt;

        SubType(final Function<Object, Integer> toInt) {
            this.toInt = toInt;
        }

        private interface Casting<R> extends Function<Object, R> {

            Casting<Integer> TO_STRING_TO_INTEGER = value -> {
                try {
                    return Integer.valueOf(value.toString());
                } catch (final NumberFormatException e) {
                    throw new RuntimeException(e);
                }
            };
        }
    }
}
