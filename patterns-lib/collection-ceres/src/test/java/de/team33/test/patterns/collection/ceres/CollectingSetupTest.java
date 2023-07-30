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

    @SuppressWarnings("Convert2MethodRef")
    @Test
    final void add_single() {
        final List<String> expected = SUPPLY.nextStringList(3);
        final List<String> result = Collecting.builder(() -> new ArrayList<String>())
                                              .add(expected.get(0))
                                              .add(expected.get(1))
                                              .add(expected.get(2))
                                              .build();
        assertEquals(expected, result);
    }

    @SuppressWarnings("Convert2MethodRef")
    @Test
    final void add_more() {
        final List<String> expected = SUPPLY.nextStringList(9);
        final List<String> result = Collecting.builder(() -> new ArrayList<String>())
                                              .add(expected.get(0), expected.get(1))
                                              .add(expected.get(2), expected.get(3), expected.get(4))
                                              .add(expected.get(5), expected.get(6), expected.get(7), expected.get(8))
                                              .build();
        assertEquals(expected, result);
    }

    @SuppressWarnings("Convert2MethodRef")
    @Test
    final void addAll() {
        final List<String> expected = SUPPLY.nextStringList(20);
        final Collection<String> head = expected.subList(0, 4);
        final Stream<String> stream = expected.stream().skip(4).limit(4);
        final Iterable<String> iterable1 = expected.subList(8, 12);
        final Iterable<String> iterable2 = () -> expected.subList(12, 16).iterator();
        final String[] array = expected.subList(16, 20).toArray(new String[4]);
        final List<String> result = Collecting.builder(() -> new ArrayList<String>())
                                              .addAll(head)
                                              .addAll(stream)
                                              .addAll(iterable1)
                                              .addAll(iterable2)
                                              .addAll(array)
                                              .build();
        assertEquals(expected, result);
    }
}
