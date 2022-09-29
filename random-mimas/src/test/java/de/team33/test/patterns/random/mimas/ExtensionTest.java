package de.team33.test.patterns.random.mimas;

import de.team33.patterns.random.mimas.Extension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.EnumMap;
import java.util.IntSummaryStatistics;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExtensionTest {

    static class XRandom extends Random implements Extension {

        @Override
        public BigInteger nextBits(final int numBits) {
            return new BigInteger(numBits, this);
        }
    }

    private static final BigInteger FIXED = new BigInteger("45619B2F8D02DD0BD545B8C8BD2CDBF86E8149DD7581925275C0", 16);

    private final XRandom random = new XRandom();

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 5, 7, 11, 17, 19, 29, 97, 197, 1997})
    final void nextBits(final int numBits) {
        final BigInteger result = random.nextBits(numBits);
        assertTrue(BigInteger.ZERO.compareTo(result) <= 0,
                   () -> "result is expected to be greater or equal to ZERO but was " + result);

        final BigInteger limit = BigInteger.ONE.shiftLeft(numBits);
        assertTrue(limit.compareTo(result) > 0,
                   () -> "result is expected to be less than " + limit + " but was " + result);
    }

    @Test
    final void nextBoolean() {
        assertInstanceOf(Boolean.class, random.nextBoolean());
    }

    @Test
    final void nextByte() {
        assertInstanceOf(Byte.class, random.nextByte());
    }

    @Test
    final void nextShort() {
        assertInstanceOf(Short.class, random.nextShort());
    }

    @Test
    final void nextInt() {
        assertInstanceOf(Integer.class, random.nextInt());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 5, 7, 11, 17, 19, 29, 97})
    final void nextInt_bound(final int bound) {
        final int result = random.nextInt(bound);
        //noinspection ConstantConditions
        assertTrue(0 <= result,
                   () -> "result is expected to be greater or equal to ZERO but was " + result);
        assertTrue(bound > result,
                   () -> "result is expected to be less than " + bound + " but was " + result);
    }

    @ParameterizedTest
    @ValueSource(ints = {-3, -2, -1, 0, 1, 2, 5, 11, 19, 97})
    final void nextInt_min_bound(final int bound) {
        final int result = random.nextInt(-5, bound);
        assertTrue(-5 <= result,
                   () -> "result is expected to be greater or equal to -5 but was " + result);
        assertTrue(bound > result,
                   () -> "result is expected to be less than " + bound + " but was " + result);
    }

    @Test
    final void nextLong() {
        assertInstanceOf(Long.class, random.nextLong());
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3, 5, 7, 11, 17, 19, 29, 97})
    final void nextLong_bound(final long bound) {
        final long result = random.nextLong(bound);
        assertTrue(0 <= result,
                   () -> "result is expected to be greater or equal to ZERO but was " + result);
        assertTrue(bound > result,
                   () -> "result is expected to be less than " + bound + " but was " + result);
    }

    @ParameterizedTest
    @ValueSource(longs = {-3, -2, -1, 0, 1, 2, 5, 11, 19, 97})
    final void nextLong_min_bound(final long bound) {
        final long result = random.nextLong(-5, bound);
        assertTrue(-5 <= result,
                   () -> "result is expected to be greater or equal to -5 but was " + result);
        assertTrue(bound > result,
                   () -> "result is expected to be less than " + bound + " but was " + result);
    }

    @Test
    final void nextChar() {
        final char result = random.nextChar("0123456789");
        assertTrue('0' <= result,
                   () -> "result is expected to be greater or equal to '0' but was '" + result + "'");
        assertTrue('9' >= result,
                   () -> "result is expected to be less or equal to '9' but was '" + result + "'");
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 5, 7, 11, 17, 19, 29, 97})
    final void nextString(final int length) {
        final String result = random.nextString(length, "0123456789");
        assertEquals(length, result.length());
        for (final char c : result.toCharArray()) {
            assertTrue('0' <= c,
                       () -> "result(c) is expected to be greater or equal to '0' but was '" + result + "'");
            assertTrue('9' >= c,
                       () -> "result(c) is expected to be less or equal to '9' but was '" + result + "'");
        }
    }

    @Test
    final void nextFloat() {
        assertInstanceOf(Float.class, random.nextFloat());
    }

    @Test
    final void nextDouble() {
        assertInstanceOf(Double.class, random.nextDouble());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 5, 7, 11, 17, 19, 29, 97})
    final void nextBigInteger_bound(final int bound) {
        final BigInteger bigBound = BigInteger.valueOf(bound);
        final BigInteger result = random.nextBigInteger(bigBound);
        assertTrue(BigInteger.ZERO.compareTo(result) <= 0,
                   () -> "result is expected to be greater or equal to ZERO but was " + result);
        assertTrue(bigBound.compareTo(result) > 0,
                   () -> "result is expected to be less than " + bigBound + " but was " + result);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 5, 7, 11, 17, 19, 29, 97})
    final void nextBigInteger_min_bound(final int bound) {
        final BigInteger bigBound = BigInteger.valueOf(bound);
        final BigInteger bigMin = BigInteger.valueOf(bound - 17);
        final BigInteger result = random.nextBigInteger(bigMin, bigBound);
        assertTrue(bigMin.compareTo(result) <= 0,
                   () -> "result is expected to be greater or equal to " + bigMin + " but was " + result);
        assertTrue(bigBound.compareTo(result) > 0,
                   () -> "result is expected to be less than " + bigBound + " but was " + result);
    }

    @Test
    final void nextOf() {
        final int expected = 10000;
        final Map<RoundingMode, AtomicInteger> counts = new EnumMap<>(RoundingMode.class);
        Stream.generate(() -> random.nextOf(RoundingMode.class))
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
}