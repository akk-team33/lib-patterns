package de.team33.patterns.streamable.galatea.publics;

import de.team33.patterns.arbitrary.mimas.Generator;
import de.team33.patterns.streamable.galatea.Streamable;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
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
        assertEquals(origin, Streamable.of(origin::iterator).stream().toList());
        assertEquals(origin, Streamable.of(origin).stream().toList());
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
}