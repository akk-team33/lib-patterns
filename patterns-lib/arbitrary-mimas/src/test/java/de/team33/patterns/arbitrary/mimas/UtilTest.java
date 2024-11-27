package de.team33.patterns.arbitrary.mimas;

import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class UtilTest {

    @Test
    final void load() {
        try {
            final String result = Util.load(UtilTest.class, "no.txt");
            fail("should fail but was <" + result + ">");
        } catch (final UnfitConditionException e) {
            assertEquals("Should not happen:", e.getMessage().substring(0, 18));
            assertTrue(e.getMessage().contains(UtilTest.class.getSimpleName()));
            assertTrue(e.getMessage().contains("no.txt"));
        }
    }

    @Test
    final void legacy_proxy() {
        final Random random = Util.legacy(() -> 278L);
        assertEquals(278L, random.nextLong());
    }

    @Test
    final void legacy_random() {
        final Random random = Util.legacy(new SecureRandom());
        assertInstanceOf(SecureRandom.class, random);
    }
}
