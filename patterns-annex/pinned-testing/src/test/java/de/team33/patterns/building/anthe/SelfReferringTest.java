package de.team33.patterns.building.anthe;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SelfReferringTest extends SelfReferring<SelfReferringTest> {

    SelfReferringTest() {
        super(SelfReferringTest.class);
    }

    @Test
    final void testTHIS() {
        assertSame(this, THIS());
    }

    @Test
    final void illegal() {
        assertThrows(IllegalArgumentException.class, IllegalSelfReferring::new);
    }

    static class IllegalSelfReferring extends SelfReferring<SelfReferringTest> {

        IllegalSelfReferring() {
            super(SelfReferringTest.class);
        }
    }
}