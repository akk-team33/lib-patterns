package de.team33.patterns.reflect.pandora;

import de.team33.patterns.reflect.pandora.testing.Supply;
import de.team33.patterns.sample.reflect.pandora.Sample02;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

class Sample02Test extends Supply {

    @Test
    void getIntValue() {
        final Sample02 origin = nextSample02();
        final Sample02 result = origin.toMutable();
        assertNotSame(origin, result);
        assertEquals(origin, result);
    }
}
