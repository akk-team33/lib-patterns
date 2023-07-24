package de.team33.test.patterns.collection.ceres;

import de.team33.patterns.collection.ceres.Collecting;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CollectingSetupTest {

    private static final Supply SUPPLY = new Supply();

    @Test
    void add_single() {
        final List<String> expected = SUPPLY.nextStringList(3);
        final List<String> result = Collecting.builder(() -> new ArrayList<String>())
                                              .add(expected.get(0))
                                              .add(expected.get(1))
                                              .add(expected.get(2))
                                              .build();
        assertEquals(expected, result);
    }

    @Test
    void add_more() {
        final List<String> expected = SUPPLY.nextStringList(5);
        final List<String> result = Collecting.builder(() -> new ArrayList<String>())
                                              .add(expected.get(0), expected.get(1))
                                              .add(expected.get(2), expected.get(3), expected.get(4))
                                              .build();
        assertEquals(expected, result);
    }

    @Test
    void addAll() {
        final List<String> expected = SUPPLY.nextStringList(16);
        final Collection<String> head = expected.subList(0, 4);
        final Stream<String> stream = expected.stream().skip(4).limit(4);
        final Iterable<String> iterable = () -> expected.subList(8, 12).iterator();
        final String[] array = expected.subList(12, 16).toArray(new String[4]);
        final List<String> result = Collecting.builder(() -> new ArrayList<String>())
                                              .addAll(head)
                                              .addAll(stream)
                                              .addAll(iterable)
                                              .addAll(array)
                                              .build();
        assertEquals(expected, result);
    }
}
