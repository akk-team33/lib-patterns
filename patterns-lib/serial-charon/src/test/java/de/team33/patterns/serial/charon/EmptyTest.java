package de.team33.patterns.serial.charon;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class EmptyTest {

    @Test
    final void head() {
        assertThrows(NoSuchElementException.class, Empty.INSTANCE::head);
    }

    @Test
    final void tail() {
        assertSame(Empty.INSTANCE, Empty.INSTANCE.tail());
    }

    @Test
    final void size() {
        assertEquals(0, Empty.INSTANCE.size());
    }

    @Test
    final void asList() {
        assertEquals(Collections.emptyList(), Empty.INSTANCE.asList());
    }
}