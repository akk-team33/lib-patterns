package de.team33.patterns.json.charon;

import de.team33.patterns.value.sinope.Equation;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

@SuppressWarnings("StaticInheritance")
public class JsonNumber implements JsonValue {

    private static final Equation<JsonNumber> EQUATION = Equation.of(JsonNumber.class, number -> number.value);

    private final BigDecimal value;

    private JsonNumber(final BigDecimal value) {
        this.value = value;
    }

    public static JsonNumber of(final byte value) {
        return new JsonNumber(BigDecimal.valueOf(value));
    }

    public static JsonNumber of(final short value) {
        return new JsonNumber(BigDecimal.valueOf(value));
    }

    public static JsonNumber of(final int value) {
        return new JsonNumber(BigDecimal.valueOf(value));
    }

    public static JsonNumber of(final long value) {
        return new JsonNumber(BigDecimal.valueOf(value));
    }

    public static JsonNumber of(final float value) {
        return new JsonNumber(new BigDecimal(Float.toString(value)));
    }

    public static JsonNumber of(final double value) {
        return new JsonNumber(new BigDecimal(Double.toString(value)));
    }

    public static JsonNumber of(final BigInteger value) {
        return new JsonNumber(new BigDecimal(value));
    }

    public static JsonNumber of(final BigDecimal value) {
        return new JsonNumber(Objects.requireNonNull(value));
    }

    public final byte byteValue() {
        return value.byteValue();
    }

    public final short shortValue() {
        return value.shortValue();
    }

    public final int intValue() {
        return value.intValue();
    }

    public final long longValue() {
        return value.longValue();
    }

    public final float floatValue() {
        return value.floatValue();
    }

    public final double doubleValue() {
        return value.doubleValue();
    }

    public final BigInteger toBigInteger() {
        return value.toBigInteger();
    }

    public final BigDecimal toBigDecimal() {
        return value;
    }

    @Override
    public final boolean equals(final Object obj) {
        return EQUATION.equals(this, obj);
    }

    @Override
    public final int hashCode() {
        return EQUATION.hashCode(this);
    }

    @Override
    public final String toString() {
        return EQUATION.toString(this);
    }
}