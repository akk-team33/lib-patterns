package de.team33.samples.patterns.production.e1;

import de.team33.patterns.production.e1.FactoryHub;
import de.team33.patterns.production.e1.FactoryUtil;

public class Sample {

    private final FactoryHub<Sample> factoryHub;

    private Sample(final Builder builder) {
        factoryHub = new FactoryHub<Sample>(builder) {
            @Override
            protected Sample getContext() {
                return Sample.this;
            }
        };
    }

    // ...

    public static class Builder extends FactoryHub.Collector<Sample, Builder> {

        @Override
        protected final Builder getBuilder() {
            return this;
        }

        public final Sample build() {
            return new Sample(this);
        }
    }
}
