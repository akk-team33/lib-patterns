package de.team33.patterns.streamable.galatea.publics;

import de.team33.patterns.arbitrary.mimas.Generator;
import de.team33.patterns.streamable.galatea.Streamable;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.Collection;
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

    private static <E> Streamable<E> streamable(final Collection<E> collection) {
        return collection::stream;
    }

    @Test
    final void concat() {
        final List<Integer> left = Stream.generate(GENERATOR::anyInt).limit(GENERATOR.anyInt(4, 8)).toList();
        final List<Double> right = Stream.generate(GENERATOR::anyDouble).limit(GENERATOR.anyInt(4, 8)).toList();
        final List<? extends Number> expected = Stream.concat(left.stream(), right.stream()).toList();
        final List<? extends Number> result = Streamable.concat(left::stream, right::stream).stream().toList();
        assertEquals(expected, result);
    }

    @Test
    final void add() {
        assertEquals(combined.subList(0, origin.size() + 1),
                     Streamable.of(origin).add(other.get(0)).stream().toList());
    }

    @Test
    final void add_more() {
        assertEquals(combined.subList(0, origin.size() + 4),
                     Streamable.of(origin)
                               .add(other.get(0), other.get(1), other.get(2), other.get(3))
                               .stream().toList());
    }

    @Test
    final void addAll() {
        assertEquals(combined, Streamable.of(origin).addAll(other::stream).stream().toList());
    }

    @Test
    final void addAll_array() {
        final String[] array = other.toArray(String[]::new);
        assertEquals(combined, Streamable.of(origin).addAll(array).stream().toList());
    }

    @Test
    final void of_Iterable() {
        //noinspection FunctionalExpressionCanBeFolded
        assertEquals(origin, Streamable.of(origin::iterator).stream().toList());
        assertEquals(origin, Streamable.of(origin).stream().toList());
    }

    @Test
    final void isEmpty() {
        assertTrue(Streamable.empty().isEmpty());
        assertTrue(streamable(EMPTY_LIST).isEmpty());
        assertEquals(EMPTY_LIST.isEmpty(), streamable(EMPTY_LIST).isEmpty());
        assertFalse(streamable(origin).isEmpty());
        assertEquals(origin.isEmpty(), streamable(origin).isEmpty());
    }

    @Test
    final void containsAny() {
        assertTrue(Streamable.of(List.of(GENERATOR.anyString())).containsAny());
        assertTrue(streamable(origin).containsAny());
        assertEquals(0 < origin.size(), streamable(origin).containsAny());
        assertFalse(streamable(EMPTY_LIST).containsAny());
        assertEquals(0 < EMPTY_LIST.size(), streamable(EMPTY_LIST).containsAny());
    }

    @Test
    final void containsAny_withPredicate() {
        for (final String element : combined) {
            assertEquals(origin.contains(element),
                         streamable(origin).containsAny(item -> Objects.equals(item, element)));
        }

        assertThrows(NullPointerException.class, () -> streamable(origin).containsAny((Predicate<Object>) null));
        assertThrows(NullPointerException.class, () -> Streamable.empty().containsAny((Predicate<Object>) null));
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @Test
    final void containsAll_withPredicate() {
        assertTrue(streamable(combined).containsAll(combined::contains));
        assertTrue(streamable(origin).containsAll(combined::contains));
        assertTrue(Streamable.empty().containsAll(origin::contains));
        assertFalse(streamable(combined).containsAll(origin::contains));

        assertThrows(NullPointerException.class, () -> streamable(origin).containsAll((Predicate<Object>) null));
        assertThrows(NullPointerException.class, () -> Streamable.empty().containsAll((Predicate<Object>) null));
    }

    @Test
    final void contains() {
        for (final String element : combined) {
            assertEquals(origin.contains(element), streamable(origin).contains(element));
        }
        assertEquals(origin.contains(null), streamable(origin).contains(null));
    }

    @Test
    final void containsAny_candidates() {
        assertTrue(streamable(origin).containsAny(combined::stream));
        assertFalse(streamable(origin).containsAny(other::stream));

        assertFalse(Streamable.empty().containsAny(origin::stream));
        assertFalse(Streamable.empty().containsAny(Streamable.empty()));
        assertFalse(streamable(origin).containsAny(Streamable.empty()));

        //noinspection DataFlowIssue
        assertThrows(NullPointerException.class, () -> Streamable.empty().containsAny((Streamable<?>) null));
    }

    @Test
    final void containsAll_candidates() {
        assertTrue(streamable(combined).containsAll(List.of(combined.get(0),
                                                            combined.get(2),
                                                            combined.get(3),
                                                            combined.get(5))::stream));

        assertTrue(streamable(combined).containsAll(origin::stream));
        assertEquals(combined.containsAll(origin), streamable(combined).containsAll(origin::stream));

        assertTrue(streamable(origin).containsAll(EMPTY_LIST::stream));
        assertEquals(origin.containsAll(EMPTY_LIST), streamable(origin).containsAll(EMPTY_LIST::stream));

        assertFalse(streamable(origin).containsAll(other::stream));
        assertEquals(origin.containsAll(other), streamable(origin).containsAll(other::stream));

        assertFalse(streamable(origin).containsAll(combined::stream));
        assertEquals(origin.containsAll(combined), streamable(origin).containsAll(combined::stream));

        assertFalse(Streamable.empty().containsAll(origin::stream));
        assertTrue(Streamable.empty().containsAll(Streamable.empty()));
        assertTrue(streamable(origin).containsAll(Streamable.empty()));

        //noinspection DataFlowIssue
        assertThrows(NullPointerException.class, () -> Streamable.empty().containsAll((Streamable<?>) null));
    }

    @Test
    final void forEach() {
        final List<String> result = new LinkedList<>();
        streamable(origin).forEach(result::add);
        assertEquals(origin, result);
    }

    @Test
    final void forEach_null() {
        assertThrows(NullPointerException.class, () -> streamable(origin).forEach(null));
        assertThrows(NullPointerException.class, () -> Streamable.empty().forEach(null));
        assertThrows(NullPointerException.class, () -> streamable(EMPTY_LIST).forEach(null));
        assertThrows(NullPointerException.class, () -> Streamable.of(EMPTY_LIST).forEach(null));
    }
}