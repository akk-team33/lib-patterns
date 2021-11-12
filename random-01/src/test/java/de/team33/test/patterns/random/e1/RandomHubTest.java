package de.team33.test.patterns.random.e1;

import de.team33.patterns.random.e1.RandomHub;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigInteger;
import java.util.IntSummaryStatistics;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class RandomHubTest {

    private final RandomHub defaultHub = RandomHub.builder().build();

    @Test
    final void any() {
        assertInstanceOf(Boolean.class, defaultHub.any(false));
        assertInstanceOf(Boolean.class, defaultHub.any(true));
        assertInstanceOf(Byte.class, defaultHub.any(RandomHub.BYTE));
        assertInstanceOf(Short.class, defaultHub.any(RandomHub.SHORT));
        assertInstanceOf(Integer.class, defaultHub.any(RandomHub.INTEGER));
        assertInstanceOf(Long.class, defaultHub.any(RandomHub.LONG));
        assertInstanceOf(Float.class, defaultHub.any(RandomHub.FLOAT));
        assertInstanceOf(Double.class, defaultHub.any(RandomHub.DOUBLE));
        assertInstanceOf(Character.class, defaultHub.any(RandomHub.CHARACTER));
        assertInstanceOf(String.class, defaultHub.any(RandomHub.STRING));
    }

    @Test
    final void stream() {
        defaultHub.stream(RandomHub.INTEGER)
                  .limit(10000)
                  .forEach(value -> assertInstanceOf(Integer.class, value));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 5, 7, 11})
    final void anyBits(final int numBits) {
        final int numValues = 1 << numBits;
        final int[] values = new int[numValues];
        final int countPerValue = 1000;
        Stream.generate(() -> defaultHub.anyBits(numBits))
              .limit(countPerValue * numValues)
              .forEach(value -> values[value.intValue()] += 1);
        final IntSummaryStatistics stats = IntStream.of(values).summaryStatistics();

        //IntStream.range(0, values.length).forEach(value -> System.out.printf("%01d -> %d%n", value, values[value]));

        assertEquals(1.0 * countPerValue, stats.getAverage());

        final int deviation = countPerValue / 10;
        assertEquals(1.0 * (countPerValue - deviation), 1.0 * stats.getMin(), 1.0 * deviation);
        assertEquals(1.0 * (countPerValue + deviation), 1.0 * stats.getMax(), 1.0 * deviation);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 11, 278, Integer.MAX_VALUE})
    final void anyBigInteger_bound(final int intBound) {
        final BigInteger bound = BigInteger.valueOf(intBound);
        final BigInteger result = defaultHub.anyBigInteger(bound);
        assertNotEquals(1, BigInteger.ZERO.compareTo(result));
        assertEquals(1, bound.compareTo(result));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 11, 278, Integer.MAX_VALUE})
    final void anyBigInteger_min_bound(final int intBound) {
        final BigInteger bound = BigInteger.valueOf(intBound);
        final BigInteger min = bound.negate();
        final BigInteger result = defaultHub.anyBigInteger(min, bound);
        assertNotEquals(1, min.compareTo(result));
        assertEquals(1, bound.compareTo(result));
    }

    @Test
    void anyInt() {
    }

    @Test
    void testAnyInt() {
    }

    @Test
    void anyChar() {
    }

    @Test
    void anyString() {
    }

    @Test
    void anyOf() {
    }
}