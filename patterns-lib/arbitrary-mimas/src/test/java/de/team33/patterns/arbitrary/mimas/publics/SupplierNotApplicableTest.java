package de.team33.patterns.arbitrary.mimas.publics;

import de.team33.patterns.arbitrary.mimas.Charger;
import de.team33.patterns.arbitrary.mimas.Generator;
import de.team33.patterns.arbitrary.mimas.Initiator;
import de.team33.patterns.arbitrary.mimas.UnfitConditionException;
import de.team33.patterns.arbitrary.mimas.sample.Record;
import de.team33.patterns.arbitrary.mimas.sample.Sample;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class SupplierNotApplicableTest extends Random implements Generator, Charger, Initiator {

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
    public final BigInteger anyBits(final int numBits) {
        return new BigInteger(numBits, this);
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
