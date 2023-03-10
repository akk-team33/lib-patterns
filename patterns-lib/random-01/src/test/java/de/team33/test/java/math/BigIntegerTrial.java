package de.team33.test.java.math;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BigIntegerTrial {

    private static final byte[] BYTE_FFFFFFFFFFFFFF = {-1, -1, -1, -1, -1, -1, -1};
    private static final byte[] BYTE_FFFFFF = {-1, -1, -1};
    private static final BigInteger BIG_FFFFFFFFFFFFFF = new BigInteger(BYTE_FFFFFFFFFFFFFF);
    private static final BigInteger BIG_FFFFFFFFFFFFFF_2 = BigInteger.valueOf(0xFFFFFFFFFFFFFFL);
    private static final BigInteger BIG_FFFFFF = new BigInteger(BYTE_FFFFFF);
    private static final BigInteger BIG_FFFFFF_2 = BigInteger.valueOf(0xFFFFFF);

    @Test
    final void BIG_FFFFFFFFFFFFFF_isNegative() {
        assertTrue(BigInteger.ZERO.compareTo(BIG_FFFFFFFFFFFFFF) > 0);
        assertTrue(BigInteger.ZERO.compareTo(BIG_FFFFFF) > 0);
        assertEquals(BigInteger.valueOf(-1), BIG_FFFFFFFFFFFFFF);
        assertEquals(BigInteger.valueOf(-1), BIG_FFFFFF);
        assertEquals(BIG_FFFFFFFFFFFFFF, BIG_FFFFFF);
        assertEquals(1, BIG_FFFFFFFFFFFFFF.toByteArray().length);
    }

    @Test
    final void BIG_FFFFFFFFFFFFFF_2_isPositive() {
        assertTrue(BigInteger.ZERO.compareTo(BIG_FFFFFFFFFFFFFF_2) < 0);
        assertTrue(BigInteger.ZERO.compareTo(BIG_FFFFFF_2) < 0);
        assertEquals(8, BIG_FFFFFFFFFFFFFF_2.toByteArray().length);
        assertEquals(4, BIG_FFFFFF_2.toByteArray().length);
    }

    @Test
    final void BIG_FFFFFFFFFFFFFF_and_FFFFFF_isFFFFFF() {
        assertEquals(BIG_FFFFFF, BIG_FFFFFFFFFFFFFF.and(BIG_FFFFFF));
    }

    @Test
    final void BIG_FFFFFFFFFFFFFF_2_and_FFFFFF_isFFFFFF_2() {
        assertEquals(BIG_FFFFFFFFFFFFFF_2, BIG_FFFFFFFFFFFFFF_2.and(BIG_FFFFFF));
    }

    @Test
    final void BIG_FFFFFFFFFFFFFF_and_FFFFFF_2_isFFFFFF_2() {
        assertEquals(BIG_FFFFFF_2, BIG_FFFFFFFFFFFFFF.and(BIG_FFFFFF_2));
    }

    @Test
    final void shift() {
        final BigInteger shifted = BigInteger.ONE.shiftLeft(56).subtract(BigInteger.ONE);
        assertEquals(BIG_FFFFFFFFFFFFFF_2, shifted);
        assertEquals(BIG_FFFFFFFFFFFFFF_2, BIG_FFFFFFFFFFFFFF.and(shifted));
        assertEquals(BIG_FFFFFFFFFFFFFF_2, shifted.and(BIG_FFFFFFFFFFFFFF));
    }
}
