package de.team33.patterns.collection.mneme.publics;

import de.team33.patterns.arbitrary.mimas.Generator;
import de.team33.patterns.collection.mneme.FinalList;
import de.team33.patterns.streamable.naiad.Streamable;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FinalListTest {

    private static final Generator GENERATOR = Generator.of(new SecureRandom());

    @Test
    final void empty() {
        assertEquals(List.of(), FinalList.empty());
    }

    @Test
    final void of_single() {
        final List<String> expected = Stream.generate(GENERATOR::anyString).limit(1).toList();
        final FinalList<String> result = FinalList.of(expected.get(0));
        assertEquals(expected, result);
    }

    @Test
    final void of_two() {
        final List<String> expected = Stream.generate(GENERATOR::anyString).limit(2).toList();
        final FinalList<String> result = FinalList.of(expected.get(0), expected.get(1));
        assertEquals(expected, result);
    }

    @Test
    final void of_three() {
        final List<String> expected = Stream.generate(GENERATOR::anyString).limit(3).toList();
        final FinalList<String> result = FinalList.of(expected.get(0), expected.get(1), expected.get(2));
        assertEquals(expected, result);
    }

    @Test
    final void of_more() {
        final List<String> expected = Stream.generate(GENERATOR::anyString).limit(5).toList();
        final FinalList<String> result = FinalList.of(expected.get(0),
                                                      expected.get(1),
                                                      expected.get(2),
                                                      expected.get(3),
                                                      expected.get(4));
        assertEquals(expected, result);
    }

    @Test
    final void of_array() {
        final String[] elements = Stream.generate(GENERATOR::anyString).limit(4).toArray(String[]::new);
        final List<String> expected = List.of(elements);
        final FinalList<String> result = FinalList.of(elements);
        assertEquals(expected, result);
    }

    @Test
    final void of_collection() {
        final List<String> expected = Stream.generate(GENERATOR::anyString)
                                            .limit(GENERATOR.anyInt(10))
                                            .toList();
        final FinalList<CharSequence> result = FinalList.of(expected);
        assertEquals(expected, result);
    }

    @Test
    final void of_streamable() {
        final List<String> expected = Stream.generate(GENERATOR::anyString)
                                            .limit(GENERATOR.anyInt(10))
                                            .toList();
        final Streamable<String> stage = expected::stream;
        final FinalList<CharSequence> result = FinalList.of(stage);
        assertEquals(expected, result);
    }

    @Test
    final void of_nullable() {
        final List<Integer> expected = Arrays.asList(1, 2, 3, null, 2, 3, 4, null, 3, 4, 5);
        final FinalList<Number> result = FinalList.of(expected);
        assertEquals(expected, result);
    }

    @Test
    final void immutable() {
        final FinalList<String> sample = FinalList.of("1", "2", "3");
        assertThrows(UnsupportedOperationException.class, () -> sample.add("4"));
        assertThrows(UnsupportedOperationException.class, () -> sample.remove(1));
        assertThrows(UnsupportedOperationException.class, () -> sample.remove("2"));
        assertThrows(UnsupportedOperationException.class, () -> {
            final Iterator<String> iterator = sample.iterator();
            iterator.next();
            iterator.remove();
        });
        assertThrows(UnsupportedOperationException.class, () -> sample.sort(String::compareTo));
        assertThrows(UnsupportedOperationException.class, sample::clear);
    }
}