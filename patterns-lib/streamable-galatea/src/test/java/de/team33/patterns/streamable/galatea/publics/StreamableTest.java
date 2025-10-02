package de.team33.patterns.streamable.galatea.publics;

import de.team33.patterns.arbitrary.mimas.Generator;
import de.team33.patterns.streamable.galatea.Streamable;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;
import static org.junit.jupiter.api.Assertions.*;

class StreamableTest {

    private static final Generator GENERATOR = Generator.of(new SecureRandom());

    private final List<String> origin = Stream.generate(GENERATOR::anyString)
                                              .limit(GENERATOR.anyInt(4, 8))
                                              .toList();

    @Test
    final void stream() {
        final Streamable<String> streamable = origin::stream;
        assertEquals(origin, streamable.stream().toList());
    }

    @Test
    final void empty() {
        assertTrue(Streamable.empty().stream().findAny().isEmpty());
    }

    @Test
    final void of_one() {
        final Streamable<String> streamable = Streamable.of(origin.get(0));
        assertEquals(List.of(origin.get(0)), streamable.stream().toList());
    }

    @Test
    final void of_two() {
        final Streamable<String> streamable = Streamable.of(origin.get(0), origin.get(1));
        assertEquals(List.of(origin.get(0), origin.get(1)), streamable.stream().toList());
    }

    @Test
    final void of_more() {
        final Streamable<String> streamable = Streamable.of(origin.get(0), origin.get(1), origin.get(2));
        assertEquals(List.of(origin.get(0), origin.get(1), origin.get(2)), streamable.stream().toList());
    }

    @Test
    final void containsAny() {
        final Streamable<String> streamable = origin::stream;
        assertTrue(streamable.containsAny(origin::contains));
        assertFalse(streamable.containsAny(not(origin::contains)));
    }

    @Test
    final void contains() {
        final Streamable<String> streamable = origin::stream;
        assertTrue(streamable.contains(origin.get(1)));
    }

    @Test
    final void containsAll() {
        final Streamable<String> streamable = origin::stream;
        assertTrue(streamable.containsAll(Streamable.of(origin.get(0), origin.get(2), origin.get(3))));
    }

    @Test
    final void forEach() {
        final List<String> result = new LinkedList<>();
        final Streamable<String> streamable = origin::stream;
        streamable.forEach(result::add);
        assertEquals(origin, result);
    }
}