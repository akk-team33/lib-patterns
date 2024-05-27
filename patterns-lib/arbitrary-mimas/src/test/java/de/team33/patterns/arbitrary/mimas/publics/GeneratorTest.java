package de.team33.patterns.arbitrary.mimas.publics;

import de.team33.patterns.arbitrary.mimas.Bridge;
import de.team33.patterns.arbitrary.mimas.Generator;
import de.team33.patterns.arbitrary.mimas.sample.Producer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.EnumMap;
import java.util.IntSummaryStatistics;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class GeneratorTest {

    private static final BigInteger FIXED = new BigInteger("45619B2F8D02DD0BD545B8C8BD2CDBF86E8149DD7581925275C0", 16);

    private final Producer variable = new Producer();
    private final Generator fixed = numBits -> FIXED.and(ONE.shiftLeft(numBits).subtract(ONE));

    @Test
    final void simple() {
        final Generator simple = Generator.simple();
        assertInstanceOf(Generator.class, simple);
        assertInstanceOf(Boolean.class, simple.anyBoolean());
        assertInstanceOf(Byte.class, simple.anyByte());
        // etc. ...
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 5, 7, 11, 17, 19, 29, 97, 197, 1997})
    final void anyBits(final int numBits) {
        final BigInteger result = variable.anyBits(numBits);
        assertTrue(BigInteger.ZERO.compareTo(result) <= 0,
                   () -> "result is expected to be greater or equal to ZERO but was " + result);

        final BigInteger limit = ONE.shiftLeft(numBits);
        assertTrue(limit.compareTo(result) > 0,
                   () -> "result is expected to be less than " + limit + " but was " + result);
    }

    @Test
    final void anyBoolean() {
        assertInstanceOf(Boolean.class, variable.anyBoolean());
        assertFalse(fixed.anyBoolean());
    }

    @Test
    final void anyByte() {
        assertInstanceOf(Byte.class, variable.anyByte());
        assertEquals(BigInteger.valueOf(0xC0).byteValue(), fixed.anyByte());
    }

    @Test
    final void anyShort() {
        assertInstanceOf(Short.class, variable.anyShort());
        assertEquals(BigInteger.valueOf(0x75C0).shortValue(), fixed.anyShort());
    }

    @ParameterizedTest
    @EnumSource
    final void anySmallInt(final Case testCase) {
        final int bound = Case.RANDOM.generator.anyInt(256);
        final int result = testCase.generator.anySmallInt(bound);
        assertTrue(0 <= result);
        assertTrue(bound > result);
    }

    @Test
    final void anyInt() {
        assertInstanceOf(Integer.class, variable.anyInt());
        assertEquals(0x925275C0, fixed.anyInt());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 5, 7, 11, 17, 19, 29, 97})
    final void anyInt_bound(final int bound) {
        final int result = variable.anyInt(bound);
        assertTrue(0 <= result,
                   () -> "result is expected to be greater or equal to ZERO but was " + result);
        assertTrue(bound > result,
                   () -> "result is expected to be less than " + bound + " but was " + result);
        final int mask = IntStream.of(1, 2, 4, 8, 16, 32, 64, 128)
                                  .filter(i -> i > bound)
                                  .findFirst()
                                  .orElse(256) - 1;
        assertEquals(0x925275C0 & mask, fixed.anyInt(bound));
    }

    @ParameterizedTest
    @ValueSource(ints = {-3, -2, -1, 0, 1, 2, 5, 11, 19, 97})
    final void anyInt_min_bound(final int bound) {
        final int result = variable.anyInt(-5, bound);
        assertTrue(-5 <= result,
                   () -> "result is expected to be greater or equal to -5 but was " + result);
        assertTrue(bound > result,
                   () -> "result is expected to be less than " + bound + " but was " + result);
    }

    @Test
    final void anyLong() {
        assertInstanceOf(Long.class, variable.anyLong());
        assertEquals(0x49DD7581925275C0L, fixed.anyLong());
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3, 5, 7, 11, 17, 19, 29, 97})
    final void anyLong_bound(final long bound) {
        final long result = variable.anyLong(bound);
        assertTrue(0 <= result,
                   () -> "result is expected to be greater or equal to ZERO but was " + result);
        assertTrue(bound > result,
                   () -> "result is expected to be less than " + bound + " but was " + result);
    }

    @ParameterizedTest
    @ValueSource(longs = {0, -1, -3, -5, -11, -19, -29, -97})
    final void anyLong_subzero(final long bound) {
        try {
            final long result = variable.anyLong(bound);
            fail("should fail but was <" + result + ">");
        } catch (final IllegalArgumentException e) {
            // e.printStackTrace();
            assertEquals("<bound> must be greater than ZERO but was " + bound, e.getMessage());
        }
    }

    @ParameterizedTest
    @ValueSource(longs = {-3, -2, -1, 0, 1, 2, 5, 11, 19, 97})
    final void anyLong_min_bound(final long bound) {
        final long result = variable.anyLong(-5, bound);
        assertTrue(-5 <= result,
                   () -> "result is expected to be greater or equal to -5 but was " + result);
        assertTrue(bound > result,
                   () -> "result is expected to be less than " + bound + " but was " + result);
    }

    @ParameterizedTest
    @EnumSource
    final void anyChar_DEFAULT(final Case testCase) {
        final char result = testCase.generator.anyChar();
        final int index = Bridge.STD_CHARACTERS.indexOf(result);
        assertFalse(0 > index);
    }

    @ParameterizedTest
    @EnumSource
    final void anyChar_CHARSET(final Case testCase) {
        final char result = testCase.generator.anyChar("0123456789");
        assertTrue('0' <= result,
                   () -> "result is expected to be greater or equal to '0' but was '" + result + "'");
        assertTrue('9' >= result,
                   () -> "result is expected to be less or equal to '9' but was '" + result + "'");
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 5, 7, 11, 17, 19, 29, 97})
    final void anyString(final int length) {
        final String result = variable.anyString(length, "0123456789");
        assertEquals(length, result.length());
        for (final char c : result.toCharArray()) {
            assertTrue('0' <= c,
                       () -> "result(c) is expected to be greater or equal to '0' but was '" + result + "'");
            assertTrue('9' >= c,
                       () -> "result(c) is expected to be less or equal to '9' but was '" + result + "'");
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -3, -7, -17, -29, -97})
    final void anyString_subzero(final int length) {
        try {
            final String result = variable.anyString(length, "0123456789");
            fail("should fail but was <" + result + ">");
        } catch (final IllegalArgumentException e) {
            // e.printStackTrace();
            assertEquals("<length> must be greater than or equal to zero but was " + length, e.getMessage());
        }
    }

    @Test
    final void anyFloat() {
        final float result = assertInstanceOf(Float.class, variable.anyFloat());
        assertTrue(0.0f <= result,
                   () -> "result is expected to be greater or equal to 0.0 but was " + result);
        assertTrue(1.0f > result,
                   () -> "result is expected to be less than 1.0 but was " + result);
        assertEquals(Float.valueOf("0.32210922"), fixed.anyFloat());
    }

    @Test
    final void anyDouble() {
        final double result = variable.anyDouble();
        assertInstanceOf(Double.class, result);
        assertTrue(0.0 <= result,
                   () -> "result is expected to be greater or equal to 0.0 but was " + result);
        assertTrue(1.0 > result,
                   () -> "result is expected to be less than 1.0 but was " + result);
        assertEquals(0.9205940111020752, fixed.anyDouble());
    }

    @ParameterizedTest
    @EnumSource
    final void anyBigInteger(final Case testCase) {
        final BigInteger result = testCase.generator.anyBigInteger();
        assertTrue(BigInteger.valueOf(Long.MIN_VALUE).compareTo(result) <= 0);
        assertTrue(BigInteger.valueOf(Long.MAX_VALUE).compareTo(result) >= 0);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 5, 7, 11, 17, 19, 29, 97})
    final void anyBigInteger_bound(final int bound) {
        final BigInteger bigBound = BigInteger.valueOf(bound);
        final BigInteger result = variable.anyBigInteger(bigBound);
        assertTrue(BigInteger.ZERO.compareTo(result) <= 0,
                   () -> "result is expected to be greater or equal to ZERO but was " + result);
        assertTrue(bigBound.compareTo(result) > 0,
                   () -> "result is expected to be less than " + bigBound + " but was " + result);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 5, 7, 11, 17, 19, 29, 97})
    final void anyBigInteger_min_bound(final int bound) {
        final BigInteger bigBound = BigInteger.valueOf(bound);
        final BigInteger bigMin = BigInteger.valueOf(bound - 17);
        final BigInteger result = variable.anyBigInteger(bigMin, bigBound);
        assertTrue(bigMin.compareTo(result) <= 0,
                   () -> "result is expected to be greater or equal to " + bigMin + " but was " + result);
        assertTrue(bigBound.compareTo(result) > 0,
                   () -> "result is expected to be less than " + bigBound + " but was " + result);
    }

    @ParameterizedTest
    @EnumSource
    final void anySmallBigInteger(final Case testCase) {
        final BigInteger result = testCase.generator.anySmallBigInteger();
        assertTrue(ZERO.compareTo(result) <= 0);
        assertTrue(ONE.shiftLeft(16).compareTo(result) > 0);
    }

    @ParameterizedTest
    @EnumSource
    final void anySmallBigInteger_bound(final Case testCase) {
        final BigInteger bound = Case.RANDOM.generator.anyBigInteger(ONE.shiftLeft(16));
        final BigInteger result = testCase.generator.anySmallBigInteger(bound);
        assertTrue(ZERO.compareTo(result) <= 0);
        assertTrue(bound.compareTo(result) > 0);
    }

    @Test
    final void anyOf() {
        final int expected = 10000;
        final Map<RoundingMode, AtomicInteger> counts = new EnumMap<>(RoundingMode.class);
        Stream.generate(() -> variable.anyOf(RoundingMode.class))
              .limit((long) expected * RoundingMode.values().length)
              .forEach(mode -> counts.computeIfAbsent(mode, key -> new AtomicInteger(0)).incrementAndGet());

        //counts.forEach((mode, count) -> System.out.printf("[%s] -> %s%n", mode, count));

        final IntSummaryStatistics stats = counts.values()
                                                 .stream()
                                                 .map(AtomicInteger::get)
                                                 .collect(IntSummaryStatistics::new,
                                                          IntSummaryStatistics::accept,
                                                          IntSummaryStatistics::combine);
        assertEquals(1.0 * expected, stats.getAverage());

        final int spread = expected / 50;
        assertEquals((1.0 * expected) - spread, stats.getMin(), 1.0 * spread);
        assertEquals((1.0 * expected) + spread, stats.getMax(), 1.0 * spread);
    }

    enum Case {

        FIXED_MIN(numBits -> BigInteger.ZERO),
        FIXED_MAX(numBits -> ONE.shiftLeft(numBits).subtract(ONE)),
        RANDOM(Generator.simple()),
        SECURE_RANDOM(Generator.simple(new SecureRandom()));

        final Generator generator;

        Case(Generator generator) {
            this.generator = generator;
        }
    }
}
