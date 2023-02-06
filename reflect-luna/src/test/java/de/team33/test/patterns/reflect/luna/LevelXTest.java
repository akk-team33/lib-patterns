package de.team33.test.patterns.reflect.luna;

import de.team33.patterns.random.tarvos.Generator;
import de.team33.sample.patterns.reflect.luna.Level0;
import de.team33.sample.patterns.reflect.luna.Level1;
import de.team33.sample.patterns.reflect.luna.Level2;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.time.Instant;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class LevelXTest extends Random implements Generator {

    @Override
    public final BigInteger nextBits(final int numBits) {
        return new BigInteger(numBits, this);
    }

    private Level0 nextLevel0() {
        return new Level0().setIntValue(nextInt())
                           .setDoubleValue(nextDouble())
                           .setInstantValue(
                                   Instant.now()
                                          .plusSeconds(nextLong(-10000L, 10000L)))
                           .setStringValue(
                                   nextString(nextInt(0, 24), "abc"));
    }

    private Level1 nextLevel1() {
        return new Level1(nextLevel0()).setIntValue1(nextInt())
                                       .setDoubleValue1(nextDouble())
                                       .setInstantValue1(
                                               Instant.now()
                                                      .plusSeconds(nextLong(-10000L, 10000L)))
                                       .setStringValue1(
                                               nextString(nextInt(0, 24), "abc"));
    }

    private Level2 nextLevel2() {
        return new Level2(nextLevel1()).setIntValue2(nextInt())
                                       .setDoubleValue2(nextDouble())
                                       .setInstantValue2(
                                               Instant.now()
                                                      .plusSeconds(nextLong(-10000L, 10000L)))
                                       .setStringValue2(
                                               nextString(nextInt(0, 24), "abc"));
    }

    @Test
    final void copyLevel0() {
        final Level0 expected = nextLevel0();
        final Level0 result = new Level0(expected);
        assertEquals(expected, result);
    }

    @Test
    final void copyLevel1_to_0() {
        final Level1 expected = nextLevel1();
        final Level0 result = new Level0(expected);
        assertNotEquals(expected, result);
    }

    @Test
    final void copyLevel1() {
        final Level1 expected = nextLevel1();
        final Level1 result = new Level1(expected);
        assertEquals(expected, result);
    }

    @Test
    final void copyLevel2_to_1() {
        final Level2 expected = nextLevel2();
        final Level1 result = new Level1(expected);
        assertNotEquals(expected, result);
    }

    @Test
    final void copyLevel2() {
        final Level2 expected = nextLevel2();
        final Level2 result = new Level2(expected);
        assertEquals(expected, result);
    }
}
