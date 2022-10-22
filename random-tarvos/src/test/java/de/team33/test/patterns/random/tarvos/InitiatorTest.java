package de.team33.test.patterns.random.tarvos;

import de.team33.patterns.random.tarvos.Initiator;
import de.team33.patterns.random.tarvos.UnfitConditionException;
import de.team33.test.patterns.random.shared.Record;
import de.team33.test.patterns.random.shared.Sample;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class InitiatorTest implements Initiator {

    private final Record expected = new Record(true,
                                               "ABC",
                                               0,
                                               Long.MAX_VALUE,
                                               Arrays.asList("abc", "def", "ghi"),
                                               Arrays.asList(4L, 69L, 345L));

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
            // e.printStackTrace();
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
            // e.printStackTrace();
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
            // e.printStackTrace();
            assertEquals("Constructor not applicable!", e.getMessage().substring(0, 27));
            assertTrue(e.getMessage().contains(RecordEx.class.getSimpleName() + "("));
        }
    }

    public final boolean nextBoolean() {
        return expected.booleanValue();
    }

    public final String nextString() {
        return expected.stringValue();
    }

    public final long nextLong() {
        return expected.longValue();
    }

    public final List<String> nextStringList() {
        return expected.stringList();
    }

    public final List<Long> nextLongList() {
        return expected.longList();
    }
}