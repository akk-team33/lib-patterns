package de.team33.patterns.streamable.galatea.publics;

import de.team33.patterns.arbitrary.mimas.Generator;
import de.team33.patterns.streamable.galatea.Buffer;
import de.team33.patterns.streamable.galatea.Streamable;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;
import static org.junit.jupiter.api.Assertions.*;

class BufferAsStreamableTest {

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
        assertEquals(origin, Buffer.of(origin::iterator).toList());
        assertEquals(origin, Buffer.of(origin).toList());
    }

    @Test
    final void isEmpty() {
        assertTrue(Buffer.empty().isEmpty());
        assertTrue(Buffer.of(EMPTY_LIST).isEmpty());
        assertEquals(EMPTY_LIST.isEmpty(), Buffer.of(EMPTY_LIST).isEmpty());
        assertFalse(Buffer.of(origin).isEmpty());
        assertEquals(origin.isEmpty(), Buffer.of(origin).isEmpty());
    }

    @Test
    final void containsAny() {
        assertTrue(Buffer.of(List.of(GENERATOR.anyString())).containsAny());
        assertTrue(Buffer.of(origin).containsAny());
        assertEquals(0 < origin.size(), Buffer.of(origin).containsAny());
        assertFalse(Buffer.of(EMPTY_LIST).containsAny());
        assertEquals(0 < EMPTY_LIST.size(), Buffer.of(EMPTY_LIST).containsAny());
    }

    @Test
    final void containsAny_withPredicate() {
        for (final String element : combined) {
            assertEquals(origin.contains(element),
                         Buffer.of(origin).containsAny(item -> Objects.equals(item, element)));
        }

        assertThrows(NullPointerException.class, () -> Buffer.of(origin).containsAny((Predicate<Object>) null));
        assertThrows(NullPointerException.class, () -> Buffer.empty().containsAny((Predicate<Object>) null));
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @Test
    final void containsAll_withPredicate() {
        assertTrue(Buffer.of(combined).containsAll(combined::contains));
        assertTrue(Buffer.of(origin).containsAll(combined::contains));
        assertTrue(Buffer.empty().containsAll(origin::contains));
        assertFalse(Buffer.of(combined).containsAll(origin::contains));

        assertThrows(NullPointerException.class, () -> Buffer.of(origin).containsAll((Predicate<Object>) null));
        assertThrows(NullPointerException.class, () -> Buffer.empty().containsAll((Predicate<Object>) null));
    }

    @Test
    final void contains() {
        for (final String element : combined) {
            assertEquals(origin.contains(element), Buffer.of(origin).contains(element));
        }
        assertEquals(origin.contains(null), Buffer.of(origin).contains(null));
    }

    @Test
    final void containsAny_candidates() {
        assertTrue(Buffer.of(origin).containsAny(combined::stream));
        assertFalse(Buffer.of(origin).containsAny(other::stream));

        assertFalse(Buffer.empty().containsAny(origin::stream));
        assertFalse(Buffer.empty().containsAny(Buffer.empty()));
        assertFalse(Buffer.of(origin).containsAny(Buffer.empty()));

        //noinspection DataFlowIssue
        assertThrows(NullPointerException.class, () -> Buffer.empty().containsAny((Streamable<?>) null));
    }

    @Test
    final void containsAll_candidates() {
        assertTrue(Buffer.of(combined).containsAll(List.of(combined.get(0),
                                                           combined.get(2),
                                                           combined.get(3),
                                                           combined.get(5))::stream));

        assertTrue(Buffer.of(combined).containsAll(origin::stream));
        assertEquals(combined.containsAll(origin), Buffer.of(combined).containsAll(origin::stream));

        assertTrue(Buffer.of(origin).containsAll(EMPTY_LIST::stream));
        assertEquals(origin.containsAll(EMPTY_LIST), Buffer.of(origin).containsAll(EMPTY_LIST::stream));

        assertFalse(Buffer.of(origin).containsAll(other::stream));
        assertEquals(origin.containsAll(other), Buffer.of(origin).containsAll(other::stream));

        assertFalse(Buffer.of(origin).containsAll(combined::stream));
        assertEquals(origin.containsAll(combined), Buffer.of(origin).containsAll(combined::stream));

        assertFalse(Buffer.empty().containsAll(origin::stream));
        assertTrue(Buffer.empty().containsAll(Buffer.empty()));
        assertTrue(Buffer.of(origin).containsAll(Buffer.empty()));

        //noinspection DataFlowIssue
        assertThrows(NullPointerException.class, () -> Buffer.empty().containsAll((Streamable<?>) null));
    }

    @Test
    final void forEach() {
        final List<String> result = new LinkedList<>();
        Buffer.of(origin).forEach(result::add);
        assertEquals(origin, result);
    }

    @Test
    final void forEach_null() {
        assertThrows(NullPointerException.class, () -> Buffer.of(origin).forEach(null));
        assertThrows(NullPointerException.class, () -> Buffer.empty().forEach(null));
        assertThrows(NullPointerException.class, () -> Buffer.of(EMPTY_LIST).forEach(null));
        assertThrows(NullPointerException.class, () -> Buffer.of(EMPTY_LIST).forEach(null));
    }

    @Test
    final void cast() {
        final Streamable<CharSequence> streamable = Streamable.cast(Buffer.by(origin::stream));
        assertEquals(origin, streamable.toList());
    }

    @Test
    final void toSet() {
        final List<String> expectedList = Stream.concat(origin.stream(), combined.stream()).toList();
        final Set<String> expectedSet = new HashSet<>(expectedList);
        final Buffer<String> buffer = Buffer.by(origin::stream).addAll(combined::stream);
        assertEquals(expectedList, buffer.toList());
        assertEquals(expectedSet, buffer.toSet());
    }
}