package de.team33.patterns.test.reflect.pandora;

import de.team33.patterns.testing.reflect.pandora.Supply;
import de.team33.patterns.sample.reflect.pandora.Sample01;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Sample01Test extends Supply {

    @Test
    void getIntValue() {
        final Sample01 origin = nextSample01();
        final Sample01 result = origin.toMutable();
        assertNotSame(origin, result);
        assertEquals(origin, result);
    }
}
