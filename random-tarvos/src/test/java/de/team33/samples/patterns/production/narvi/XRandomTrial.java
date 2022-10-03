package de.team33.samples.patterns.production.narvi;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class XRandomTrial {

    public static final XRandom RANDOM = new XRandom();

    @Test
    final void nextSample() {
        final Sample result = RANDOM.nextSample();

        final int intValue = result.getIntValue();
        assertTrue(5 > intValue);
        assertTrue(-5 <= intValue);

        final Long longValue = result.getLongValue();
        assertTrue(5 > longValue);
        assertTrue(-5 <= longValue);

        assertEquals(5, result.getStringValue().length());
        System.out.println(result);
    }

    @Test
    final void nextBuildable() {
        final Buildable result = RANDOM.nextBuildable();

        final int intValue = result.getIntValue();
        assertTrue(5 > intValue);
        assertTrue(-5 <= intValue);

        final Long longValue = result.getLongValue();
        assertTrue(5 > longValue);
        assertTrue(-5 <= longValue);

        assertEquals(5, result.getStringValue().length());
        System.out.println(result);
    }
}