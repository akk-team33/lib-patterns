package de.team33.test.patterns.tuple.janus;

import de.team33.patterns.tuple.janus.Quad;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class QuadTest extends TupleTestBase {

    @Test
    final void of() {
        final Quad<Integer, Long, String, Instant> result = Quad.of(nextInt(), nextLong(), nextString(), nextInstant());
        assertInstanceOf(Integer.class, result.north());
        assertInstanceOf(Long.class, result.east());
        assertInstanceOf(String.class, result.south());
        assertInstanceOf(Instant.class, result.west());
    }

    @Test
    final void testEquals() {
        final Quad<String, Long, List<Integer>, Byte> expected = Quad.of(nextString(),
                                                                         nextLong(),
                                                                         nextList(this::nextInt),
                                                                         nextByte());
        final Quad<String, Long, List<Integer>, Byte> result = new Quad<>(expected.north(),
                                                                          expected.east(),
                                                                          expected.south(),
                                                                          expected.west());
        assertEquals(expected, result);
    }

    @Test
    final void testHashCode() {
        final Quad<Instant, String, BigInteger, Float> expected = Quad.of(nextInstant(),
                                                                          nextString(),
                                                                          anyBits(nextInt(1, 256)),
                                                                          nextFloat());
        final Quad<Instant, String, BigInteger, Float> result = new Quad<>(expected.north(),
                                                                           expected.east(),
                                                                           expected.south(),
                                                                           expected.west());
        assertEquals(expected.hashCode(), result.hashCode());
    }

    @Test
    final void testToString() {
        final Quad<Set<String>, List<Instant>, Boolean, Short> expected = Quad.of(nextSet(this::nextString),
                                                                                  nextList(this::nextInstant),
                                                                                  nextBoolean(),
                                                                                  nextShort());
        final Quad<Set<String>, List<Instant>, Boolean, Short> result = new Quad<>(expected.north(),
                                                                                   expected.east(),
                                                                                   expected.south(),
                                                                                   expected.west());
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    final void testToString_explizit() {
        assertEquals("[north, east, south, west]", Quad.of("north", "east", "south", "west").toString());
    }

    @Test
    final void testToList() {
        final Quad<List<String>, Set<Instant>, Integer, Long> expected = Quad.of(nextList(this::nextString),
                                                                                 nextSet(this::nextInstant),
                                                                                 nextInt(),
                                                                                 nextLong());
        assertEquals(Arrays.asList(expected.north(), expected.east(), expected.south(), expected.west()),
                     expected.toList());
    }

    @Test
    final void testToList_explizit() {
        assertEquals(Arrays.asList("north", "east", "south", "west"),
                     Quad.of("north", "east", "south", "west").toList());
    }
}
