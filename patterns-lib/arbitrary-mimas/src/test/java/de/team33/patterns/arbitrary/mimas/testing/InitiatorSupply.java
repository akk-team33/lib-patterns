package de.team33.patterns.arbitrary.mimas.testing;

import de.team33.patterns.arbitrary.mimas.Initiator;
import de.team33.patterns.arbitrary.mimas.sample.Record;

import java.util.Arrays;
import java.util.List;

public class InitiatorSupply implements Initiator {

    protected final Record expected = new Record(true,
                                                 "ABC",
                                                 0,
                                                 Long.MAX_VALUE,
                                                 Arrays.asList("abc", "def", "ghi"),
                                                 Arrays.asList(4L, 69L, 345L));

    public final boolean anyBoolean() {
        return !expected.booleanValue();
    }

    public final boolean anyArg0() {
        return expected.booleanValue();
    }

    public final String anyString() {
        return expected.stringValue();
    }

    public final long anyLong() {
        return expected.longValue();
    }

    public final List<String> anyStringList() {
        return expected.stringList();
    }

    public final List<Long> anyLongList() {
        return expected.longList();
    }
}
