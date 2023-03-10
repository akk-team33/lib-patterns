package de.team33.test.patterns.tuple.janus;

import de.team33.patterns.tuple.janus.Trip;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class TripTest extends TupleTestBase {

    @Test
    final void of() {
        final Trip<Integer, Long, String> result = Trip.of(nextInt(), nextLong(), nextString());
        assertInstanceOf(Integer.class, result.red());
        assertInstanceOf(Long.class, result.green());
        assertInstanceOf(String.class, result.blue());
    }

    @Test
    final void testEquals() {
        final Trip<String, Long, List<Integer>> expected = Trip.of(nextString(),
                                                                   nextLong(),
                                                                   nextList(this::nextInt));
        final Trip<String, Long, List<Integer>> result = new Trip<>(expected.red(),
                                                                    expected.green(),
                                                                    expected.blue());
        assertEquals(expected, result);
    }

    @Test
    final void testHashCode() {
        final Trip<Instant, String, BigInteger> expected = Trip.of(nextInstant(),
                                                                   nextString(),
                                                                   nextBits(nextInt(1, 256)));
        final Trip<Instant, String, BigInteger> result = new Trip<>(expected.red(),
                                                                    expected.green(),
                                                                    expected.blue());
        assertEquals(expected.hashCode(), result.hashCode());
    }

    @Test
    final void testToString() {
        final Trip<Set<String>, List<Instant>, Boolean> expected = Trip.of(nextSet(this::nextString),
                                                                           nextList(this::nextInstant),
                                                                           nextBoolean());
        final Trip<Set<String>, List<Instant>, Boolean> result = new Trip<>(expected.red(),
                                                                            expected.green(),
                                                                            expected.blue());
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    final void testToString_explizit() {
        assertEquals("[red, green, blue]", Trip.of("red", "green", "blue").toString());
    }

    @Test
    final void testToList() {
        final Trip<List<String>, Set<Instant>, Integer> expected = Trip.of(nextList(this::nextString),
                                                                           nextSet(this::nextInstant),
                                                                           nextInt());
        assertEquals(Arrays.asList(expected.red(), expected.green(), expected.blue()), expected.toList());
    }

    @Test
    final void testToList_explizit() {
        assertEquals(Arrays.asList("red", "green", "blue"), Trip.of("red", "green", "blue").toList());
    }
}
