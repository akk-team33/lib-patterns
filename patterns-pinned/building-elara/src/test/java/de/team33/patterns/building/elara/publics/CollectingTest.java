package de.team33.patterns.building.elara.publics;

import de.team33.patterns.building.elara.sample.Collecting;
import de.team33.patterns.building.elara.sample.Supply;
import de.team33.patterns.streamable.galatea.Streamable;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CollectingTest {

    private static final Supply SUPPLY = new Supply();

    @Test
    final void lifecycle_Charger() {
        final Collecting.Charger<String, List<String>, ?> charger = Collecting.charger(SUPPLY.anyStringList(3));
        charger.charged();
        assertThrows(IllegalStateException.class, () -> charger.add(SUPPLY.anyString()));
    }

    @Test
    final void identity_Charger() {
        final Collecting.Charger<String, List<String>, ?> charger = Collecting.charger(SUPPLY.anyStringList(3));
        final List<String> original = charger.charged();
        final List<String> result = charger.charged();
        assertEquals(original, result);
        assertSame(original, result);
    }

    @Test
    final void identity_Builder() {
        final List<String> template = SUPPLY.anyStringList(3);
        final Collecting.Builder<String, List<String>, ?> builder = Collecting.builder(() -> new ArrayList<>(template));
        final List<String> original = builder.build();
        final List<String> result = builder.build();
        assertEquals(original, result);
        assertNotSame(original, result);
    }

    @Test
    final void add_Charger() {
        final List<String> expected = SUPPLY.anyStringList(3);
        final List<String> result = Collecting.charger(new ArrayList<String>(3))
                                              .add(expected.get(0))
                                              .add(expected.get(1))
                                              .add(expected.get(2))
                                              .charged();
        assertEquals(expected, result);
    }

    @Deprecated
    @Test
    final void add_Charger_forEach_streamable() {
        final List<String> expected = SUPPLY.anyStringList(3);
        final List<String> result = Collecting.charger(new ArrayList<String>(3))
                                              .forEach(expected::stream, Collecting.Charger::add)
                                              .forEach(Streamable.<String>empty(), Collecting.Setup::add)
                                              .charged();
        assertEquals(expected, result);
    }

    @Test
    final void add_Charger_forEach() {
        final List<String> expected = SUPPLY.anyStringList(3);
        final List<String> result = Collecting.charger(new ArrayList<String>(3))
                                              .forEach(expected.stream()).apply(Collecting.Charger::add)
                                              .forEach(Stream.<String>empty()).apply(Collecting.Setup::add)
                                              .charged();
        assertEquals(expected, result);
    }

    @Test
    final void add_Builder() {
        final List<String> expected = SUPPLY.anyStringList(3);
        final List<String> result = Collecting.builder(() -> new ArrayList<String>(3))
                                              .add(expected.get(0))
                                              .add(expected.get(1))
                                              .add(expected.get(2))
                                              .build();
        assertEquals(expected, result);
    }

    @Deprecated
    @Test
    final void add_Builder_forEach_streamable() {
        final List<String> expected = SUPPLY.anyStringList(3);
        final List<String> result = Collecting.builder(() -> new ArrayList<String>(3))
                                              .forEach(expected::stream, Collecting.Builder::add)
                                              .forEach(Streamable.<String>empty(), Collecting.Setup::add)
                                              .build();
        assertEquals(expected, result);
    }

    @Test
    final void add_Builder_forEach() {
        final List<String> expected = SUPPLY.anyStringList(3);
        final List<String> result = Collecting.builder(() -> new ArrayList<String>(3))
                                              .forEach(expected.stream()).apply(Collecting.Builder::add)
                                              .forEach(Stream.<String>empty()).apply(Collecting.Setup::add)
                                              .build();
        assertEquals(expected, result);
    }

    @Test
    final void addAll_Charger() {
        final List<String> expected = SUPPLY.anyStringList(3);
        final List<String> result = Collecting.charger(new ArrayList<String>(3))
                                              .addAll(expected)
                                              .charged();
        assertEquals(expected, result);
    }

    @Test
    final void addAll_Builder() {
        final List<String> expected = SUPPLY.anyStringList(3);
        final List<String> result = Collecting.builder(() -> new ArrayList<String>(3))
                                              .addAll(expected)
                                              .build();
        assertEquals(expected, result);
    }
}
