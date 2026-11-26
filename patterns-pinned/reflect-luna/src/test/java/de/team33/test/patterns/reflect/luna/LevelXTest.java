package de.team33.test.patterns.reflect.luna;

import de.team33.sample.patterns.reflect.luna.Level0;
import de.team33.sample.patterns.reflect.luna.Level1;
import de.team33.sample.patterns.reflect.luna.Level2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class LevelXTest {

    private static final Supply SUPPLY = new Supply();

    @Test
    final void copyLevel0() {
        final Level0 expected = SUPPLY.nextLevel0();
        final Level0 result = new Level0(expected);
        assertEquals(expected, result);
    }

    @Test
    final void copyLevel1_to_0() {
        final Level1 expected = SUPPLY.nextLevel1();
        final Level0 result = new Level0(expected);
        assertNotEquals(expected, result);
    }

    @Test
    final void copyLevel1() {
        final Level1 expected = SUPPLY.nextLevel1();
        final Level1 result = new Level1(expected);
        assertEquals(expected, result);
    }

    @Test
    final void copyLevel2_to_1() {
        final Level2 expected = SUPPLY.nextLevel2();
        final Level1 result = new Level1(expected);
        assertNotEquals(expected, result);
    }

    @Test
    final void copyLevel2() {
        final Level2 expected = SUPPLY.nextLevel2();
        final Level2 result = new Level2(expected);
        assertEquals(expected, result);
    }
}
