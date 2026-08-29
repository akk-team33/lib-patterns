package de.team33.patterns.collection.mneme.publics;

import de.team33.patterns.arbitrary.mimas.Generator;
import de.team33.patterns.collection.mneme.StatedSet;
import de.team33.patterns.streamable.naiad.Streamer;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StatedSetTest {

    private static final Generator GENERATOR = Generator.of(new SecureRandom());

    @Test
    final void empty() {
        assertTrue(new StatedSet().isEmpty());
    }

    @Test
    final void tryBuild_map_reduce() {
        final List<Integer> source = Stream.generate(() -> GENERATOR.anyInt(0, 10))
                                           .limit(200000)
                                           .toList();
        final Set<Integer> expected = new LinkedHashSet<>(source);
        final StatedSet<Integer> result = source.stream()
                                                .map(Streamer::of)
                                                .reduce(Streamer.empty(), Streamer::addAll)
                                                .map(StatedSet::new);
        assertEquals(List.copyOf(expected), List.copyOf(result));
    }

    @Test
    final void tryBuild_collect() {
        final List<Integer> source = Stream.generate(() -> GENERATOR.anyInt(0, 10))
                                           .limit(200000)
                                           .toList();
        final Set<Integer> expected = new LinkedHashSet<>(source);
        final StatedSet<Integer> result = source.stream()
                                                .collect(Streamer::<Integer>empty, Streamer::add, Streamer::addAll)
                                                .map(StatedSet::new);
        assertEquals(List.copyOf(expected), List.copyOf(result));
    }

    @Test
    final void of_null() {
        final List<Integer> source = Arrays.asList(1, 2, 3, null, 2, 3, 4, null, 3, 4, 5);
        final StatedSet<Integer> result = new StatedSet<>(source::stream);
        assertTrue(result.contains(null));
    }

    @Test
    final void of() {
        final List<Integer> source = Stream.generate(() -> GENERATOR.anyInt(0, 10))
                                           .limit(20)
                                           .toList();
        final Set<Integer> expected = new LinkedHashSet<>(source);
        assert expected.size() <= 10 : "Expected max. 10 elements - but was %d".formatted(expected.size());

        final StatedSet<Integer> result = new StatedSet<>(source::stream);
        assertEquals(List.copyOf(expected), List.copyOf(result));
    }
}