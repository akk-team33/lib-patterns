package de.team33.patterns.collection.mneme.publics;

import de.team33.patterns.arbitrary.mimas.Generator;
import de.team33.patterns.collection.mneme.FinalSet;
import de.team33.patterns.streamable.naiad.Streamable;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class FinalSet2Test {

    private static final Generator GENERATOR = Generator.of(new SecureRandom());

    @Test
    final void empty() {
        assertEquals(Set.of(), FinalSet.empty());
    }

    @Test
    final void of_single() {
        final List<String> expected = Stream.generate(GENERATOR::anyString).limit(1).toList();
        final FinalSet<String> result = FinalSet.of(expected.get(0));
        assertEquals(expected, result.stream().toList());
    }

    @Test
    final void of_two() {
        final List<String> expected = Stream.generate(GENERATOR::anyString).distinct().limit(2).toList();
        final FinalSet<String> result = FinalSet.of(expected.get(0), expected.get(1));
        assertEquals(expected, result.stream().toList());
    }

    @Test
    final void of_three() {
        final List<String> expected = Stream.generate(GENERATOR::anyString).distinct().limit(3).toList();
        final FinalSet<String> result = FinalSet.of(expected.get(0), expected.get(1), expected.get(2));
        assertEquals(expected, result.stream().toList());
    }

    @Test
    final void of_more() {
        final List<String> expected = Stream.generate(GENERATOR::anyString).distinct().limit(5).toList();
        final FinalSet<String> result = FinalSet.of(expected.get(0),
                                                    expected.get(1),
                                                    expected.get(2),
                                                    expected.get(3),
                                                    expected.get(4));
        assertEquals(expected, result.stream().toList());
    }

    @Test
    final void of_array() {
        final String[] elements = Stream.generate(GENERATOR::anyString)
                                        .distinct()
                                        .limit(GENERATOR.anyInt(10))
                                        .toArray(String[]::new);
        final List<String> expected = List.of(elements);
        final FinalSet<String> result = FinalSet.of(elements);
        assertEquals(expected, result.stream().toList());
    }

    @Test
    final void of_collection() {
        final List<String> expected = Stream.generate(GENERATOR::anyString)
                                            .distinct()
                                            .limit(GENERATOR.anyInt(10))
                                            .toList();
        final FinalSet<CharSequence> result = FinalSet.of(expected);
        assertEquals(expected, result.stream().toList());
    }

    @Test
    final void of_streamable() {
        final List<String> expected = Stream.generate(GENERATOR::anyString)
                                            .distinct()
                                            .limit(GENERATOR.anyInt(10))
                                            .toList();
        final Streamable<String> stage = expected::stream;
        final FinalSet<CharSequence> result = FinalSet.of(stage);
        assertEquals(expected, result.stream().toList());
    }

    @Test
    final void of_nullable_distinct() {
        final List<Integer> origin = Arrays.asList(1, 2, 3, null, 2, 3, 4, null, 3, 4, 5);
        final Set<Integer> expected = new LinkedHashSet<>(origin);
        final FinalSet<Number> result = FinalSet.of(origin);
        assertEquals(expected, result);
        assertTrue(result.contains(null));
    }

    @Test
    final void immutable() {
        final FinalSet<String> sample = FinalSet.of("1", "2", "3");
        assertThrows(UnsupportedOperationException.class, () -> sample.add("4"));
        assertThrows(UnsupportedOperationException.class, () -> sample.remove("2"));
        assertThrows(UnsupportedOperationException.class, () -> {
            final Iterator<String> iterator = sample.iterator();
            iterator.next();
            iterator.remove();
        });
        assertThrows(UnsupportedOperationException.class, sample::clear);
    }
}