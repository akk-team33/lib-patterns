package de.team33.patterns.arbitrary.mimas.publics;

import de.team33.patterns.arbitrary.mimas.Generator;
import de.team33.patterns.arbitrary.mimas.sample.FixedGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigInteger;

import static de.team33.patterns.arbitrary.mimas.Generator.CHARACTERS;
import static de.team33.patterns.arbitrary.mimas.Generator.MAX_STRING_LENGTH;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("ClassWithTooManyMethods")
class FixedGeneratorTest {

    private final Generator generator = new FixedGenerator();

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 5, 8, 13, 30})
    final void anyBits(final int numBits) {
        final BigInteger bound = BigInteger.ONE.shiftLeft(numBits);
        final BigInteger result = generator.anyBits(numBits);
        assertFalse(BigInteger.ZERO.compareTo(result) > 0);
        assertTrue(bound.compareTo(result) > 0);
    }

    @Test
    final void anyBoolean() {
        final boolean expected = generator.anyBoolean();
        final boolean result = generator.anyBoolean();
        assertEquals(expected, result);
    }

    @Test
    final void anyByte() {
        final byte expected = generator.anyByte();
        final byte result = generator.anyByte();
        assertEquals(expected, result);
    }

    @Test
    final void anyShort() {
        final short expected = generator.anyShort();
        final short result = generator.anyShort();
        assertEquals(expected, result);
    }

    @Test
    final void anyInt() {
        final int expected = generator.anyInt();
        final int result = generator.anyInt();
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 5, 8, 13, 30})
    final void anyInt_bound(final int bound) {
        final int result = generator.anyInt(bound);
        assertFalse(0 > result);
        assertTrue(bound > result);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 5, 8, 13, 30})
    final void anyInt_min_bound(final int bound) {
        final int min = -bound;
        final int result = generator.anyInt(bound);
        assertFalse(min > result);
        assertTrue(bound > result);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 5, 8, 13, 30})
    final void anySmallInt(final int bound) {
        final int result = generator.anySmallInt(bound);
        assertFalse(0 > result);
        assertTrue(bound > result);
    }

    @Test
    final void anyLong() {
        final long expected = generator.anyLong();
        final long result = generator.anyLong();
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3, 5, 8, 13, 30})
    final void anyLong_bound(final long bound) {
        final long result = generator.anyLong(bound);
        assertFalse(0 > result);
        assertTrue(bound > result);
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3, 5, 8, 13, 30})
    final void anyLong_min_bound(final long bound) {
        final long min = -bound;
        final long result = generator.anyLong(min, bound);
        assertFalse(min > result);
        assertTrue(bound > result);
    }

    @Test
    final void anyFloat() {
        final float result = generator.anyFloat();
        assertFalse(0.0 > result);
        assertTrue(1.0 > result);
    }

    @Test
    final void anyDouble() {
        final double result = generator.anyDouble();
        assertFalse(0.0 > result);
        assertTrue(1.0 > result);
    }

    @Test
    final void anyBigInteger() {
        final BigInteger min = BigInteger.valueOf(Long.MIN_VALUE);
        final BigInteger max = BigInteger.valueOf(Long.MAX_VALUE);
        final BigInteger result = generator.anyBigInteger();
        assertFalse(min.compareTo(result) > 0);
        assertFalse(max.compareTo(result) < 0);
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3, 5, 8, 13, 30})
    final void anyBigInteger_bound(final long bound) {
        final BigInteger bigBound = BigInteger.valueOf(bound);
        final BigInteger result = generator.anyBigInteger(bigBound);
        assertFalse(BigInteger.ZERO.compareTo(result) > 0);
        assertTrue(bigBound.compareTo(result) > 0);
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3, 5, 8, 13, 30})
    final void anyBigInteger_min_bound(final long bound) {
        final BigInteger bigBound = BigInteger.valueOf(bound);
        final BigInteger bigMin = BigInteger.ZERO.subtract(bigBound);
        final BigInteger result = generator.anyBigInteger(bigMin, bigBound);
        assertFalse(bigMin.compareTo(result) > 0);
        assertTrue(bigBound.compareTo(result) > 0);
    }

    @Test
    final void anySmallBigInteger() {
        final BigInteger bigBound = BigInteger.valueOf(0x10000);
        final BigInteger result = generator.anySmallBigInteger();
        assertFalse(BigInteger.ZERO.compareTo(result) > 0);
        assertTrue(bigBound.compareTo(result) > 0);
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 20, 300, 5000, 80_000, 1_300_000, 30_000_000})
    final void anySmallBigInteger_bound(final long bound) {
        final BigInteger bigBound = BigInteger.valueOf(bound);
        final BigInteger result = generator.anySmallBigInteger(bigBound);
        assertFalse(BigInteger.ZERO.compareTo(result) > 0);
        assertTrue(bigBound.compareTo(result) > 0);
    }

    @Test
    final void anyChar() {
        final char result = generator.anyChar();
        assertFalse(0 > CHARACTERS.indexOf(result));
    }

    @Test
    final void anyChar_of() {
        final String characters = "abc-123";
        final char result = generator.anyChar(characters);
        assertFalse(0 > characters.indexOf(result));
    }

    @Test
    final void anyString() {
        final String result = generator.anyString();
        assertFalse(MAX_STRING_LENGTH < result.length());
        for (int index = 0; index < result.length(); ++index) {
            assertFalse(0 > CHARACTERS.indexOf(result.charAt(index)));
        }
    }

    @Test
    final void anyString_length() {
        final int length = 13;
        final String result = generator.anyString(length);
        assertEquals(length, result.length());
        for (int index = 0; index < result.length(); ++index) {
            assertFalse(0 > CHARACTERS.indexOf(result.charAt(index)));
        }
    }

    @Test
    final void anyString_range() {
        final String characters = "abc-123";
        final int min = 5;
        final int max = 13;
        final String result = generator.anyString(min, max, characters);
        assertFalse(min > result.length());
        assertFalse(max < result.length());
        for (int index = 0; index < result.length(); ++index) {
            assertFalse(0 > CHARACTERS.indexOf(result.charAt(index)));
        }
    }

    @Test
    final void anyString_of() {
        final String characters = "abc-123";
        final String result = generator.anyString(characters);
        assertFalse(MAX_STRING_LENGTH < result.length());
        for (int index = 0; index < result.length(); ++index) {
            assertFalse(0 > characters.indexOf(result.charAt(index)));
        }
    }

    @Test
    final void anyString_length_of() {
        final String characters = "abc-123";
        final int length = 17;
        final String result = generator.anyString(length, characters);
        assertEquals(length, result.length());
        for (int index = 0; index < result.length(); ++index) {
            assertFalse(0 > characters.indexOf(result.charAt(index)));
        }
    }

    @Test
    final void anyString_range_of() {
        final String characters = "abc-123";
        final int min = 3;
        final int max = 7;
        final String result = generator.anyString(min, max, characters);
        assertFalse(min > result.length());
        assertFalse(max < result.length());
        for (int index = 0; index < result.length(); ++index) {
            assertFalse(0 > characters.indexOf(result.charAt(index)));
        }
    }

    @Test
    void anyOf() {
    }

    @Test
    void testAnyOf() {
    }

    @Test
    void anyDataSet() {
    }

    @Test
    void anyNullable() {
    }

    @Test
    void stream() {
    }
}