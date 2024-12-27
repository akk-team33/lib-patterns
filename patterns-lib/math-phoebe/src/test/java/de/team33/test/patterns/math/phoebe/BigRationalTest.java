package de.team33.test.patterns.math.phoebe;

import de.team33.patterns.exceptional.dione.Ignoring;
import de.team33.patterns.math.phoebe.BigRational;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

class BigRationalTest {

    private static final Random RANDOM = new Random();

    private static BigInteger anyBigInteger() {
        return new BigInteger(RANDOM.nextInt(128) + 1, RANDOM);
    }

    @Test
    final void valueOf_numerator_denominator() {
        assertThrows(ArithmeticException.class, () -> BigRational.valueOf(anyBigInteger(), BigInteger.ZERO));

        final BigInteger expected = anyBigInteger().subtract(anyBigInteger());
        final BigInteger denominator = anyBigInteger().subtract(anyBigInteger());
        final BigInteger numerator = expected.multiply(denominator);
        final BigRational result = BigRational.valueOf(numerator, denominator);
        assertEquals(expected, result.getNumerator());
        assertEquals(BigInteger.ONE, result.getDenominator());
    }

    @ParameterizedTest
    @ValueSource(doubles = {1.0, -1.0, 100.0, -100.0, 0.0001, -0.0001, 1.0E99, -1.0E99, 1.0E-99, -1.0E-99, 0.0})
    final void valueOf_double(final double value) {
        final double expected = value * RANDOM.nextDouble() / 3.0;
        final BigRational result = BigRational.valueOf(expected);
        assertEquals(expected, result.doubleValue());
    }

    @Test
    final void valueOf_long() {
        final long expected = RANDOM.nextLong();
        final BigRational result = BigRational.valueOf(expected);
        assertEquals(expected, result.longValue());
    }

    @Test
    final void valueOf_BigInteger() {
        final BigInteger expected = BigInteger.valueOf(RANDOM.nextLong())
                                              .multiply(BigInteger.valueOf(RANDOM.nextLong()));
        final BigRational result = BigRational.valueOf(expected);
        assertEquals(expected, result.toBigInteger());
    }

    @Test
    //@Disabled
    final void toBigDecimal() {
        assertEquals(BigDecimal.valueOf(1.0/3.0), BigRational.valueOf(1, 3).toBigDecimal());
        //assertEquals(BigDecimal.valueOf(2.0/3.0), BigRational.valueOf(2, 3).toBigDecimal());
        assertEquals(BigDecimal.valueOf(2.0/7.0), BigRational.valueOf(2, 7).toBigDecimal());
        assertEquals(BigDecimal.valueOf(5.0/7.0), BigRational.valueOf(5, 7).toBigDecimal());
    }

    @Test
    final void inverse_positive() {
        final BigRational expected = BigRational.valueOf(3, 7);
        final BigRational result = BigRational.valueOf(7, 3).inverse();
        assertEquals(expected, result);
    }

    @Test
    final void inverse_negative() {
        final BigRational expected = BigRational.valueOf(-3, 7);
        final BigRational result = BigRational.valueOf(-7, 3).inverse();
        assertEquals(expected, result);
    }

    @Test
    final void inverse_zero() {
        final BigRational origin = BigRational.valueOf(0, 3);
        try {
            final BigRational result = origin.inverse();
            fail("expected to fail - but was " + result);
        } catch (final ArithmeticException e) {
            // as expected
            // e.printStackTrace();
        }
    }

    @ParameterizedTest
    @EnumSource
    final void add(final Case given) {
        final BigRational result = given.origin.add(given.addend);
        assertEquals(given.expectedSum, result);
    }

    @ParameterizedTest
    @EnumSource
    final void subtract(final Case given) {
        final BigRational result = given.origin.subtract(given.subtrahend);
        assertEquals(given.expectedSum, result);
    }

    @ParameterizedTest
    @EnumSource
    final void multiply(final Case given) {
        final BigRational result = given.origin.multiply(given.multiplier);
        assertEquals(given.expectedProduct, result);
    }

    @ParameterizedTest
    @EnumSource
    final void divide(final Case given) {
        if (null != given.divisor) {
            final BigRational result = given.origin.divide(given.divisor);
            assertEquals(given.expectedProduct, result);
        }
    }

    @Test
    final void divide_zero() {
        final BigRational origin = BigRational.valueOf(278, 27);
        try {
            final BigRational result = origin.divide(BigRational.ZERO);
            fail("expected to fail - but was " + result);
        } catch (final ArithmeticException e) {
            // as expected
            // e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    enum Case {
        CASE_z_p12_p8_p27(0, 12, 8, 27),
        CASE_p13_p12_z_p27(13, 12, 0, 27),
        CASE_z_p12_z_p27(0, 12, 0, 27),
        CASE_p13_p12_p8_p27(13, 12, 8, 27),
        CASE_p13_p12_p8_n27(13, 12, 8, -27),
        CASE_p13_p12_n8_p27(13, 12, -8, 27),
        CASE_p13_p12_n8_n27(13, 12, -8, -27),
        CASE_p13_n12_p8_p27(13, -12, 8, 27),
        CASE_p13_n12_n8_p27(13, -12, -8, 27),
        CASE_p13_n12_n8_n27(13, -12, -8, -27),
        CASE_n13_p12_p8_p27(-13, 12, 8, 27),
        CASE_n13_n12_p8_p27(-13, -12, 8, 27),
        CASE_n13_n12_n8_p27(-13, -12, -8, 27),
        CASE_n13_n12_n8_n27(-13, -12, -8, -27);

        private final BigRational origin;
        private final BigRational multiplier;
        private final BigRational divisor;
        private final BigRational addend;
        private final BigRational subtrahend;
        private final BigRational expectedSum;
        private final BigRational expectedProduct;

        Case(final long o_num, final long o_den, final long a_num, final long a_den) {
            this.origin = BigRational.valueOf(o_num, o_den);
            this.addend = BigRational.valueOf(a_num, a_den);
            this.subtrahend = BigRational.valueOf(-a_num, a_den);
            this.multiplier = BigRational.valueOf(a_num, a_den);
            this.divisor = Ignoring.any(ArithmeticException.class)
                                   .get(() -> BigRational.valueOf(a_den, a_num))
                                   .orElse(null);
            this.expectedSum = BigRational.valueOf((o_num * a_den) + (a_num * o_den), o_den * a_den);
            this.expectedProduct = BigRational.valueOf(o_num * a_num, o_den * a_den);
        }
    }
}
