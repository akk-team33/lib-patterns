package de.team33.patterns.arbitrary.mimas.publics;

import de.team33.patterns.arbitrary.mimas.UnfitConditionException;
import de.team33.patterns.arbitrary.mimas.sample.Record;
import de.team33.patterns.arbitrary.mimas.sample.Sample;
import de.team33.patterns.arbitrary.mimas.testing.InitiatorSupply;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class InitiatorTest extends InitiatorSupply {

    @Test
    final void record_behavior() {
        final Record result = new Record(expected.booleanValue(),
                                         expected.stringValue(),
                                         expected.intValue(),
                                         expected.longValue(),
                                         expected.stringList(),
                                         expected.longList());
        assertEquals(expected.toString(), result.toString());
        assertEquals(expected.hashCode(), result.hashCode());
        assertEquals(expected, result);
    }

    @Test
    final void initiate() {
        final Record result = initiate(Record.class, "next_Int", "arg2");
        assertEquals(expected, result);
    }

    @Test
    final void no_supplier() {
        try {
            final Record result = initiate(Record.class);
            fail("should fail but was <" + result + ">");
        } catch (final UnfitConditionException e) {
            assertEquals("No appropriate supplier method found!", e.getMessage().substring(0, 37));
            assertTrue(e.getMessage().contains(Record.class.getSimpleName() + "("));
        }
    }

    @Test
    final void no_constructor() {
        try {
            final Sample result = initiate(SampleEx.class);
            fail("should fail but was <" + result + ">");
        } catch (final UnfitConditionException e) {
            assertEquals("No public constructor found in", e.getMessage().substring(0, 30));
            assertTrue(e.getMessage().contains(SampleEx.class.getSimpleName()));
        }
    }

    @Test
    final void unfitConstructor() {
        try {
            final Record result = initiate(RecordEx.class, "next_Int", "arg2");
            fail("should fail but was <" + result + ">");
        } catch (final UnfitConditionException e) {
            assertEquals("Constructor not applicable!", e.getMessage().substring(0, 27));
            assertTrue(e.getMessage().contains(RecordEx.class.getSimpleName() + "("));
        }
    }
}
