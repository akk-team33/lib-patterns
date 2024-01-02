package de.team33.patterns.normal.iocaste.test;

import de.team33.patterns.normal.iocaste.Normal;
import de.team33.patterns.normal.iocaste.Normalizer;
import de.team33.patterns.normal.iocaste.testing.Supply;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NormalizerTest extends Supply {

    private static final Normalizer NORMALIZER = Normalizer.builder().build();

    private static Normal normal(final Object origin) {
        return NORMALIZER.normal(origin);
    }

    @Test
    final void normal_null() {
        assertNull(normal(null));
    }

    @Test
    final void normal_String() {
        final String origin = nextString();

        final Normal normal = normal(origin);

        assertEquals(Normal.Type.SIMPLE, normal.type());
        assertTrue(normal.isSimple());
        assertEquals(origin, normal.asSimple());
    }
}