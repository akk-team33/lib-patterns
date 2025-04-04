package de.team33.patterns.arbitrary.mimas.publics;

import de.team33.patterns.arbitrary.mimas.Bridge;
import de.team33.patterns.arbitrary.mimas.Generator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.EnumSet;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GeneratorTest {

    @Test
    final void simple() {
        final Generator generator = Generator.of(new SecureRandom());
        assertInstanceOf(Generator.class, generator);
        assertInstanceOf(Boolean.class, generator.anyBoolean());
        assertInstanceOf(Byte.class, generator.anyByte());
        // etc. ...
    }

    @ParameterizedTest
    @EnumSource
    final void anyBits(final Case testCase) {
        final int numBits = Case.RANDOM.generator.anyInt(256);
        final BigInteger bound = ONE.shiftLeft(numBits);

        final BigInteger result = testCase.generator.anyBits(numBits);

        assertTrue(ZERO.compareTo(result) <= 0,
                   () -> "<result> is expected to be greater or equal to ZERO but was " + result);
        assertTrue(bound.compareTo(result) > 0,
                   () -> "<result> is expected to be less than " + bound + " but was " + result);
    }

    @ParameterizedTest
    @EnumSource
    final void anyBoolean(final Case testCase) {
        final boolean result = testCase.generator.anyBoolean();
        assertTrue(testCase.isExpected(result));
    }

    @ParameterizedTest
    @EnumSource
    final void anyByte(final Case testCase) {
        final byte result = testCase.generator.anyByte();
        assertTrue(testCase.isExpected(result));
    }

    @ParameterizedTest
    @EnumSource
    final void anyShort(final Case testCase) {
        final short result = testCase.generator.anyShort();
        assertTrue(testCase.isExpected(result));
    }

    @ParameterizedTest
    @EnumSource
    final void anyInt(final Case testCase) {
        final int result = testCase.generator.anyInt();
        assertTrue(testCase.isExpected(result));
    }

    @ParameterizedTest
    @EnumSource
    final void anyInt_bound(final Case testCase) {
        final int bound = 1 + Case.SECURE_RANDOM.generator.anyInt(256);

        final int result = testCase.generator.anyInt(bound);

        assertTrue(0 <= result,
                   () -> "result is expected to be greater or equal to 0 but was " + result);
        assertTrue(bound > result,
                   () -> "result is expected to be less than " + bound + " but was " + result);
    }

    @ParameterizedTest
    @EnumSource
    final void anyInt_bound_edge(final Case testCase) {
        assertEquals(0, testCase.generator.anyInt(1));
        assertThrows(IllegalArgumentException.class, () -> testCase.generator.anyInt(0));
        assertThrows(IllegalArgumentException.class, () -> testCase.generator.anyInt(-5));
    }

    @ParameterizedTest
    @EnumSource
    final void anyInt_min_bound(final Case testCase) {
        final int min = Case.SECURE_RANDOM.generator.anyInt(-256, 256);
        final int bound = min + 1 + Case.SECURE_RANDOM.generator.anyInt(256);

        final int result = testCase.generator.anyInt(min, bound);

        assertTrue(min <= result,
                   () -> "result is expected to be greater or equal to " + min + " but was " + result);
        assertTrue(bound > result,
                   () -> "result is expected to be less than " + bound + " but was " + result);
    }

    @ParameterizedTest
    @EnumSource
    final void anySmallInt(final Case testCase) {
        final int bound = 1 + Case.RANDOM.generator.anyInt(256);

        final int result = testCase.generator.anySmallInt(bound);

        assertTrue(0 <= result,
                   () -> "result is expected to be greater or equal to 0 but was " + result);
        assertTrue(bound > result,
                   () -> "result is expected to be less than " + bound + " but was " + result);
    }

    @ParameterizedTest
    @EnumSource
    final void anyLong(final Case testCase) {
        final long result = testCase.generator.anyLong();
        assertTrue(testCase.isExpected(result));
    }

    @ParameterizedTest
    @EnumSource
    final void anyLong_bound(final Case testCase) {
        final long bound = 1L + Case.SECURE_RANDOM.generator.anyLong(Integer.MAX_VALUE);

        final long result = testCase.generator.anyLong(bound);

        assertTrue(0 <= result,
                   () -> "result is expected to be greater or equal to 0 but was " + result);
        assertTrue(bound > result,
                   () -> "result is expected to be less than " + bound + " but was " + result);
    }

    @ParameterizedTest
    @EnumSource
    final void anyLong_bound_edge(final Case testCase) {
        assertEquals(0L, testCase.generator.anyLong(1L));
        assertThrows(IllegalArgumentException.class, () -> testCase.generator.anyLong(0L));
        assertThrows(IllegalArgumentException.class, () -> testCase.generator.anyLong(-5L));
    }

    @ParameterizedTest
    @EnumSource
    final void anyLong_min_bound(final Case testCase) {
        final long min = Case.SECURE_RANDOM.generator.anyLong(Integer.MIN_VALUE, Integer.MAX_VALUE);
        final long bound = min + 1 + Case.SECURE_RANDOM.generator.anyLong(Integer.MAX_VALUE);

        final long result = testCase.generator.anyLong(min, bound);

        assertTrue(min <= result,
                   () -> "result is expected to be greater or equal to " + min + " but was " + result);
        assertTrue(bound > result,
                   () -> "result is expected to be less than " + bound + " but was " + result);
    }

    @ParameterizedTest
    @EnumSource
    final void anyChar(final Case testCase) {
        final char result = testCase.generator.anyChar();
        final int index = Bridge.STD_CHARACTERS.indexOf(result);
        assertFalse(0 > index);
    }

    @ParameterizedTest
    @EnumSource
    final void anyChar_characters(final Case testCase) {
        final char result = testCase.generator.anyChar("0123456789");
        assertTrue('0' <= result,
                   () -> "result is expected to be greater or equal to '0' but was '" + result + "'");
        assertTrue('9' >= result,
                   () -> "result is expected to be less or equal to '9' but was '" + result + "'");
    }

    @ParameterizedTest
    @EnumSource
    final void anyString(final Case testCase) {
        final int length = Case.SECURE_RANDOM.generator.anyInt(128);

        final String result = testCase.generator.anyString(length, "0123456789");

        assertEquals(length, result.length());
        for (final char c : result.toCharArray()) {
            assertTrue('0' <= c,
                       () -> "result(c) is expected to be greater or equal to '0' but was '" + result + "'");
            assertTrue('9' >= c,
                       () -> "result(c) is expected to be less or equal to '9' but was '" + result + "'");
        }
    }

    @ParameterizedTest
    @EnumSource
    final void anyString_default(final Case testCase) {
        final String result = testCase.generator.anyString();

        assertFalse(result.isEmpty());
        assertTrue(128 >= result.length());

        for (final char c : result.toCharArray()) {
            assertTrue(0 <= Bridge.STD_CHARACTERS.indexOf(c));
        }
    }

    @ParameterizedTest
    @EnumSource
    final void anyString_edge(final Case testCase) {
        assertEquals("", testCase.generator.anyString(0, "0123456789"));
        assertEquals("00000", testCase.generator.anyString(5, "0"));
        assertThrows(IllegalArgumentException.class, () -> testCase.generator.anyString(-1, "0123456789"));
        assertThrows(IllegalArgumentException.class, () -> testCase.generator.anyString(1, ""));
        assertThrows(IllegalArgumentException.class, () -> testCase.generator.anyString(0, ""));
    }

    @ParameterizedTest
    @EnumSource
    final void anyFloat(final Case testCase) {
        final float result = testCase.generator.anyFloat();

        assertTrue(0.0 <= result,
                   () -> "result is expected to be greater or equal to 0.0 but was " + result);
        assertTrue(1.0 > result,
                   () -> "result is expected to be less than 1.0 but was " + result);
    }

    @ParameterizedTest
    @EnumSource
    final void anyDouble(final Case testCase) {
        final double result = testCase.generator.anyDouble();

        assertTrue(0.0 <= result,
                   () -> "result is expected to be greater or equal to 0.0 but was " + result);
        assertTrue(1.0 > result,
                   () -> "result is expected to be less than 1.0 but was " + result);
    }

    @ParameterizedTest
    @EnumSource
    final void anyBigInteger(final Case testCase) {
        final BigInteger result = testCase.generator.anyBigInteger();

        assertTrue(BigInteger.valueOf(Long.MIN_VALUE).compareTo(result) <= 0);
        assertTrue(BigInteger.valueOf(Long.MAX_VALUE).compareTo(result) >= 0);
    }

    @ParameterizedTest
    @EnumSource
    final void anyBigInteger_bound(final Case testCase) {
        final BigInteger bound = Case.SECURE_RANDOM.generator.anySmallBigInteger().add(ONE);

        final BigInteger result = testCase.generator.anyBigInteger(bound);

        assertTrue(ZERO.compareTo(result) <= 0,
                   () -> "result is expected to be greater or equal to ZERO but was " + result);
        assertTrue(bound.compareTo(result) > 0,
                   () -> "result is expected to be less than " + bound + " but was " + result);
    }

    @ParameterizedTest
    @EnumSource
    final void anyBigInteger_min_bound(final Case testCase) {
        final long min = Case.SECURE_RANDOM.generator.anyLong(Integer.MIN_VALUE, Integer.MAX_VALUE);
        final long bound = min + 1 + Case.SECURE_RANDOM.generator.anyLong(Integer.MAX_VALUE);
        final BigInteger bigMin = BigInteger.valueOf(min);
        final BigInteger bigBound = BigInteger.valueOf(bound);

        final BigInteger result = testCase.generator.anyBigInteger(bigMin, bigBound);

        assertTrue(bigMin.compareTo(result) <= 0,
                   () -> "result is expected to be greater or equal to " + bigMin + " but was " + result);
        assertTrue(bigBound.compareTo(result) > 0,
                   () -> "result is expected to be less than " + bigBound + " but was " + result);
    }

    @ParameterizedTest
    @EnumSource
    final void anySmallBigInteger(final Case testCase) {
        final BigInteger bound = ONE.shiftLeft(16);

        final BigInteger result = testCase.generator.anySmallBigInteger();

        assertTrue(ZERO.compareTo(result) <= 0,
                   () -> "result is expected to be greater or equal to ZERO but was " + result);
        assertTrue(bound.compareTo(result) > 0,
                   () -> "result is expected to be less than " + bound + " but was " + result);
    }

    @ParameterizedTest
    @EnumSource
    final void anySmallBigInteger_bound(final Case testCase) {
        final BigInteger bound = Case.RANDOM.generator.anyBigInteger(ONE.shiftLeft(16));

        final BigInteger result = testCase.generator.anySmallBigInteger(bound);

        assertTrue(ZERO.compareTo(result) <= 0,
                   () -> "result is expected to be greater or equal to ZERO but was " + result);
        assertTrue(bound.compareTo(result) > 0,
                   () -> "result is expected to be less than " + bound + " but was " + result);
    }

    @ParameterizedTest
    @EnumSource
    final void anyOf(final Case testCase) {
        final RoundingMode result = testCase.generator.anyOf(RoundingMode.values());
        assertTrue(EnumSet.allOf(RoundingMode.class).contains(result));
    }

    @ParameterizedTest
    @EnumSource
    final void anyOf_enum(final Case testCase) {
        final RoundingMode result = testCase.generator.anyOf(RoundingMode.class);
        assertTrue(EnumSet.allOf(RoundingMode.class).contains(result));
    }

    @SuppressWarnings({"unused", "PackageVisibleField"})
    enum Case {

        FIXED_MIN(numBits -> ZERO,
                  false, 0, 0, 0, 0),

        FIXED_MEAN(numBits -> ONE.shiftLeft(numBits / 2).subtract(ONE),
                   false, 0x0f, 0x00ff, 0xffff, 0xffffffffL),

        FIXED_MAX(numBits -> ONE.shiftLeft(numBits).subtract(ONE),
                  true, -1, -1, -1, -1),

        RANDOM(Generator.of(new SecureRandom())),

        SECURE_RANDOM(Generator.of(new SecureRandom()));

        final Generator generator;
        final Boolean expBoolean;
        final Byte expByte;
        final Short expShort;
        final Integer expInt;
        final Long expLong;

        Case(final Generator generator) {
            this(generator, null, null, null, null, null);
        }

        @SuppressWarnings({"ConstructorWithTooManyParameters", "NumericCastThatLosesPrecision"})
        Case(final Generator generator,
             final boolean expBoolean, final int expByte, final int expShort, final int expInt, final long expLong) {
            this(generator,
                 Boolean.valueOf(expBoolean), Byte.valueOf((byte) expByte), Short.valueOf((short) expShort),
                 Integer.valueOf(expInt), Long.valueOf(expLong));
        }

        Case(final Generator generator, final Boolean expBoolean, final Byte expByte, final Short expShort,
             final Integer expInt, final Long expLong) {
            this.generator = generator;
            this.expBoolean = expBoolean;
            this.expByte = expByte;
            this.expShort = expShort;
            this.expInt = expInt;
            this.expLong = expLong;
        }

        final boolean isExpected(final boolean result) {
            return (null == expBoolean) || expBoolean.equals(result);
        }

        final boolean isExpected(final byte result) {
            return (null == expByte) || expByte.equals(result);
        }

        final boolean isExpected(final short result) {
            return (null == expShort) || expShort.equals(result);
        }

        final boolean isExpected(final int result) {
            return (null == expInt) || expInt.equals(result);
        }

        final boolean isExpected(final long result) {
            return (null == expLong) || expLong.equals(result);
        }
    }
}
