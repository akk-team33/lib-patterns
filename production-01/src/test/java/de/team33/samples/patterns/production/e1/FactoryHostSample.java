package de.team33.samples.patterns.production.e1;

import de.team33.patterns.production.e1.FactoryHub;

import java.math.BigInteger;
import java.util.Random;
import java.util.function.Function;

public class FactoryHostSample {

    public static final Byte BYTE = Byte.MAX_VALUE;
    public static final Short SHORT = Short.MAX_VALUE;
    public static final Integer INTEGER = Integer.MAX_VALUE;

    private final Random random = new Random();
    private final FactoryHub<FactoryHostSample> hub;

    private FactoryHostSample(final Builder builder) {
        this.hub = new FactoryHub<>(builder.collector, () -> this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public final <R> R any(final R token) {
        return hub.another(token);
    }

    public final BigInteger anyBits(final int numBits) {
        return new BigInteger(numBits, random);
    }

    public static class Builder {

        private final FactoryHub.Collector<FactoryHostSample> collector;

        private Builder() {
            this.collector = new FactoryHub.Collector<FactoryHostSample>()
                    .on(BYTE).apply(host -> host.anyBits(Byte.SIZE).byteValue())
                    .on(SHORT).apply(host -> host.anyBits(Short.SIZE).shortValue())
                    .on(INTEGER).apply(host -> host.anyBits(Integer.SIZE).intValue());
        }

        public final <R> Function<Function<FactoryHostSample, R>, Builder> on(final R token) {
            return collector.on(token, this);
        }

        public final FactoryHostSample build() {
            return new FactoryHostSample(this);
        }
    }
}
