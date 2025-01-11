package de.team33.patterns.normal.iocaste.publics;

import de.team33.patterns.normal.iocaste.Basic;
import de.team33.patterns.normal.iocaste.testing.Supply;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BasicTest extends Supply {

    @Test
    void of_String() {
        final String origin = anyString();
        final Basic result = Basic.of(origin);
        assertEquals(origin, result.toString());
    }

    @Test
    void of_Integer() {
        final int origin = anyInt();
        final Basic result = Basic.of(origin);
        assertEquals(origin, result.toInteger());
    }

    @Test
    void of_String_toInteger_nonNumber() {
        final Basic basic = Basic.of("S_" + anyString()); // prefix to ensure not to be a numeric string
        try {
            final Integer result = basic.toInteger();
            fail("expected to fail - but was " + result);
        } catch (final RuntimeException e) {
            // as expected
            e.printStackTrace();
        }
    }

    @Test
    void of_String_toInteger_Number() {
        final int origin = anyInt();
        final Basic result = Basic.of(String.valueOf(origin)); // ensure to be a numeric string
        assertEquals(origin, result.toInteger());
    }

    @Test
    void of_String_toInteger_largeNumber() {
        // ensure to be a long numeric string ...
        final Basic basic = Basic.of(String.valueOf(1L + anyLong(Integer.MAX_VALUE, Long.MAX_VALUE)));
        try {
            final Integer result = basic.toInteger();
            fail("expected to fail - but was " + result);
        } catch (final RuntimeException e) {
            // as expected
            e.printStackTrace();
        }
    }
}