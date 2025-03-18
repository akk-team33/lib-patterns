package de.team33.patterns.random.tarvos.publics;

import de.team33.patterns.random.tarvos.Charger;
import de.team33.patterns.random.tarvos.Generator;
import de.team33.patterns.random.tarvos.Initiator;
import de.team33.patterns.random.tarvos.UnfitConditionException;
import de.team33.patterns.random.tarvos.shared.Record;
import de.team33.patterns.random.tarvos.shared.Sample;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class SupplierNotApplicableTest implements Generator, Charger, Initiator {

    private final Random random = new SecureRandom();

    @Test
    final void charger() {
        try {
            final Sample result = charge(new Sample(), "setLongValue", "setStringList", "setLongList");
            fail("should fail but was <" + result + ">");
        } catch (final UnfitConditionException e) {
            // e.printStackTrace();
            assertEquals("Method not applicable as supplier!", e.getMessage().substring(0, 34));
            assertTrue(e.getMessage().contains(getClass().getSimpleName() + ".nextString()"));
        }
    }

    @Test
    final void initiator() {
        try {
            final Record result = initiate(Record.class, "arg3", "arg4", "arg5");
            fail("should fail but was <" + result + ">");
        } catch (final UnfitConditionException e) {
            // e.printStackTrace();
            assertEquals("Method not applicable as supplier!", e.getMessage().substring(0, 34));
            assertTrue(e.getMessage().contains(getClass().getSimpleName() + ".nextString()"));
        }
    }

    @Override
    public final BigInteger nextBits(final int numBits) {
        return new BigInteger(numBits, random);
    }

    /**
     * Cannot be called by reflection outside of this package even though it is public
     * because the declaring class (this test class) is not public.
     */
    @SuppressWarnings("unused")
    public final String nextString() {
        return nextString(5, "abc");
    }
}
