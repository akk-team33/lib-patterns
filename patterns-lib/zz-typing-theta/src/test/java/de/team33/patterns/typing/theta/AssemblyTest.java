package de.team33.patterns.typing.theta;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

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

    static interface StringList extends List<String> {
    }
}
