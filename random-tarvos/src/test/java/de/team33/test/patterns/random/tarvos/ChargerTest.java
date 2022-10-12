package de.team33.test.patterns.random.tarvos;

import de.team33.patterns.random.tarvos.Charger;
import de.team33.test.patterns.random.shared.Buildable;
import de.team33.test.patterns.random.shared.Generic;
import de.team33.test.patterns.random.shared.Sample;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChargerTest implements Charger {

    private final Sample sample = new Sample().setBooleanValue(false)
                                              .setStringValue("ABC")
                                              .setIntValue(278)
                                              .setLongValue(Long.MAX_VALUE)
                                              .setStringList(Arrays.asList("abc", "def", "ghi"))
                                              .setLongList(Arrays.asList(4L, 69L, 345L));
    private final Buildable buildable = Buildable.builder()
                                                 .setStringValue("ABC")
                                                 .setIntValue(278)
                                                 .setLongValue(Long.MAX_VALUE)
                                                 .setStringList(Arrays.asList("abc", "def", "ghi"))
                                                 .setLongList(Arrays.asList(4L, 69L, 345L)).build();

    public static String getNothing() {
        throw new UnsupportedOperationException("should not be called anyways");
    }

    @Test
    final void charge_Sample() {
        final Sample result = charge(new Sample());
        assertEquals(sample, result);
    }

    @Test
    final void charge_Buildable() {
        final Buildable result = charge(Buildable.builder()).build();
        assertEquals(buildable, result);
    }

    @Test
    final void charge_Generic() {
        final Generic<String> result = charge(new Generic<>(), "setTValue_");
        assertEquals(new Generic<String>(), result);
    }

    public final boolean nextBoolean() {
        return sample.isBooleanValue();
    }

    public final int nextInt() {
        return sample.getIntValue();
    }

    public final String nextString() {
        return sample.getStringValue();
    }

    public final Long nextBoxedLong() {
        return sample.getLongValue();
    }

    public final List<String> nextStrings() {
        return sample.getStringList();
    }

    public final List<Long> nextLongs() {
        return sample.getLongList();
    }
}
