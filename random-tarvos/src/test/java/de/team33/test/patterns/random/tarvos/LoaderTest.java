package de.team33.test.patterns.random.tarvos;

import de.team33.patterns.random.tarvos.Loader;
import de.team33.samples.patterns.production.narvi.Sample;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoaderTest /* extends Random implements Generator */ {

    private final Sample sample = new Sample().setBooleanValue(false)
                                              .setStringValue("ABC")
                                              .setIntValue(278)
                                              .setLongValue(Long.MAX_VALUE)
                                              .setStringList(Arrays.asList("abc", "def", "ghi"))
                                              .setLongList(Arrays.asList(4L, 69L, 345L));

    public static String getNothing() {
        throw new UnsupportedOperationException("should not be called anyways");
    }

    @Test
    final void load() {
        final Loader<LoaderTest> loader = new Loader<>(LoaderTest.class);
        final Sample result = loader.load(new Sample(), this);
        assertEquals(sample, result);
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