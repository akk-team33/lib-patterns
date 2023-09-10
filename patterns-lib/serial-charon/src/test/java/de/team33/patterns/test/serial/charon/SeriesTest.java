package de.team33.patterns.test.serial.charon;

import de.team33.patterns.serial.charon.Series;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class SeriesTest {

    @Test
    final void empty() {
        final Series<?> empty = Series.empty();
        assertTrue(empty.isEmpty());
        assertFalse(empty.isCharged());
        assertThrows(NoSuchElementException.class, empty::head);
        assertSame(empty, empty.tail());
        assertSame(empty, Series.empty());
    }

    @Test
    final void isEmpty() {
        fail("not yet implemented");
    }

    @Test
    final void isCharged() {
        fail("not yet implemented");
    }

    @Test
    final void ifCharged() {
        fail("not yet implemented");
    }

    @Test
    final void head() {
        fail("not yet implemented");
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