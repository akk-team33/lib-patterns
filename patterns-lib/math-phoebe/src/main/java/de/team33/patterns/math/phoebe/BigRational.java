package de.team33.patterns.math.phoebe;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a rational number whose size and precision are in principle not limited at all
 * or practically only by the available memory.
 */
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

    /**
     * Returns a {@link BigRational} value based on the numerator and denominator,
     * both specified as {@link BigInteger} values.
     * The denominator must not be (equal to) {@link BigInteger#ZERO}.
     *
     * @throws ArithmeticException if the denominator is (equal to) {@link BigInteger#ZERO}.
     * @see #valueOf(long, BigInteger)
     * @see #valueOf(BigInteger, long)
     * @see #valueOf(long, long)
     * @see #valueOf(BigInteger)
     * @see #valueOf(long)
     * @see #valueOf(double)
     */
    public static BigRational valueOf(final BigInteger numerator, final BigInteger denominator) {
        switch (denominator.signum()) {
        case 1:
            return new BigRational(numerator, denominator);
        case -1:
            return new BigRational(numerator.negate(), denominator.negate());
        default:
            throw new ArithmeticException("BigRational: division by zero");
        }
    }

    /**
     * Results in a {@link BigRational} value based on a {@link BigInteger} numerator and a {@code long} denominator.
     * The denominator must not be zero.
     *
     * @throws ArithmeticException When the denominator is zero.
     * @see #valueOf(BigInteger, BigInteger)
     * @see #valueOf(long, BigInteger)
     * @see #valueOf(long, long)
     * @see #valueOf(BigInteger)
     * @see #valueOf(long)
     * @see #valueOf(double)
     */
    public static BigRational valueOf(final BigInteger numerator, final long denominator) {
        return valueOf(numerator, BigInteger.valueOf(denominator));
    }

    /**
     * Results in a {@link BigRational} value based on a {@code long} numerator and a {@link BigInteger} denominator.
     * The denominator must not be zero.
     *
     * @throws ArithmeticException When the denominator is zero.
     * @see #valueOf(BigInteger, BigInteger)
     * @see #valueOf(BigInteger, long)
     * @see #valueOf(long, long)
     * @see #valueOf(BigInteger)
     * @see #valueOf(long)
     * @see #valueOf(double)
     */
    public static BigRational valueOf(final long numerator, final BigInteger denominator) {
        return valueOf(BigInteger.valueOf(numerator), denominator);
    }

    /**
     * Results in a {@link BigRational} value based on an integer numerator and denominator that are given as
     * {@code long} values. The denominator must not be zero.
     *
     * @throws ArithmeticException When the denominator is zero.
     * @see #valueOf(BigInteger, BigInteger)
     * @see #valueOf(long, BigInteger)
     * @see #valueOf(BigInteger, long)
     * @see #valueOf(BigInteger)
     * @see #valueOf(long)
     * @see #valueOf(double)
     */
    public static BigRational valueOf(final long numerator, final long denominator) {
        return valueOf(BigInteger.valueOf(numerator), BigInteger.valueOf(denominator));
    }

    /**
     * Results in a {@link BigRational} value based on a {@link BigInteger} value.
     *
     * @see #valueOf(BigInteger, BigInteger)
     * @see #valueOf(long, BigInteger)
     * @see #valueOf(BigInteger, long)
     * @see #valueOf(long, long)
     * @see #valueOf(long)
     * @see #valueOf(double)
     */
    public static BigRational valueOf(final BigInteger integer) {
        return valueOf(integer, BigInteger.ONE);
    }

    /**
     * Results in a {@link BigRational} value based on a {@code long} value.
     *
     * @see #valueOf(BigInteger, BigInteger)
     * @see #valueOf(long, BigInteger)
     * @see #valueOf(BigInteger, long)
     * @see #valueOf(long, long)
     * @see #valueOf(BigInteger)
     * @see #valueOf(double)
     */
    public static BigRational valueOf(final long integer) {
        return valueOf(BigInteger.valueOf(integer));
    }

    /**
     * Results in a {@link BigRational} value based on a {@code double} value.
     *
     * @see #valueOf(BigInteger, BigInteger)
     * @see #valueOf(long, BigInteger)
     * @see #valueOf(BigInteger, long)
     * @see #valueOf(long, long)
     * @see #valueOf(BigInteger)
     * @see #valueOf(long)
     */
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

    /**
     * This gives the negative, more precisely the additive inverse of this value.
     */
    public final BigRational negative() {
        return new BigRational(numerator.negate(), denominator);
    }

    /**
     * Returns the inverse, more precisely multiplicative inverse of this value.
     */
    public final BigRational inverse() {
        return valueOf(denominator, numerator);
    }

    /**
     * Returns the sum of this and the other given value.
     */
    public final BigRational add(final BigRational addend) {
        return new BigRational(numerator.multiply(addend.denominator)
                                        .add(addend.numerator.multiply(denominator)),
                               denominator.multiply(addend.denominator));
    }

    /**
     * Returns the difference between this and the other given value.
     */
    public final BigRational subtract(final BigRational subtrahend) {
        return add(subtrahend.negative());
    }

    /**
     * Returns the product of this and the other given value.
     */
    public final BigRational multiply(final BigRational multiplier) {
        return new BigRational(numerator.multiply(multiplier.numerator),
                               denominator.multiply(multiplier.denominator));
    }

    /**
     * Returns the quotient of this and the other given value.
     */
    public final BigRational divide(final BigRational divisor) {
        return multiply(divisor.inverse());
    }

    /**
     * Returns the numerator of this value.
     */
    public final BigInteger getNumerator() {
        return numerator;
    }

    /**
     * Returns the denominator of this value.
     */
    public final BigInteger getDenominator() {
        return denominator;
    }

    /**
     * Returns an (roughly) equivalent {@code double} value.
     */
    @Override
    public final double doubleValue() {
        return numerator.doubleValue() / denominator.doubleValue();
    }

    /**
     * Returns an (roughly) equivalent {@code float} value.
     */
    @Override
    public final float floatValue() {
        return numerator.floatValue() / denominator.floatValue();
    }

    /**
     * Returns an (roughly) equivalent {@code long} value.
     */
    @Override
    public final long longValue() {
        return toBigInteger().longValue();
    }

    /**
     * Returns an (roughly) equivalent {@code int} value.
     */
    @Override
    public final int intValue() {
        return toBigInteger().intValue();
    }

    /**
     * Returns an (roughly) equivalent {@link BigInteger} value.
     */
    public final BigInteger toBigInteger() {
        return numerator.divide(denominator);
    }

    /**
     * Returns an (roughly) equivalent {@link BigDecimal} value.
     */
    public final BigDecimal toBigDecimal() {
        return new BigDecimal(numerator).divide(new BigDecimal(denominator), 16, RoundingMode.HALF_UP);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int hashCode() {
        return toList(this).hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean equals(final Object obj) {
        return (this == obj) || ((obj instanceof BigRational) && toList(this).equals(toList((BigRational) obj)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        return BigInteger.ONE.equals(denominator) ? numerator.toString() : "(" + numerator + "/" + denominator + ")";
    }
}
