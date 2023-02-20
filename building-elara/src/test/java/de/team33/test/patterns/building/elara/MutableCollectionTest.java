package de.team33.test.patterns.building.elara;

import de.team33.sample.patterns.building.elara.MutableCollection;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MutableCollectionTest {

    @Test
    void identity() {
        final List<String> original = new ArrayList<>();
        final List<String> result = MutableCollection.builder(original)
                                                     .build();
        assertSame(original, result);
    }

    @Test
    void add() {
        final List<String> expected = Arrays.asList("a", "b", "c");
        final List<String> result = MutableCollection.builder(new ArrayList<String>())
                                                     .add(expected.get(0))
                                                     .add(expected.get(1))
                                                     .add(expected.get(2))
                                                     .build();
        assertEquals(expected, result);
    }

    @Test
    void remove() {
        final List<String> unexpected = Arrays.asList("a", "b", "c");
        final List<String> result = MutableCollection.builder(new ArrayList<String>())
                                                     .addAll(unexpected)
                                                     .remove(unexpected.get(0))
                                                     .remove(unexpected.get(1))
                                                     .remove(unexpected.get(2))
                                                     .build();
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void addAll() {
        final List<String> expected = Arrays.asList("a", "b", "c");
        final List<String> result = MutableCollection.builder(new ArrayList<String>())
                                                     .addAll(expected)
                                                     .build();
        assertEquals(expected, result);
    }

    @Test
    void removeAll() {
        final List<String> unexpected = Arrays.asList("a", "b", "c");
        final List<String> result = MutableCollection.builder(new ArrayList<String>())
                                                     .addAll(unexpected)
                                                     .addAll(unexpected)
                                                     .removeAll(unexpected)
                                                     .build();
        assertEquals(Collections.emptyList(), result);
    }
}
