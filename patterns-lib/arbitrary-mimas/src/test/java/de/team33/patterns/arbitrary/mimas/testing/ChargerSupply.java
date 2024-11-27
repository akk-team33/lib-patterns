package de.team33.patterns.arbitrary.mimas.testing;

import de.team33.patterns.arbitrary.mimas.Charger;
import de.team33.patterns.arbitrary.mimas.sample.Buildable;
import de.team33.patterns.arbitrary.mimas.sample.Sample;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ChargerSupply implements Charger {

    protected final Sample sample = new Sample().setBooleanValue(false)
                                                .setStringValue("ABC")
                                                .setIntValue(278)
                                                .setLongValue(Long.MAX_VALUE)
                                                .setStringList(Arrays.asList("abc", "def", "ghi"))
                                                .setLongList(Arrays.asList(4L, 69L, 345L));
    protected final Buildable buildable = Buildable.builder()
                                                   .setStringValue("ABC")
                                                   .setIntValue(278)
                                                   .setLongValue(Long.MAX_VALUE)
                                                   .setStringList(Arrays.asList("abc", "def", "ghi"))
                                                   .setLongList(Arrays.asList(4L, 69L, 345L)).build();

    public static String getNothing() {
        throw new UnsupportedOperationException("should not be called anyways");
    }

    public final boolean anyBoolean() {
        return sample.isBooleanValue();
    }

    public final Integer anyInt() {
        return sample.getIntValue();
    }

    public final String anyString() {
        return sample.getStringValue();
    }

    public final Long anyLong() {
        return sample.getLongValue();
    }

    public final long anyLongValue() {
        return sample.getLongValue();
    }

    public final List<String> anyStrings() {
        return sample.getStringList();
    }

    public final List<Long> anyLongs() {
        return sample.getLongList();
    }

    public final Date anyDate() {
        return new Date();
    }
}
