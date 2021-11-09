package de.team33.patterns.math.e1;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

public class BigRational extends Number {

    public static final BigRational ZERO = valueOf(BigInteger.ZERO, BigInteger.ONE);
    public static final BigRational ONE = valueOf(BigInteger.ONE, BigInteger.ONE);

    private final BigInteger numerator;
    private final BigInteger denominator;

    private BigRational(final BigInteger numerator, final BigInteger denominator) {
        final BigInteger gcd = numerator.gcd(denominator);
        this.numerator = numerator.divide(gcd);
        this.denominator = denominator.divide(gcd);
    }

    private static List<Object> toList(final BigRational br) {
        return Arrays.asList(br.numerator, br.denominator);
    }

    public static BigRational valueOf(final BigInteger numerator, final BigInteger denominator) {
        switch (denominator.signum()) {
        case 1:
            return new BigRational(numerator, denominator);
        case -1:
            return new BigRational(numerator.negate(), denominator.negate());
        default:
            throw new IllegalArgumentException("the denominator must not be ZERO");
        }
    }

    public static BigRational valueOf(final BigInteger integer) {
        return valueOf(integer, BigInteger.ONE);
    }

    public static BigRational valueOf(final long numerator, final long denominator) {
        return valueOf(BigInteger.valueOf(numerator), BigInteger.valueOf(denominator));
    }

    public static BigRational valueOf(final long integer) {
        return valueOf(BigInteger.valueOf(integer));
    }

    public static BigRational valueOf(final double value) {
        return (value < 0.0) ? valueOfPositive(-value).negative() : valueOfPositive(value);
    }

    private static BigRational valueOfPositive(final double value) {
        int scal = 0;
        while (Math.scalb(value, -scal) > Long.MAX_VALUE) {
            scal += 8;
        }
        return valueOfPositive(Math.scalb(value, -scal), scal);
    }

    private static BigRational valueOfPositive(final double value, final int upScal) {
        int scal = 0;
        while (isFraction(Math.scalb(value, scal))) {
            scal += 8;
        }
        return valueOfIntegral(Math.scalb(value, scal), upScal, scal);
    }

    private static BigRational valueOfIntegral(final double value, final int upScal, final int dwnScal) {
        //noinspection NumericCastThatLosesPrecision
        return valueOf(BigInteger.valueOf((long) value).shiftLeft(upScal), BigInteger.ONE.shiftLeft(dwnScal));
    }

    private static boolean isFraction(final double value) {
        return Math.floor(value) < value;
    }

    public final BigRational negative() {
        return new BigRational(numerator.negate(), denominator);
    }

    public final BigRational inverse() {
        return new BigRational(denominator, numerator);
    }

    public final BigRational add(final BigRational addend) {
        return new BigRational(numerator.multiply(addend.denominator)
                                        .add(addend.numerator.multiply(denominator)),
                               denominator.multiply(addend.denominator));
    }

    public final BigRational subtract(final BigRational subtrahend) {
        return new BigRational(numerator.multiply(subtrahend.denominator)
                                        .subtract(subtrahend.numerator.multiply(denominator)),
                               denominator.multiply(subtrahend.denominator));
    }

    public final BigRational multiply(final BigRational multiplier) {
        return new BigRational(numerator.multiply(multiplier.numerator),
                               denominator.multiply(multiplier.denominator));
    }

    public final BigRational divide(final BigRational divisor) {
        return new BigRational(numerator.multiply(divisor.denominator),
                               denominator.multiply(divisor.numerator));
    }

    public final BigInteger getNumerator() {
        return numerator;
    }

    public final BigInteger getDenominator() {
        return denominator;
    }

    @Override
    public final double doubleValue() {
        return numerator.doubleValue() / denominator.doubleValue();
    }

    @Override
    public final float floatValue() {
        return numerator.floatValue() / denominator.floatValue();
    }

    @Override
    public final long longValue() {
        return toBigInteger().longValue();
    }

    @Override
    public final int intValue() {
        return toBigInteger().intValue();
    }

    public final BigInteger toBigInteger() {
        return numerator.divide(denominator);
    }

    public final BigDecimal toBigDecimal() {
        return new BigDecimal(numerator).divide(new BigDecimal(denominator), RoundingMode.HALF_UP);
    }

    @Override
    public final int hashCode() {
        return toList(this).hashCode();
    }

    @Override
    public final boolean equals(final Object obj) {
        return this == obj || (obj instanceof BigRational && toList(this).equals(toList((BigRational) obj)));
    }

    @Override
    public final String toString() {
        return "(" + numerator + "/" + denominator + ")";
    }
}
