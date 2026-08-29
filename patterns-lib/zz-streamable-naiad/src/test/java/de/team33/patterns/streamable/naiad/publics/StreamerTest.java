package de.team33.patterns.streamable.naiad.publics;

import de.team33.patterns.arbitrary.mimas.Generator;
import de.team33.patterns.streamable.naiad.Streamable;
import de.team33.patterns.streamable.naiad.Streamer;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;
import static org.junit.jupiter.api.Assertions.*;

class StreamerTest {

    private static final Generator GENERATOR = Generator.of(new SecureRandom());
    private static final List<String> EMPTY_LIST = List.of();

    private final List<String> origin = Stream.generate(GENERATOR::anyString)
                                              .limit(GENERATOR.anyInt(4, 8))
                                              .toList();
    private final List<String> other = Stream.generate(GENERATOR::anyString)
                                             .filter(not(origin::contains))
                                             .limit(GENERATOR.anyInt(4, 8))
                                             .toList();
    private final List<String> combined = Stream.concat(origin.stream(), other.stream())
                                                .toList();

    @Test
    final void of_single() {
        assertEquals(origin, Streamer.of(origin.get(0))
                                     .addAll(() -> origin.stream().skip(1)).toList());
    }

    @Test
    final void of_Iterable() {
        assertEquals(origin, Streamer.of(origin::iterator).toList());
        assertEquals(origin, Streamer.of(origin).toList());
    }

    @Test
    final void isEmpty() {
        assertTrue(Streamer.empty().isEmpty());
        assertTrue(Streamer.of(EMPTY_LIST).isEmpty());
        assertEquals(EMPTY_LIST.isEmpty(), Streamer.of(EMPTY_LIST).isEmpty());
        assertFalse(Streamer.of(origin).isEmpty());
        assertEquals(origin.isEmpty(), Streamer.of(origin).isEmpty());
    }

    @Test
    final void containsAny() {
        assertTrue(Streamer.of(List.of(GENERATOR.anyString())).containsAny());
        assertTrue(Streamer.of(origin).containsAny());
        assertEquals(0 < origin.size(), Streamer.of(origin).containsAny());
        assertFalse(Streamer.of(EMPTY_LIST).containsAny());
        assertEquals(0 < EMPTY_LIST.size(), Streamer.of(EMPTY_LIST).containsAny());
    }

    @Test
    final void containsAny_withPredicate() {
        for (final String element : combined) {
            assertEquals(origin.contains(element),
                         Streamer.of(origin).containsAny(item -> Objects.equals(item, element)));
        }

        assertThrows(NullPointerException.class, () -> Streamer.of(origin).containsAny((Predicate<Object>) null));
        assertThrows(NullPointerException.class, () -> Streamer.empty().containsAny((Predicate<Object>) null));
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @Test
    final void containsAll_withPredicate() {
        assertTrue(Streamer.of(combined).containsAll(combined::contains));
        assertTrue(Streamer.of(origin).containsAll(combined::contains));
        assertTrue(Streamer.empty().containsAll(origin::contains));
        assertFalse(Streamer.of(combined).containsAll(origin::contains));

        assertThrows(NullPointerException.class, () -> Streamer.of(origin).containsAll((Predicate<Object>) null));
        assertThrows(NullPointerException.class, () -> Streamer.empty().containsAll((Predicate<Object>) null));
    }

    @Test
    final void contains() {
        for (final String element : combined) {
            assertEquals(origin.contains(element), Streamer.of(origin).contains(element));
        }
        assertEquals(origin.contains(null), Streamer.of(origin).contains(null));
    }

    @Test
    final void containsAny_candidates() {
        assertTrue(Streamer.of(origin).containsAny(combined::stream));
        assertFalse(Streamer.of(origin).containsAny(other::stream));

        assertFalse(Streamer.empty().containsAny(origin::stream));
        assertFalse(Streamer.empty().containsAny(Streamer.empty()));
        assertFalse(Streamer.of(origin).containsAny(Streamer.empty()));

        //noinspection DataFlowIssue
        assertThrows(NullPointerException.class, () -> Streamer.empty().containsAny((Streamable<?>) null));
    }

    @Test
    final void containsAll_candidates() {
        assertTrue(Streamer.of(combined).containsAll(List.of(combined.get(0),
                                                             combined.get(2),
                                                             combined.get(3),
                                                             combined.get(5))::stream));

        assertTrue(Streamer.of(combined).containsAll(origin::stream));
        assertEquals(combined.containsAll(origin), Streamer.of(combined).containsAll(origin::stream));

        assertTrue(Streamer.of(origin).containsAll(EMPTY_LIST::stream));
        assertEquals(origin.containsAll(EMPTY_LIST), Streamer.of(origin).containsAll(EMPTY_LIST::stream));

        assertFalse(Streamer.of(origin).containsAll(other::stream));
        assertEquals(origin.containsAll(other), Streamer.of(origin).containsAll(other::stream));

        assertFalse(Streamer.of(origin).containsAll(combined::stream));
        assertEquals(origin.containsAll(combined), Streamer.of(origin).containsAll(combined::stream));

        assertFalse(Streamer.empty().containsAll(origin::stream));
        assertTrue(Streamer.empty().containsAll(Streamer.empty()));
        assertTrue(Streamer.of(origin).containsAll(Streamer.empty()));

        //noinspection DataFlowIssue
        assertThrows(NullPointerException.class, () -> Streamer.empty().containsAll((Streamable<?>) null));
    }

    @Test
    final void forEach() {
        final List<String> result = new LinkedList<>();
        Streamer.of(origin).forEach(result::add);
        assertEquals(origin, result);
    }

    @Test
    final void forEach_null() {
        assertThrows(NullPointerException.class, () -> Streamer.of(origin).forEach(null));
        assertThrows(NullPointerException.class, () -> Streamer.empty().forEach(null));
        assertThrows(NullPointerException.class, () -> Streamer.of(EMPTY_LIST).forEach(null));
        assertThrows(NullPointerException.class, () -> Streamer.of(EMPTY_LIST).forEach(null));
    }

    @Test
    final void add() {
        assertEquals(combined.subList(0, origin.size() + 1),
                     Streamer.of(origin).add(other.get(0)).toList());
    }

    @Test
    final void add_more() {
        assertEquals(combined.subList(0, origin.size() + 4),
                     Streamer.of(origin)
                             .addAll(Streamer.of(other.get(0), other.get(1), other.get(2), other.get(3)))
                             .toList());
    }

    @Test
    final void addAll() {
        assertEquals(combined, Streamer.of(origin).addAll(other::stream).toList());
    }

    @Test
    final void addAll_self() {
        final List<String> expected = Streamable.of(origin).addAll(origin::stream).toList();
        final Streamer<String> streamer = Streamer.of(origin);
        final Streamable<String> other = streamer::stream;
        assertEquals(expected, streamer.addAll(other).toList());
    }

    @Test
    final void additional() {
        assertEquals(origin, Streamer.empty().addAll(origin::stream).toList());
    }

    @Test
    final void addAll_array() {
        final String[] array = other.toArray(String[]::new);
        assertEquals(combined, Streamer.of(origin)
                                       .addAll(Streamer.of(array))
                                       .toList());
    }

    @Test
    final void removeIf() {
        assertEquals(origin, Streamer.of(combined).removeIf(other::contains).toList());
    }

    @Test
    final void remove() {
        assertEquals(origin.stream()
                           .filter(not(e -> e.equals(origin.get(2))))
                           .toList(),
                     Streamer.of(origin)
                             .remove(origin.get(2))
                             .toList());
    }

    @Test
    final void removeAll() {
        assertEquals(origin, Streamer.of(combined)
                                     .removeAll(other::stream)
                                     .toList());
    }

    @Test
    final void removeAll_array() {
        final Object[] array = other.toArray(Object[]::new);
        assertEquals(origin, Streamer.of(combined)
                                     .removeAll(Streamer.of(array))
                                     .toList());
    }

    @Test
    final void retainIf() {
        assertEquals(origin, Streamer.of(combined).retainIf(origin::contains).toList());
    }

    @Test
    final void retainAll() {
        assertEquals(origin, Streamer.of(combined).retainAll(origin::stream).toList());
    }

    @Test
    final void retainAll_array() {
        final Object[] array = origin.toArray(Object[]::new);
        assertEquals(origin, Streamer.of(combined)
                                     .retainAll(Streamer.of(array))
                                     .toList());
    }

    @Test
    final void toSet() {
        final var expectedList = Stream.concat(origin.stream(), combined.stream()).toList();
        final var expectedSet = new HashSet<>(expectedList);
        final var streamable = Streamer.of(origin).addAll(combined::stream);
        assertEquals(expectedList, streamable.toList());
        assertEquals(expectedSet, streamable.toSet());
    }
}