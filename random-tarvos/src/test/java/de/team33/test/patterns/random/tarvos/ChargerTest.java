package de.team33.test.patterns.random.tarvos;

import de.team33.patterns.random.tarvos.Charger;
import de.team33.samples.patterns.production.narvi.Buildable;
import de.team33.samples.patterns.production.narvi.Sample;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChargerTest /* extends Random implements Generator */ {

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
    private final Charger<ChargerTest> charger = new Charger<>(ChargerTest.class);

    public static String getNothing() {
        throw new UnsupportedOperationException("should not be called anyways");
    }

    @Test
    final void load_Sample() {
        final Sample result = charger.charge(new Sample(), this);
        assertEquals(sample, result);
    }

    @Test
    final void load_Buildable() {
        final Buildable result = charger.charge(Buildable.builder(), this).build();
        assertEquals(buildable, result);
    }

//    @Override
//    public final BigInteger nextBits(final int numBits) {
//        return new BigInteger(numBits, this);
//    }

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
