package de.team33.samples.patterns.producing.e1;

import de.team33.patterns.producing.e1.FactoryHub;

import java.math.BigInteger;
import java.util.Random;
import java.util.function.Function;

public final class FactoryHubSample extends FactoryHub<FactoryHubSample> {

    // Some pre-defined tokens ...
    public static final Byte BYTE = Byte.MAX_VALUE;
    public static final Short SHORT = Short.MAX_VALUE;
    public static final Integer INTEGER = Integer.MAX_VALUE;

    private final Random random = new Random();

    // The instantiation takes place via a builder pattern ...
    private FactoryHubSample(final Builder builder) {
        super(builder.collector, FactoryHubSample.class);
    }

    // To get a builder that has already been pre-initialized
    // with the tokens defined above and corresponding methods ...
    public static Builder builder() {
        return new Builder().on(BYTE).apply(host -> host.createBits(Byte.SIZE).byteValue())
                            .on(SHORT).apply(host -> host.createBits(Short.SIZE).shortValue())
                            .on(INTEGER).apply(host -> host.createBits(Integer.SIZE).intValue());
    }

    // A basic method to be provided by the context ...
    public final BigInteger createBits(final int numBits) {
        return new BigInteger(numBits, random);
    }

    // Definition of the builder ...
    public static class Builder {

        // ... which in this case contains a FactoryHub.Collector
        //     (it could also be derived from it instead)
        private final FactoryHub.Collector<FactoryHubSample> collector = new FactoryHub.Collector<>();

        // In order to implement the two-stage builder pattern as proposed by the collector,
        // this method delegates to the collector ...
        public final <T> Function<Function<FactoryHubSample, T>, Builder> on(final T token) {
            return collector.on(token, this);
        }

        // finally the typical production method ...
        public final FactoryHubSample build() {
            return new FactoryHubSample(this);
        }
    }
}
