package de.team33.test.patterns.building.elara;

import de.team33.sample.patterns.building.elara.Collecting;
import de.team33.sample.patterns.building.elara.Supply;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

class CollectingTest {

    private static final Supply SUPPLY = new Supply();

    @Test
    final void identity_Charger() {
        final Collecting.Charger<String, List<String>, ?> charger = Collecting.charger(SUPPLY.nextStringList(3));
        final List<String> original = charger.release();
        final List<String> result = charger.release();
        assertEquals(original, result);
        assertSame(original, result);
    }

    @Test
    final void identity_Builder() {
        final List<String> template = SUPPLY.nextStringList(3);
        final Collecting.Builder<String, List<String>, ?> builder = Collecting.builder(() -> new ArrayList<>(template));
        final List<String> original = builder.build();
        final List<String> result = builder.build();
        assertEquals(original, result);
        assertNotSame(original, result);
    }

    @Test
    final void add_Charger() {
        final List<String> expected = SUPPLY.nextStringList(3);
        final List<String> result = Collecting.charger(new ArrayList<String>())
                                              .add(expected.get(0))
                                              .add(expected.get(1))
                                              .add(expected.get(2))
                                              .release();
        assertEquals(expected, result);
    }

    @Test
    final void add_Builder() {
        final List<String> expected = SUPPLY.nextStringList(3);
        final List<String> result = Collecting.builder(() -> new ArrayList<String>())
                                              .add(expected.get(0))
                                              .add(expected.get(1))
                                              .add(expected.get(2))
                                              .build();
        assertEquals(expected, result);
    }

    @Test
    final void addAll_Charger() {
        final List<String> expected = SUPPLY.nextStringList(3);
        final List<String> result = Collecting.charger(new ArrayList<String>())
                                              .addAll(expected)
                                              .release();
        assertEquals(expected, result);
    }

    @Test
    final void addAll_Builder() {
        final List<String> expected = SUPPLY.nextStringList(3);
        final List<String> result = Collecting.builder(() -> new ArrayList<String>())
                                              .addAll(expected)
                                              .build();
        assertEquals(expected, result);
    }
}
