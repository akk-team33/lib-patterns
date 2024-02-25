package de.team33.patterns.reflect.pandora.publics;

import de.team33.patterns.reflect.pandora.testing.Supply;
import de.team33.patterns.reflect.pandora.sample.Sample01;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Sample01Test extends Supply {

    @Test
    final void getIntValue() {
        final Sample01 origin = nextSample01();
        final Sample01 result = origin.toMutable();
        assertNotSame(origin, result);
        assertEquals(origin, result);
    }
}
