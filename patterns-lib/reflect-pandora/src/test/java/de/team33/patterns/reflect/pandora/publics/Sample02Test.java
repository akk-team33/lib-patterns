package de.team33.patterns.reflect.pandora.publics;

import de.team33.patterns.reflect.pandora.testing.Supply;
import de.team33.patterns.reflect.pandora.sample.Sample02;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

class Sample02Test extends Supply {

    @Test
    final void getIntValue() {
        final Sample02 origin = nextSample02();
        final Sample02 result = origin.toMutable();
        assertNotSame(origin, result);
        assertEquals(origin, result);
    }
}
