package de.team33.test.patterns.tuple.janus;

import de.team33.patterns.tuple.janus.Triple;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class TripleTest extends TupleTestBase {

    @Test
    final void of() {
        final Triple<Integer, Long, String> result = Triple.of(nextInt(), nextLong(), nextString());
        assertInstanceOf(Integer.class, result.getRed());
        assertInstanceOf(Long.class, result.getGreen());
        assertInstanceOf(String.class, result.getBlue());
    }

    @Test
    final void testEquals() {
        final Triple<String, Long, List<Integer>> expected = Triple.of(nextString(),
                                                                       nextLong(),
                                                                       nextList(this::nextInt));
        final Triple<String, Long, List<Integer>> result = new Triple<>(expected.getRed(),
                                                                        expected.getGreen(),
                                                                        expected.getBlue());
        assertEquals(expected, result);
    }

    @Test
    final void testHashCode() {
        final Triple<Instant, String, BigInteger> expected = Triple.of(nextInstant(),
                                                                       nextString(),
                                                                       nextBits(nextInt(1, 256)));
        final Triple<Instant, String, BigInteger> result = new Triple<>(expected.getRed(),
                                                                        expected.getGreen(),
                                                                        expected.getBlue());
        assertEquals(expected.hashCode(), result.hashCode());
    }

    @Test
    final void testToString() {
        final Triple<Set<String>, List<Instant>, Boolean> expected = Triple.of(nextSet(this::nextString),
                                                                               nextList(this::nextInstant),
                                                                               nextBoolean());
        final Triple<Set<String>, List<Instant>, Boolean> result = new Triple<>(expected.getRed(),
                                                                                expected.getGreen(),
                                                                                expected.getBlue());
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    final void testToString_explizit() {
        assertEquals("[red, green, blue]", Triple.of("red", "green", "blue").toString());
    }

    @Test
    final void testToList() {
        final Triple<List<String>, Set<Instant>, Integer> expected = Triple.of(nextList(this::nextString),
                                                                               nextSet(this::nextInstant),
                                                                               nextInt());
        assertEquals(Arrays.asList(expected.getRed(), expected.getGreen(), expected.getBlue()), expected.toList());
    }

    @Test
    final void testToList_explizit() {
        assertEquals(Arrays.asList("red", "green", "blue"), Triple.of("red", "green", "blue").toList());
    }
}
