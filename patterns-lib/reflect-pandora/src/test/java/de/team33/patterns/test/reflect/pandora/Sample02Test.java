package de.team33.patterns.test.reflect.pandora;

import de.team33.patterns.testing.reflect.pandora.Supply;
import de.team33.patterns.sample.reflect.pandora.Sample02;
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
