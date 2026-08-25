package de.team33.patterns.value.sinope.publics;

import de.team33.patterns.value.sinope.Equation;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EquationTest {

    private static final Equation<Sample> EQUATION = Equation.of(Sample.class, Sample::uuid);

    private final UUID uuid = UUID.randomUUID();
    private final Sample left = new Sample(uuid);

    @Test
    final void identical() {
        assertTrue(EQUATION.equals(left, left));
    }

    @Test
    final void equal() {
        assertTrue(EQUATION.equals(left, new Sample(uuid)));
    }

    @Test
    final void notEqual() {
        assertFalse(EQUATION.equals(left, new Sample(UUID.randomUUID())));
    }

    @Test
    final void notEqual_null() {
        assertFalse(EQUATION.equals(left, new Sample(null)));
    }

    @Test
    final void foreign() {
        assertFalse(EQUATION.equals(left, uuid));
    }

    @Test
    final void foreign_null() {
        assertFalse(EQUATION.equals(left, null));
    }

    @Test
    final void failing() {
        assertThrows(NullPointerException.class, () -> EQUATION.equals(null, left));
        assertThrows(NullPointerException.class, () -> EQUATION.equals(new Sample(null), left));
    }

    @Test
    final void hash() {
        assertEquals(uuid.hashCode(), EQUATION.hashCode(left));
    }

    @Test
    final void string() {
        assertEquals(uuid.toString(), EQUATION.toString(left));
    }

    private record Sample(UUID uuid) {}
}