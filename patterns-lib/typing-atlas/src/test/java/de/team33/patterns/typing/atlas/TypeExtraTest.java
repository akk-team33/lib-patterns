package de.team33.patterns.typing.atlas;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TypeExtraTest {

    @Test
    void getActualParameter_unknown() {
        try {
            final Type result = ClassCase.toTypedef(Integer.class).getActualParameter("E");
            fail("expected to Fail - but was " + result);
        } catch (final IllegalArgumentException e) {
            // e.printStackTrace();
            assertEquals("formal parameter <E> not found in []", e.getMessage());
        }
    }

    static interface StringList extends List<String> {}

    @Test
    void getActualParameter_definite() {
        final Type result = ClassCase.toTypedef(StringList.class)
                                     .getMemberType(StringList.class.getGenericInterfaces()[0])
                                     .getActualParameter("E");
        assertEquals(ClassCase.toTypedef(String.class), result);
    }

    @Test
    void getActualParameter_indefinite() {
        try {
            final Type result = ClassCase.toTypedef(List.class)
                                         .getActualParameter("E");
            fail("expected to Fail - but was " + result);
        } catch (final IllegalStateException e) {
            // e.printStackTrace();
            assertEquals("actual parameter for <E> not found in []", e.getMessage());
        }
    }
}
