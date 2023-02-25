package de.team33.test.patterns.building.elara;

import de.team33.sample.patterns.building.elara.Collecting;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class CollectingTest {

    @Test
    void identity() {
        final List<String> original = new ArrayList<>();
        final List<String> result = Collecting.reBuilder(original)
                                              .build();
        assertSame(original, result);
    }

    @Test
    void add() {
        final List<String> expected = Arrays.asList("a", "b", "c");
        final List<String> result = Collecting.reBuilder(new ArrayList<String>())
                                              .add(expected.get(0))
                                              .add(expected.get(1))
                                              .add(expected.get(2))
                                              .build();
        assertEquals(expected, result);
    }

    @Test
    void addAll() {
        final List<String> expected = Arrays.asList("a", "b", "c");
        final List<String> result = Collecting.reBuilder(new ArrayList<String>())
                                              .addAll(expected)
                                              .build();
        assertEquals(expected, result);
    }
}
