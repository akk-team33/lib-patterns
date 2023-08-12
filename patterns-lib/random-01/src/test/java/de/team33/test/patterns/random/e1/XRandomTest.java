package de.team33.test.patterns.random.e1;

import de.team33.patterns.random.e1.XRandom;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.EnumMap;
import java.util.IntSummaryStatistics;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class XRandomTest {

    private static final long FIXED = 0x0123456789abcdefL;
    public static final char FIXED_CHAR = '\ucdef';

    private final XRandom fixed = XRandom.using(numBits -> BigInteger.valueOf(FIXED)
                                                                     .shiftLeft(Long.SIZE)
                                                                     .add(BigInteger.valueOf(FIXED))
                                                                     .and(BigInteger.ONE.shiftLeft(numBits)
                                                                                      .subtract(BigInteger.ONE)));
    private final XRandom real = XRandom.using(new Random());

    @Test
    final void test_BigInteger_doubleValue() {
        assertEquals(StrictMath.pow(2.0, 128), BigInteger.ONE.shiftLeft(128)
                                                             .doubleValue());
        assertEquals(StrictMath.pow(2.0, 128) - 1.0, BigInteger.ONE.shiftLeft(128)
                                                                   .subtract(BigInteger.ONE)
                                                                   .doubleValue());
    }

    @Test
    final void anyBits() {
        assertEquals(0x89abcdef, fixed.anyBits(64).intValue());
    }

    @Test
    final void anyBoolean() {
        assertTrue(fixed.anyBoolean());

        final int expected = 10000;
        final int[] counts = new int[2];
        Stream.generate(real::anyBoolean)
              .limit(expected * counts.length)
              .forEach(bool -> counts[bool ? 1 : 0]++);

        //IntStream.range(0, counts.length).forEach(index -> System.out.printf("[%d] -> %d%n", index, counts[index]));

        final IntSummaryStatistics stats = IntStream.of(counts).summaryStatistics();
        assertEquals(1.0 * expected, stats.getAverage());

        final int spread = expected / 100;
        assertEquals((1.0 * expected) - spread, stats.getMin(), 1.0 * spread);
        assertEquals((1.0 * expected) + spread, stats.getMax(), 1.0 * spread);
    }

    @Test
    final void anyByte() {
        assertEquals(-17, fixed.anyByte());
    }

    @Test
    final void anyShort() {
        assertEquals(-12817, fixed.anyShort());
    }

    @Test
    final void anyInt() {
        assertEquals(0x89abcdef, fixed.anyInt());
    }

    @Test
    final void anyInt_bound() {
        assertEquals(13, fixed.anyInt(29));
    }

    @Test
    final void anyInt_min_bound() {
        assertEquals(8, fixed.anyInt(-5, 23));
    }

    @Test
    final void anyLong() {
        assertEquals(FIXED, fixed.anyLong());
    }

    @Test
    final void anyChar() {
        assertEquals(FIXED_CHAR, fixed.anyChar());
    }

    @Test
    final void anyCharOf() {
        assertEquals('l', fixed.anyChar("abcdefghijklmnopqrstuvwx"));
    }

    @Test
    final void anyString() {
        assertEquals("", fixed.anyString(0, "abcdefghijklmnopqrstuvwx"));
        assertEquals("l", fixed.anyString(1, "abcdefghijklmnopqrstuvwx"));
        assertEquals("lllll", fixed.anyString(5, "abcdefghijklmnopqrstuvwx"));
        assertThrows(IllegalArgumentException.class, () -> fixed.anyString(-1, "abc"));
    }

    @Test
    final void anyFloat_fixed() {
        final float numerator = BigInteger.valueOf(FIXED & ((1L << Float.SIZE) - 1)).floatValue();
        final float denominator = BigInteger.ONE.shiftLeft(Float.SIZE).floatValue();
        assertEquals(numerator / denominator, fixed.anyFloat());
    }

    @RepeatedTest(100)
    final void anyFloat() {
        final float result = real.anyFloat();
        assertNotEquals(1, Float.compare(0.0F, result));
        assertEquals(1, Float.compare(1.0F, result));
    }

    @Test
    final void anyDouble_fixed() {
        final double numerator = BigInteger.valueOf(FIXED).doubleValue();
        final double denominator = BigInteger.ONE.shiftLeft(Long.SIZE).doubleValue();
        assertEquals(numerator / denominator, fixed.anyDouble());
    }

    @RepeatedTest(100)
    final void anyDouble() {
        final double result = real.anyDouble();
        assertNotEquals(1, Double.compare(0.0, result));
        assertEquals(1, Double.compare(1.0, result));
    }

    @RepeatedTest(100)
    final void anyBigInteger_bound() {
        final BigInteger bound = BigInteger.valueOf(real.anyByte());
        try {
            final BigInteger result = real.anyBigInteger(bound);
            final Supplier<String> message = () -> String.format("expected: ZERO <= result (%s) < bound (%s)",
                                                                 result, bound);
            assertNotEquals(1, BigInteger.ZERO.compareTo(result), message);
            assertEquals(1, bound.compareTo(result), message);
        } catch (final IllegalArgumentException e) {
            assertNotEquals(1, bound.compareTo(BigInteger.ZERO),
                            () -> String.format("expected to throw when: ZERO >= bound (%s)", bound));
        }
    }

    @RepeatedTest(100)
    final void anyBigInteger_min_bound() {
        final BigInteger bound = BigInteger.valueOf(real.anyByte());
        final BigInteger min = BigInteger.valueOf(real.anyByte());
        try {
            final BigInteger result = real.anyBigInteger(min, bound);
            final Supplier<String> message = () -> String.format("expected: min (%s) <= result (%s) < bound (%s)",
                                                                 min, result, bound);
            assertNotEquals(1, min.compareTo(result), message);
            assertEquals(1, bound.compareTo(result), message);
        } catch (final IllegalArgumentException e) {
            assertNotEquals(1, bound.compareTo(min),
                            () -> String.format("expected to throw when: min (%s) >= bound (%s)", min, bound));
        }
    }

    @Test
    final void anyOf() {
        assertEquals(RoundingMode.UNNECESSARY, fixed.anyOf(RoundingMode.values()));

        final int expected = 10000;
        final Map<RoundingMode, AtomicInteger> counts = new EnumMap<>(RoundingMode.class);
        Stream.generate(() -> real.anyOf(RoundingMode.values()))
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
