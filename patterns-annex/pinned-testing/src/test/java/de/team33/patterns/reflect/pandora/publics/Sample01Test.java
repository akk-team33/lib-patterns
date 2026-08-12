package de.team33.patterns.reflect.pandora.publics;

import de.team33.patterns.reflect.pandora.sample.Sample01;
import de.team33.patterns.reflect.pandora.testing.Supply;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

class Sample01Test extends Supply {

    @Test
    final void getIntValue() {
        final Sample01 origin = anySample01();
        final Sample01 result = origin.toMutable();
        assertNotSame(origin, result);
        assertEquals(origin, result);
    }
}
