package de.team33.patterns.building.elara.publics;

import de.team33.patterns.building.elara.sample.Collecting;
import de.team33.patterns.building.elara.sample.Supply;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CollectingTest {

    private static final Supply SUPPLY = new Supply();

    @Test
    final void lifecycle_Charger() {
        final Collecting.Charger<String, List<String>, ?> charger = Collecting.charger(SUPPLY.nextStringList(3));
        charger.charged();
        assertThrows(IllegalStateException.class, () -> charger.add(SUPPLY.nextString()));
    }

    @Test
    final void identity_Charger() {
        final Collecting.Charger<String, List<String>, ?> charger = Collecting.charger(SUPPLY.nextStringList(3));
        final List<String> original = charger.charged();
        final List<String> result = charger.charged();
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
                                              .charged();
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
                                              .charged();
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
