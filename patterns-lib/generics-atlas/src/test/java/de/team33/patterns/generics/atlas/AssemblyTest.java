package de.team33.patterns.generics.atlas;

import org.junit.jupiter.api.Test;

import java.lang.reflect.TypeVariable;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AssemblyTest {

    @Test
    void getActualParameter_unknown() {
        try {
            final Assembly result = ClassCase.toAssembly(Integer.class).getActualParameter("E");
            fail("expected to Fail - but was " + result);
        } catch (final IllegalArgumentException e) {
            // e.printStackTrace();
            assertEquals("formal parameter <E> not found in []", e.getMessage());
        }
    }

    static interface StringList extends List<String> {}

    @Test
    void getActualParameter_definite() {
        final Assembly result = ClassCase.toAssembly(StringList.class)
                                         .getMemberAssembly(StringList.class.getGenericInterfaces()[0])
                                         .getActualParameter("E");
        assertEquals(ClassCase.toAssembly(String.class), result);
    }

    @Test
    void getActualParameter_indefinite() {
        try {
            final Assembly result = ClassCase.toAssembly(List.class)
                                             .getActualParameter("E");
            fail("expected to Fail - but was " + result);
        } catch (final IllegalStateException e) {
            // e.printStackTrace();
            assertEquals("actual parameter for <E> not found in []", e.getMessage());
        }
    }
}
