package de.team33.patterns.streamable.naiad.publics;

import de.team33.patterns.arbitrary.mimas.Generator;
import de.team33.patterns.streamable.naiad.Streamable;
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

class StreamableTest {

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
    final void of_Iterable() {
        //noinspection FunctionalExpressionCanBeFolded
        assertEquals(origin, Streamable.of(origin::iterator).toList());
        assertEquals(origin, Streamable.of(origin).toList());
    }

    @Test
    final void isEmpty() {
        assertTrue(Streamable.empty().isEmpty());
        assertTrue(Streamable.of(EMPTY_LIST).isEmpty());
        assertEquals(EMPTY_LIST.isEmpty(), Streamable.of(EMPTY_LIST).isEmpty());
        assertFalse(Streamable.of(origin).isEmpty());
        assertEquals(origin.isEmpty(), Streamable.of(origin).isEmpty());
    }

    @Test
    final void containsAny() {
        assertTrue(Streamable.of(List.of(GENERATOR.anyString())).containsAny());
        assertTrue(Streamable.of(origin).containsAny());
        assertEquals(0 < origin.size(), Streamable.of(origin).containsAny());
        assertFalse(Streamable.of(EMPTY_LIST).containsAny());
        assertEquals(0 < EMPTY_LIST.size(), Streamable.of(EMPTY_LIST).containsAny());
    }

    @Test
    final void containsAny_withPredicate() {
        for (final String element : combined) {
            assertEquals(origin.contains(element),
                         Streamable.of(origin).containsAny(item -> Objects.equals(item, element)));
        }

        assertThrows(NullPointerException.class, () -> Streamable.of(origin).containsAny((Predicate<Object>) null));
        assertThrows(NullPointerException.class, () -> Streamable.empty().containsAny((Predicate<Object>) null));
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @Test
    final void containsAll_withPredicate() {
        assertTrue(Streamable.of(combined).containsAll(combined::contains));
        assertTrue(Streamable.of(origin).containsAll(combined::contains));
        assertTrue(Streamable.empty().containsAll(origin::contains));
        assertFalse(Streamable.of(combined).containsAll(origin::contains));

        assertThrows(NullPointerException.class, () -> Streamable.of(origin).containsAll((Predicate<Object>) null));
        assertThrows(NullPointerException.class, () -> Streamable.empty().containsAll((Predicate<Object>) null));
    }

    @Test
    final void contains() {
        for (final String element : combined) {
            assertEquals(origin.contains(element), Streamable.of(origin).contains(element));
        }
        assertEquals(origin.contains(null), Streamable.of(origin).contains(null));
    }

    @Test
    final void containsAny_candidates() {
        assertTrue(Streamable.of(origin).containsAny(combined::stream));
        assertFalse(Streamable.of(origin).containsAny(other::stream));

        assertFalse(Streamable.empty().containsAny(origin::stream));
        assertFalse(Streamable.empty().containsAny(Streamable.empty()));
        assertFalse(Streamable.of(origin).containsAny(Streamable.empty()));

        //noinspection DataFlowIssue
        assertThrows(NullPointerException.class, () -> Streamable.empty().containsAny((Streamable<?>) null));
    }

    @Test
    final void containsAll_candidates() {
        assertTrue(Streamable.of(combined).containsAll(List.of(combined.get(0),
                                                               combined.get(2),
                                                               combined.get(3),
                                                               combined.get(5))::stream));

        assertTrue(Streamable.of(combined).containsAll(origin::stream));
        assertEquals(combined.containsAll(origin), Streamable.of(combined).containsAll(origin::stream));

        assertTrue(Streamable.of(origin).containsAll(EMPTY_LIST::stream));
        assertEquals(origin.containsAll(EMPTY_LIST), Streamable.of(origin).containsAll(EMPTY_LIST::stream));

        assertFalse(Streamable.of(origin).containsAll(other::stream));
        assertEquals(origin.containsAll(other), Streamable.of(origin).containsAll(other::stream));

        assertFalse(Streamable.of(origin).containsAll(combined::stream));
        assertEquals(origin.containsAll(combined), Streamable.of(origin).containsAll(combined::stream));

        assertFalse(Streamable.empty().containsAll(origin::stream));
        assertTrue(Streamable.empty().containsAll(Streamable.empty()));
        assertTrue(Streamable.of(origin).containsAll(Streamable.empty()));

        //noinspection DataFlowIssue
        assertThrows(NullPointerException.class, () -> Streamable.empty().containsAll((Streamable<?>) null));
    }

    @Test
    final void forEach() {
        final List<String> result = new LinkedList<>();
        Streamable.of(origin).forEach(result::add);
        assertEquals(origin, result);
    }

    @Test
    final void forEach_null() {
        assertThrows(NullPointerException.class, () -> Streamable.of(origin).forEach(null));
        assertThrows(NullPointerException.class, () -> Streamable.empty().forEach(null));
        assertThrows(NullPointerException.class, () -> Streamable.of(EMPTY_LIST).forEach(null));
        assertThrows(NullPointerException.class, () -> Streamable.of(EMPTY_LIST).forEach(null));
    }

    @Test
    final void add() {
        assertEquals(combined.subList(0, origin.size() + 1),
                     Streamable.of(origin).add(other.get(0)).toList());
    }

    @Test
    final void add_more() {
        assertEquals(combined.subList(0, origin.size() + 4),
                     Streamable.of(origin)
                               .addAll(Streamable.of(other.get(0), other.get(1), other.get(2), other.get(3)))
                               .toList());
    }

    @Test
    final void addAll() {
        assertEquals(combined, Streamable.of(origin).addAll(other::stream).toList());
    }

    @Test
    final void additional() {
        assertEquals(origin, Streamable.empty().addAll(origin::stream).toList());
    }

    @Test
    final void addAll_array() {
        final String[] array = other.toArray(String[]::new);
        assertEquals(combined, Streamable.of(origin)
                                         .addAll(Streamable.of(array))
                                         .toList());
    }

    @Test
    final void removeIf() {
        assertEquals(origin, Streamable.of(combined).removeIf(other::contains).toList());
    }

    @Test
    final void remove() {
        assertEquals(origin.stream()
                           .filter(not(e -> e.equals(origin.get(2))))
                           .toList(),
                     Streamable.of(origin)
                               .remove(origin.get(2))
                               .toList());
    }

    @Test
    final void removeAll() {
        assertEquals(origin, Streamable.of(combined)
                                       .removeAll(other::stream)
                                       .toList());
    }

    @Test
    final void removeAll_array() {
        final Object[] array = other.toArray(Object[]::new);
        assertEquals(origin, Streamable.of(combined)
                                       .removeAll(Streamable.of(array))
                                       .toList());
    }

    @Test
    final void retainIf() {
        assertEquals(origin, Streamable.of(combined).retainIf(origin::contains).toList());
    }

    @Test
    final void retainAll() {
        assertEquals(origin, Streamable.of(combined).retainAll(origin::stream).toList());
    }

    @Test
    final void retainAll_array() {
        final Object[] array = origin.toArray(Object[]::new);
        assertEquals(origin, Streamable.of(combined)
                                       .retainAll(Streamable.of(array))
                                       .toList());
    }

    @Test
    final void map() {
        final Streamable<String> streamable = Streamable.of(origin);
        final var expected = streamable.toList();
        final var result = streamable.map(Streamable::toList);
        assertEquals(expected, result);
    }

    @Test
    final void toSet() {
        final var expectedList = Stream.concat(origin.stream(), combined.stream()).toList();
        final var expectedSet = new HashSet<>(expectedList);
        final var streamable = Streamable.of(origin).addAll(combined::stream);
        assertEquals(expectedList, streamable.toList());
        assertEquals(expectedSet, streamable.toSet());
    }
}