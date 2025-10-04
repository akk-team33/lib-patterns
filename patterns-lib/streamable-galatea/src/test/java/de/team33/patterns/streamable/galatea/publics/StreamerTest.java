package de.team33.patterns.streamable.galatea.publics;

import de.team33.patterns.arbitrary.mimas.Generator;
import de.team33.patterns.streamable.galatea.Streamer;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StreamerTest {

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
    final void concat() {
        final List<Integer> left = Stream.generate(GENERATOR::anyInt).limit(GENERATOR.anyInt(4, 8)).toList();
        final List<Double> right = Stream.generate(GENERATOR::anyDouble).limit(GENERATOR.anyInt(4, 8)).toList();
        final List<? extends Number> expected = Stream.concat(left.stream(), right.stream()).toList();
        final List<? extends Number> result = Streamer.concat(left::stream, right::stream).stream().toList();
        assertEquals(expected, result);
    }

    @Test
    final void add() {
        assertEquals(combined.subList(0, origin.size() + 1),
                     Streamer.of(origin).add(other.get(0)).stream().toList());
    }

    @Test
    final void add_more() {
        assertEquals(combined.subList(0, origin.size() + 4),
                     Streamer.of(origin)
                             .addAll(Streamer.of(other.get(0), other.get(1), other.get(2), other.get(3)))
                             .stream().toList());
    }

    @Test
    final void addAll() {
        assertEquals(combined, Streamer.of(origin).addAll(other::stream).stream().toList());
    }

    @Test
    final void additional() {
        assertEquals(origin, Streamer.empty().addAll(origin::stream).stream().toList());
        assertEquals(origin, Streamer.by(Streamer.empty()).addAll(origin::stream).stream().toList());
    }

    @Test
    final void addAll_array() {
        final String[] array = other.toArray(String[]::new);
        assertEquals(combined, Streamer.of(origin)
                                       .addAll(Streamer.of(array))
                                       .stream().toList());
    }

    @Test
    final void removeIf() {
        assertEquals(origin, Streamer.of(combined).removeIf(other::contains).stream().toList());
    }

    @Test
    final void remove() {
        assertEquals(origin.stream()
                           .filter(not(e -> e.equals(origin.get(2))))
                           .toList(),
                     Streamer.of(origin)
                             .remove(origin.get(2))
                             .stream().toList());
    }

    @Test
    final void removeAll() {
        assertEquals(origin, Streamer.of(combined)
                                     .removeAll(other::stream)
                                     .stream().toList());
    }

    @Test
    final void removeAll_array() {
        final Object[] array = other.toArray(Object[]::new);
        assertEquals(origin, Streamer.of(combined)
                                     .removeAll(Streamer.of(array))
                                     .stream().toList());
    }

    @Test
    final void retainIf() {
        assertEquals(origin, Streamer.of(combined).retainIf(origin::contains).stream().toList());
    }

    @Test
    final void retainAll() {
        assertEquals(origin, Streamer.of(combined).retainAll(origin::stream).stream().toList());
    }

    @Test
    final void retainAll_array() {
        final Object[] array = origin.toArray(Object[]::new);
        assertEquals(origin, Streamer.of(combined)
                                     .retainAll(Streamer.of(array))
                                     .stream().toList());
    }

    @Test
    final void of_Iterable() {
        //noinspection FunctionalExpressionCanBeFolded
        assertEquals(origin, Streamer.of(origin::iterator).stream().toList());
        assertEquals(origin, Streamer.of(origin).stream().toList());
    }
}