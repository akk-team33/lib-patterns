package de.team33.samples.patterns.production.e1;

import de.team33.patterns.production.e1.FactoryHub;
import de.team33.patterns.production.e1.FactoryUtil;

import java.math.BigInteger;
import java.util.Random;

@SuppressWarnings("WeakerAccess")
public final class FactoryHubSample extends FactoryHub<FactoryHubSample> {

    // Some pre-defined tokens ...
    public static final Byte BYTE = Byte.MAX_VALUE;
    public static final Short SHORT = Short.MAX_VALUE;
    public static final Integer INTEGER = Integer.MAX_VALUE;
    public static final Long LONG = Long.MAX_VALUE;

    private final Random random = new Random();

    // The instantiation takes place via a builder pattern ...
    private FactoryHubSample(final Builder builder) {
        super(builder);
    }

    // To get a builder that has already been pre-initialized
    // with the tokens defined above and corresponding methods ...
    public static Builder builder() {
        return new Builder().setUnknownTokenListener(FactoryUtil.LOG_UNKNOWN_TOKEN)
                            .on(BYTE).apply(context -> context.anyBits(Byte.SIZE).byteValue())
                            .on(SHORT).apply(context -> context.anyBits(Short.SIZE).shortValue())
                            .on(INTEGER).apply(context -> context.anyBits(Integer.SIZE).intValue())
                            .on(LONG).apply(context -> context.anyBits(Long.SIZE).longValue());
    }

    // Implementation of the method that provides the context ...
    @Override
    protected final FactoryHubSample getContext() {
        return this;
    }

    // A basic method to be provided by this context ...
    public final BigInteger anyBits(final int numBits) {
        return new BigInteger(numBits, random);
    }

    // Definition of a Builder for a new instance ...
    public static class Builder extends Collector<FactoryHubSample, Builder> {

        // Implementation of the method that provides the Builder as such for the underlying Collector ...
        @Override
        protected final Builder getBuilder() {
            return this;
        }

        // finally the typical production method of the Builder ...
        public final FactoryHubSample build() {
            return new FactoryHubSample(this);
        }
    }
}
