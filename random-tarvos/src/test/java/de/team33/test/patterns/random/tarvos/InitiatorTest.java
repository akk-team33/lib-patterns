package de.team33.test.patterns.random.tarvos;

import de.team33.patterns.random.tarvos.Initiator;
import de.team33.test.patterns.random.shared.Record;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    public final boolean nextBoolean() {
        return expected.booleanValue();
    }

    public final String nextString() {
        return expected.stringValue();
    }

    public final int nextInt() {
        return expected.intValue() + 1;
    }

    public final Long nextLong() {
        return expected.longValue();
    }

    public final List<String> nextStringList() {
        return expected.stringList();
    }

    public final List<Long> nextLongList() {
        return expected.longList();
    }
}