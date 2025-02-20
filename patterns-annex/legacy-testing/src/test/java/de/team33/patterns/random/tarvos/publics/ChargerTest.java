package de.team33.patterns.random.tarvos.publics;

import de.team33.patterns.random.tarvos.Charger;
import de.team33.patterns.random.tarvos.UnfitConditionException;
import de.team33.patterns.random.tarvos.shared.Buildable;
import de.team33.patterns.random.tarvos.shared.Generic;
import de.team33.patterns.random.tarvos.shared.Sample;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

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
    final void setterNotApplicable() {
        try {
            final Sample result = charge(new SampleEx());
            fail("should fail but was <" + result + ">");
        } catch (final UnfitConditionException e) {
            // e.printStackTrace();
            assertEquals("Method not applicable as setter!", e.getMessage().substring(0, 32));
            assertTrue(e.getMessage().contains(SampleEx.class.getSimpleName() + ".setDateValue"));
        }
    }

    @Test
    final void supplierNotFound() {
        try {
            final Sample result = charge(new Sample(), "nextStrings");
            fail("should fail but was <" + result + ">");
        } catch (final UnfitConditionException e) {
            // e.printStackTrace();
            assertEquals("No appropriate supplier method found!", e.getMessage().substring(0, 37));
            assertTrue(e.getMessage().contains(getClass().getSimpleName()));
            assertTrue(e.getMessage().contains(Sample.class.getSimpleName()));
        }
    }

    @Test
    final void charge_Sample() {
        final Sample result = charge(new Sample());
        assertEquals(sample, result);
    }

    @Test
    final void charge_SampleEx() {
        final Sample result = charge(new SampleEx(), "setDateValue");
        assertEquals(sample, result);
    }

    @Test
    final void charge_Buildable() {
        final Buildable result = charge(Buildable.builder()).build();
        assertEquals(buildable, result);
    }

    @Test
    final void charge_Generic() {
        final Generic<String> result = charge(new Generic<>(), "setTValue");
        assertEquals(new Generic<String>(), result);
    }

    public final boolean nextBoolean() {
        return sample.isBooleanValue();
    }

    public final Integer nextInt() {
        return sample.getIntValue();
    }

    public final String nextString() {
        return sample.getStringValue();
    }

    public final Long nextLong() {
        return sample.getLongValue();
    }

    public final long nextLongValue() {
        return sample.getLongValue();
    }

    public final List<String> nextStrings() {
        return sample.getStringList();
    }

    public final List<Long> nextLongs() {
        return sample.getLongList();
    }

    public final Date nextDate() {
        return new Date();
    }
}

