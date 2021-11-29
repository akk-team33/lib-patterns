package de.team33.samples.patterns.production.e1;

import de.team33.patterns.production.e1.FactoryHub;
import de.team33.patterns.production.e1.FactoryUtil;

import java.math.BigInteger;
import java.util.Random;

public class FactoryHostSample {

    public static final Byte BYTE = Byte.MAX_VALUE;
    public static final Short SHORT = Short.MAX_VALUE;
    public static final Integer INTEGER = Integer.MAX_VALUE;

    private final Random random = new Random();
    private final FactoryHub<FactoryHostSample> hub;

    public FactoryHostSample() {
        this.hub = FactoryUtil.builder(() -> this)
                              .on(BYTE).apply(host -> host.anyBits(Byte.SIZE).byteValue())
                              .on(SHORT).apply(host -> host.anyBits(Short.SIZE).shortValue())
                              .on(INTEGER).apply(host -> host.anyBits(Integer.SIZE).intValue())
                              .build();
    }

    public final <R> R any(final R token) {
        return hub.get(token);
    }

    public final BigInteger anyBits(final int numBits) {
        return new BigInteger(numBits, random);
    }
}
