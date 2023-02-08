package de.team33.test.patterns.reflect.luna;

import de.team33.patterns.random.tarvos.Generator;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.team33.sample.patterns.reflect.luna.CloningSample.*;
import static org.junit.jupiter.api.Assertions.*;

class CloningTest extends Random implements Generator {

    @Test
    final void testString() {
        final String expected = nextString();
        final String result = CLONE_MAP.get(String.class).apply(expected);
        assertSame(expected, result);
    }

    @Test
    final void testLong() {
        final Long expected = nextLong();
        final Long result = CLONE_MAP.get(Long.class).apply(expected);
        assertSame(expected, result);
    }

    @Test
    final void testDate() {
        final Date expected = new Date(System.currentTimeMillis() + nextLong(Integer.MIN_VALUE, Integer.MAX_VALUE));
        final Date result = CLONE_MAP.get(Date.class).apply(expected);
        assertNotSame(expected, result);
        assertEquals(expected, result);
    }

    @Test
    final void testList() {
        final List<String> expected = Stream.generate(() -> nextString())
                                            .limit(nextInt(4, 16))
                                            .collect(Collectors.toList());
        //noinspection unchecked
        final List<String> result = CLONE_MAP.get(List.class).apply(expected);
        assertNotSame(expected, result);
        assertEquals(expected, result);
    }

    @Test
    final void testMap() {
        final Map<String, Double> expected = Stream.generate(() -> nextString())
                                           .limit(nextInt(4, 16))
                                           .collect(Collectors.toMap(key -> key, key -> nextDouble()));
        //noinspection unchecked
        final Map<String, Double> result = CLONE_MAP.get(Map.class).apply(expected);
        assertNotSame(expected, result);
        assertEquals(expected, result);
    }

    private String nextString() {
        return nextString(16, "abcdefghijklmnopqrstuvwxyz");
    }

    @Override
    public final BigInteger nextBits(final int numBits) {
        return new BigInteger(numBits, this);
    }
}
