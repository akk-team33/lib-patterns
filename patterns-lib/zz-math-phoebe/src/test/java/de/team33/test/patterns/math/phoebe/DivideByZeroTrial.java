package de.team33.test.patterns.math.phoebe;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@SuppressWarnings({"NumericOverflow", "divzero"})
public class DivideByZeroTrial {

    @Test
    final void divideIntByZero() {
        try {
            final int x = 278 / 0;
            fail("expected to fail - but was " + x);
        } catch (final ArithmeticException e) {
            // as expected
            // e.printStackTrace();
        }
    }

    @Test
    final void divideLongByZero() {
        try {
            final long x = 278L / 0L;
            fail("expected to fail - but was " + x);
        } catch (final ArithmeticException e) {
            // as expected
            // e.printStackTrace();
        }
    }

    @Test
    final void divideDoubleByZero() {
        final double x = 278.0 / 0.0;
        assertEquals(Double.POSITIVE_INFINITY, x);
    }

    @Test
    final void divideBigIntegerByZero() {
        try {
            final BigInteger x = BigInteger.valueOf(278).divide(BigInteger.ZERO);
            fail("expected to fail - but was " + x);
        } catch (final ArithmeticException e) {
            // as expected
            // e.printStackTrace();    
        }
    }

    @Test
    final void divideBigDecimalByZero() {
        try {
            final BigDecimal x = BigDecimal.valueOf(278).divide(BigDecimal.ZERO);
            fail("expected to fail - but was " + x);
        } catch (final ArithmeticException e) {
            // as expected
            // e.printStackTrace();
        }
    }
}
