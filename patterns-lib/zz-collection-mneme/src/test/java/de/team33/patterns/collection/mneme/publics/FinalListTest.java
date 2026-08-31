package de.team33.patterns.collection.mneme.publics;

import de.team33.patterns.arbitrary.mimas.Generator;
import de.team33.patterns.collection.mneme.FinalList;
import de.team33.patterns.streamable.naiad.Streamer;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FinalListTest {

    private static final Generator GENERATOR = Generator.of(new SecureRandom());

    @Test
    final void empty() {
        assertTrue(FinalList.empty().isEmpty());
    }

    @Test
    final void tryBuild_map_reduce() {
        final List<Integer> expected = Stream.generate(() -> GENERATOR.anyInt(0, 10))
                                             .limit(20000)
                                             .toList();
        final FinalList<Integer> result = expected.stream()
                                                  .map(Streamer::of)
                                                  .reduce(Streamer.empty(), Streamer::addAll)
                                                  .map(FinalList::of);
        assertEquals(expected, result);
    }

    @Test
    final void tryBuild_collect() {
        final List<Integer> expected = Stream.generate(() -> GENERATOR.anyInt(0, 10))
                                             .limit(20000)
                                             .toList();
        final FinalList<Integer> result = expected.stream()
                                                  .collect(Streamer::<Integer>empty, Streamer::add, Streamer::addAll)
                                                  .map(FinalList::of);
        assertEquals(expected, result);
    }

    @Test
    final void of_nullable() {
        final List<Integer> source = Arrays.asList(1, 2, 3, null, 2, 3, 4, null, 3, 4, 5);
        final FinalList<Comparable<?>> result = FinalList.of(source);
        assertTrue(result.contains(null));
        assertEquals(source, result);
    }

    @Test
    final void of() {
        final List<Integer> expected = Stream.generate(() -> GENERATOR.anyInt(0, 10))
                                             .limit(20)
                                             .toList();

        final FinalList<Number> result = FinalList.of(expected);
        assertEquals(expected, result);
    }
}