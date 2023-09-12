package de.team33.patterns.test.serial.charon;

import de.team33.patterns.serial.charon.Series;
import de.team33.patterns.shared.serial.charon.Supply;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class SeriesTest {

    private static final Supply SUPPLY = new Supply();

    @Test
    final void empty() {
        final Series<?> empty = Series.empty();
        assertTrue(empty.isEmpty());
        assertFalse(empty.isCharged());
        assertThrows(NoSuchElementException.class, empty::head);
        assertSame(empty, empty.tail());
        assertSame(empty, Series.empty());
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    final void isEmpty(final boolean empty) {
        final List<String> origin = empty ? Collections.emptyList() : SUPPLY.nextChargedList();
        final Series<String> result = Series.of(origin);
        assertEquals(empty, result.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    final void isCharged(final boolean charged) {
        final List<String> origin = charged ? SUPPLY.nextChargedList() : Collections.emptyList();
        final Series<String> result = Series.of(origin);
        assertEquals(charged, result.isCharged());
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    final void ifCharged(final boolean charged) {
        final List<String> origin = charged ? SUPPLY.nextChargedList() : Collections.emptyList();
        final String fallback = SUPPLY.nextString();
        final Series<String> sample = Series.of(origin);
        final String expected = charged ? origin.get(0) : fallback;
        final String result = sample.ifCharged((head, tail) -> head).orElse(fallback);
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    final void head(final boolean charged) {
        final List<String> origin = charged ? SUPPLY.nextChargedList() : Collections.emptyList();
        final Series<String> sample = Series.of(origin);
        try {
            final String result = sample.head();
            assertTrue(charged);
            assertEquals(origin.get(0), result);
        } catch (final NoSuchElementException e) {
            assertFalse(charged);
        }
    }

    @Test
    final void tail() {
        fail("not yet implemented");
    }

    @Test
    final void size() {
        fail("not yet implemented");
    }

    @Test
    final void asList() {
        fail("not yet implemented");
    }

    @Test
    final void testEquals() {
        fail("not yet implemented");
    }

    @Test
    final void testHashCode() {
        fail("not yet implemented");
    }

    @Test
    final void testToString() {
        fail("not yet implemented");
    }
}
