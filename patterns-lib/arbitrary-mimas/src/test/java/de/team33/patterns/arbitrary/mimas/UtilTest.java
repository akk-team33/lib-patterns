package de.team33.patterns.arbitrary.mimas;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UtilTest {

    @Test
    final void load() {
        try {
            final String result = Util.load(UtilTest.class, "no.txt");
            fail("should fail but was <" + result + ">");
        } catch (final UnfitConditionException e) {
            // e.printStackTrace();
            assertEquals("Should not happen:", e.getMessage().substring(0, 18));
            assertTrue(e.getMessage().contains(UtilTest.class.getSimpleName()));
            assertTrue(e.getMessage().contains("no.txt"));
        }
    }
}
