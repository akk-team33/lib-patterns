package de.team33.patterns.arbitrary.mimas;

import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.random.RandomGenerator;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RandomProxyTest {

    static final SecureRandom RANDOM = new SecureRandom();

    private final long fixedValue = RANDOM.nextLong();
    private final RandomGenerator backing = () -> fixedValue;
    private final RandomProxy proxy = new RandomProxy(backing);

    @Test
    final void isDeprecated() {
        assertEquals(backing.isDeprecated(), proxy.isDeprecated());
    }

    @Test
    final void doubles() {
        final double[] expected = backing.doubles().limit(3).toArray();
        final double[] result = proxy.doubles().limit(3).toArray();
        assertArrayEquals(expected, result);
    }

    @Test
    final void doubles_size() {
        final double[] expected = backing.doubles(3).toArray();
        final double[] result = proxy.doubles(3).toArray();
        assertArrayEquals(expected, result);
    }

    @Test
    final void doubles_bounded() {
        final double[] expected = backing.doubles(2.0, 5.0).limit(3).toArray();
        final double[] result = proxy.doubles(2.0, 5.0).limit(3).toArray();
        assertArrayEquals(expected, result);
    }

    @Test
    final void doubles_size_bounded() {
        final double[] expected = backing.doubles(3, 7.0, 19.0).toArray();
        final double[] result = proxy.doubles(3, 7.0, 19.0).toArray();
        assertArrayEquals(expected, result);
    }

    @Test
    final void ints() {
        final int[] expected = backing.ints().limit(3).toArray();
        final int[] result = proxy.ints().limit(3).toArray();
        assertArrayEquals(expected, result);
    }

    @Test
    final void ints_size() {
        final int[] expected = backing.ints(3).toArray();
        final int[] result = proxy.ints(3).toArray();
        assertArrayEquals(expected, result);
    }

    @Test
    final void ints_bounded() {
        final int[] expected = backing.ints(7, 29).limit(3).toArray();
        final int[] result = proxy.ints(7, 29).limit(3).toArray();
        assertArrayEquals(expected, result);
    }

    @Test
    final void ints_size_bounded() {
        final int[] expected = backing.ints(3, 5, 23).toArray();
        final int[] result = proxy.ints(3, 5, 23).toArray();
        assertArrayEquals(expected, result);
    }

    @Test
    final void longs() {
        final long[] expected = backing.longs().limit(3).toArray();
        final long[] result = proxy.longs().limit(3).toArray();
        assertArrayEquals(expected, result);
    }

    @Test
    final void longs_size() {
        final long[] expected = backing.longs(3).toArray();
        final long[] result = proxy.longs(3).toArray();
        assertArrayEquals(expected, result);
    }

    @Test
    final void longs_bounded() {
        final long[] expected = backing.longs(7, 29).limit(3).toArray();
        final long[] result = proxy.longs(7, 29).limit(3).toArray();
        assertArrayEquals(expected, result);
    }

    @Test
    final void longs_size_bounded() {
        final long[] expected = backing.longs(3, 5, 23).toArray();
        final long[] result = proxy.longs(3, 5, 23).toArray();
        assertArrayEquals(expected, result);
    }

    @Test
    final void nextBoolean() {
        assertEquals(backing.nextBoolean(), proxy.nextBoolean());
    }

    @Test
    final void nextBytes() {
        final byte[] expected = new byte[7];
        backing.nextBytes(expected);
        final byte[] result = new byte[7];
        proxy.nextBytes(result);
        assertArrayEquals(expected, result);
    }

    @Test
    final void nextFloat() {
        assertEquals(backing.nextFloat(), proxy.nextFloat());
    }

    @Test
    final void nextFloat_bound() {
        assertEquals(backing.nextFloat(5.0f), proxy.nextFloat(5.0f));
    }

    @Test
    final void nextFloat_origin_bound() {
        assertEquals(backing.nextFloat(3, 7), proxy.nextFloat(3, 7));
    }

    @Test
    final void nextDouble() {
        assertEquals(backing.nextDouble(), proxy.nextDouble());
    }

    @Test
    final void nextDouble_bound() {
        assertEquals(backing.nextDouble(5.0), proxy.nextDouble(5.0));
    }

    @Test
    final void nextDouble_origin_bound() {
        assertEquals(backing.nextDouble(3, 7), proxy.nextDouble(3, 7));
    }

    @Test
    final void nextInt() {
        assertEquals(backing.nextInt(), proxy.nextInt());
    }

    @Test
    final void nextInt_bound() {
        assertEquals(backing.nextInt(5), proxy.nextInt(5));
    }

    @Test
    final void nextInt_origin_bound() {
        assertEquals(backing.nextInt(3, 7), proxy.nextInt(3, 7));
    }

    @Test
    final void nextLong() {
        assertEquals(backing.nextLong(), proxy.nextLong());
    }

    @Test
    final void nextLong_bound() {
        assertEquals(backing.nextLong(5), proxy.nextLong(5));
    }

    @Test
    final void nextLong_origin_bound() {
        assertEquals(backing.nextLong(3, 7), proxy.nextLong(3, 7));
    }

    @Test
    final void nextGaussian() {
        assertEquals(backing.nextGaussian(), proxy.nextGaussian());
    }

    @Test
    final void nextGaussian_mean_stddev() {
        assertEquals(backing.nextGaussian(5.0, 2.0), proxy.nextGaussian(5.0, 2.0));
    }

    @Test
    final void nextExponential() {
        assertEquals(backing.nextExponential(), proxy.nextExponential());
    }
}
