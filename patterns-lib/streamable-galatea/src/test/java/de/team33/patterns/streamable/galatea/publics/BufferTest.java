package de.team33.patterns.streamable.galatea.publics;

import de.team33.patterns.arbitrary.mimas.Generator;
import de.team33.patterns.streamable.galatea.Buffer;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BufferTest {

    private static final Generator GENERATOR = Generator.of(new SecureRandom());

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
    final void add() {
        assertEquals(combined.subList(0, origin.size() + 1),
                     Buffer.of(origin).add(other.get(0)).toList());
    }

    @Test
    final void add_more() {
        assertEquals(combined.subList(0, origin.size() + 4),
                     Buffer.of(origin)
                           .addAll(Buffer.of(other.get(0), other.get(1), other.get(2), other.get(3)))
                           .toList());
    }

    @Test
    final void addAll() {
        assertEquals(combined, Buffer.of(origin).addAll(other::stream).toList());
    }

    @Test
    final void additional() {
        assertEquals(origin, Buffer.empty().addAll(origin::stream).toList());
        assertEquals(origin, Buffer.by(Buffer.empty()).addAll(origin::stream).toList());
    }

    @Test
    final void addAll_array() {
        final String[] array = other.toArray(String[]::new);
        assertEquals(combined, Buffer.of(origin)
                                     .addAll(Buffer.of(array))
                                     .toList());
    }

    @Test
    final void removeIf() {
        assertEquals(origin, Buffer.of(combined).removeIf(other::contains).toList());
    }

    @Test
    final void remove() {
        assertEquals(origin.stream()
                           .filter(not(e -> e.equals(origin.get(2))))
                           .toList(),
                     Buffer.of(origin)
                           .remove(origin.get(2))
                           .toList());
    }

    @Test
    final void removeAll() {
        assertEquals(origin, Buffer.of(combined)
                                   .removeAll(other::stream)
                                   .toList());
    }

    @Test
    final void removeAll_array() {
        final Object[] array = other.toArray(Object[]::new);
        assertEquals(origin, Buffer.of(combined)
                                   .removeAll(Buffer.of(array))
                                   .toList());
    }

    @Test
    final void retainIf() {
        assertEquals(origin, Buffer.of(combined).retainIf(origin::contains).toList());
    }

    @Test
    final void retainAll() {
        assertEquals(origin, Buffer.of(combined).retainAll(origin::stream).toList());
    }

    @Test
    final void retainAll_array() {
        final Object[] array = origin.toArray(Object[]::new);
        assertEquals(origin, Buffer.of(combined)
                                   .retainAll(Buffer.of(array))
                                   .toList());
    }

    @Test
    final void of_Iterable() {
        assertEquals(origin, Buffer.of(origin::iterator).toList());
        assertEquals(origin, Buffer.of(origin).toList());
    }
}