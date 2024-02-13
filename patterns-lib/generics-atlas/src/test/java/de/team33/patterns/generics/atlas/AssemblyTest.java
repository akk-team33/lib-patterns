package de.team33.patterns.generics.atlas;

import org.junit.jupiter.api.Test;

import java.lang.reflect.TypeVariable;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AssemblyTest {

    @Test
    void getActualParameter_unknown() {
        try {
            final Assembly result = ClassCase.toAssembly(Integer.class).getActualParameter("T");
            fail("expected to Fail - but was " + result);
        } catch (final IllegalArgumentException e) {
            // e.printStackTrace();
            assertEquals("formal parameter <T> not found in []", e.getMessage());
        }
    }
}
