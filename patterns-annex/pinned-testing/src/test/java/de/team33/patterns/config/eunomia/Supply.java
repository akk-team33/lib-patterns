package de.team33.patterns.config.eunomia;

import de.team33.patterns.arbitrary.mimas.Generator;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class Supply implements Generator {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final AtomicInteger FILE_INDEX = new AtomicInteger(0);

    @Override
    public BigInteger anyBits(final int numBits) {
        return new BigInteger(numBits, RANDOM);
    }

    public final SampleConfig anySampleConfig() {
        return new SampleConfig(anyString(), anyEntry(), anyItemList());
    }

    private SampleConfig.Entry anyEntry() {
        return new SampleConfig.Entry(System.currentTimeMillis(), anyLong());
    }

    private SampleConfig.Item[] anyItemList() {
        return Stream.generate(this::anyItem)
                     .limit(anyInt(10))
                     .toArray(SampleConfig.Item[]::new);
    }

    private SampleConfig.Item anyItem() {
        return new SampleConfig.Item(anyInt(), anyInt());
    }

    public final String anyFileName() {
        return "%04d-%s".formatted(FILE_INDEX.incrementAndGet(), anyString(8, "0123456789ABCDEFGHJKLMNPQRSTUVWXYZ"));
    }
}
