package de.team33.patterns.streamable.galatea.publics;

import de.team33.patterns.arbitrary.mimas.Generator;
import de.team33.patterns.streamable.galatea.Streamable;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;
import static org.junit.jupiter.api.Assertions.*;

class StreamableTest {

    private static final Generator GENERATOR = Generator.of(new SecureRandom());
    private static final List<String> EMPTY = List.of();

    private final List<String> origin = Stream.generate(GENERATOR::anyString)
                                              .limit(GENERATOR.anyInt(4, 8))
                                              .toList();
    private final List<String> other = Stream.generate(GENERATOR::anyString)
                                             .filter(not(origin::contains))
                                             .limit(GENERATOR.anyInt(4, 8))
                                             .toList();
    private final List<String> combined = Stream.concat(origin.stream(), other.stream())
                                                .toList();

    private static <E> Streamable<E> streamable(final Collection<E> collection) {
        return collection::stream;
    }

    @Test
    final void of_Array() {
        assertEquals(origin, Streamable.of(origin.toArray(String[]::new)).stream().toList());
    }

    @Test
    final void of_Iterable() {
        //noinspection FunctionalExpressionCanBeFolded
        assertEquals(origin, Streamable.of(origin::iterator).stream().toList());
        assertEquals(origin, Streamable.of(origin).stream().toList());
    }

    @Test
    final void map() {
        assertEquals(origin.stream().map(String::length).toList(),
                     streamable(origin).map(stream -> stream.map(String::length)).stream().toList());
    }

    @Test
    final void map_static() {
        assertEquals(origin.stream().map(String::length).toList(),
                     Streamable.map(origin::stream, stream -> stream.map(String::length)).stream().toList());
    }

    @Test
    final void map_super() {
        final Streamable<Object> objects = Streamable.map(origin::stream);
        assertEquals(origin, objects.stream().toList());
    }

    @Test
    final void isEmpty() {
        assertTrue(Streamable.empty().isEmpty());
        assertTrue(streamable(EMPTY).isEmpty());
        assertEquals(EMPTY.isEmpty(), streamable(EMPTY).isEmpty());
        assertFalse(streamable(origin).isEmpty());
        assertEquals(origin.isEmpty(), streamable(origin).isEmpty());
    }

    @Test
    final void containsAny() {
        assertTrue(Streamable.of(GENERATOR.anyString()).containsAny());
        assertTrue(streamable(origin).containsAny());
        assertEquals(0 < origin.size(), streamable(origin).containsAny());
        assertFalse(streamable(EMPTY).containsAny());
        assertEquals(0 < EMPTY.size(), streamable(EMPTY).containsAny());
    }

    @Test
    final void containsAny_withPredicate() {
        for (final String element: combined) {
            assertEquals(origin.contains(element),
                         streamable(origin).containsAny(item -> Objects.equals(item, element)));
        }
    }

    @Test
    final void contains() {
        for (final String element: combined) {
            assertEquals(origin.contains(element), streamable(origin).contains(element));
        }
    }

    @Test
    final void containsAny_candidates() {
        assertTrue(streamable(origin).containsAny(combined::stream));
        assertFalse(streamable(origin).containsAny(other::stream));
        assertFalse(streamable(origin).containsAny(EMPTY::stream));
    }

    @Test
    final void containsAll() {
        assertTrue(streamable(combined).containsAll(Streamable.of(combined.get(0),
                                                                  combined.get(2),
                                                                  combined.get(3),
                                                                  combined.get(5))));

        assertTrue(streamable(combined).containsAll(origin::stream));
        assertEquals(combined.containsAll(origin), streamable(combined).containsAll(origin::stream));

        assertTrue(streamable(origin).containsAll(EMPTY::stream));
        assertEquals(origin.containsAll(EMPTY), streamable(origin).containsAll(EMPTY::stream));

        assertFalse(streamable(origin).containsAll(other::stream));
        assertEquals(origin.containsAll(other), streamable(origin).containsAll(other::stream));

        assertFalse(streamable(origin).containsAll(combined::stream));
        assertEquals(origin.containsAll(combined), streamable(origin).containsAll(combined::stream));
    }

    @Test
    final void forEach() {
        final List<String> result = new LinkedList<>();
        streamable(origin).forEach(result::add);
        assertEquals(origin, result);
    }
}