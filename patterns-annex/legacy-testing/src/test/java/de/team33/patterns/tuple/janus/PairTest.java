package de.team33.patterns.tuple.janus;

import de.team33.patterns.tuple.janus.Pair;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class PairTest extends TupleTestBase {

    @Test
    final void of() {
        final Pair<Integer, Long> result = Pair.of(nextInt(), nextLong());
        assertInstanceOf(Integer.class, result.left());
        assertInstanceOf(Long.class, result.right());
    }

    @Test
    final void testEquals() {
        final Pair<String, Long> expected = Pair.of(nextString(), nextLong());
        final Pair<String, Long> result = new Pair<>(expected.left(), expected.right());
        assertEquals(expected, result);
    }

    @Test
    final void testHashCode() {
        final Pair<Instant, String> expected = Pair.of(nextInstant(), nextString());
        final Pair<Instant, String> result = new Pair<>(expected.left(), expected.right());
        assertEquals(expected.hashCode(), result.hashCode());
    }

    @Test
    final void testToString() {
        final Pair<Set<String>, List<Instant>> expected = Pair.of(nextSet(this::nextString),
                                                                  nextList(this::nextInstant));
        final Pair<Set<String>, List<Instant>> result = new Pair<>(expected.left(), expected.right());
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    final void testToString_explizit() {
        assertEquals("[left, right]", Pair.of("left", "right").toString());
    }

    @Test
    final void testToList() {
        final Pair<List<String>, Set<Instant>> expected = Pair.of(nextList(this::nextString),
                                                                  nextSet(this::nextInstant));
        assertEquals(Arrays.asList(expected.left(), expected.right()), expected.toList());
    }

    @Test
    final void testToList_explizit() {
        assertEquals(Arrays.asList("left", "right"), Pair.of("left", "right").toList());
    }
}
