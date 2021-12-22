package de.team33.test.patterns.math.e1;

import de.team33.patterns.math.e1.BigRational;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BigRationalTest {

    private static final Random RANDOM = new Random();

    private static BigInteger anyBigInteger() {
        return new BigInteger(RANDOM.nextInt(128) + 1, RANDOM);
    }

    @Test
    final void valueOf_numerator_denominator() {
        assertThrows(IllegalArgumentException.class, () -> BigRational.valueOf(anyBigInteger(), BigInteger.ZERO));

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
    final void multiply() {
        final long[] values = {RANDOM.nextLong(), RANDOM.nextLong()};
        final BigRational result = BigRational.valueOf(values[0], values[1])
                                              .multiply(BigRational.valueOf(values[1], values[0]));
        assertEquals(BigInteger.ONE, result.getNumerator());
        assertEquals(BigInteger.ONE, result.getDenominator());
    }

    @Test
    final void divide() {
        final BigRational expected = BigRational.valueOf(BigInteger.valueOf(2184), BigInteger.valueOf(110));
        final BigRational result = BigRational.valueOf(111384).divide(BigRational.valueOf(5610));
        assertEquals(expected, result);
        assertEquals(1092, result.getNumerator().intValue());
        assertEquals(55, result.getDenominator().intValue());
    }

    @Test
    final void toBigDecimal() {
        //assertEquals(BigDecimal.valueOf(1.0/3.0), BigRational.valueOf(1, 3).toBigDecimal());
        //assertEquals(BigDecimal.valueOf(2.0/3.0), BigRational.valueOf(2, 3).toBigDecimal());
        //assertEquals(BigDecimal.valueOf(2.0/7.0), BigRational.valueOf(2, 7).toBigDecimal());
        assertEquals(BigDecimal.valueOf(5.0/7.0), BigRational.valueOf(5, 7).toBigDecimal());
    }
}
