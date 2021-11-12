package de.team33.samples.patterns.producing.e1;

import de.team33.patterns.producing.e1.FactoryHub;

import java.util.function.Function;

public class Sample {

    private final FactoryHub<Sample> factoryHub;

    private Sample(final Builder builder) {
        factoryHub = new FactoryHub<>(builder, () -> this);
    }

    // ...

    public static class Builder extends FactoryHub.Collector<Sample> {

        @Override
        public final <T> Function<Function<Sample, T>, Builder> on(final T template) {
            return on(template, this);
        }

        public final Sample build() {
            return new Sample(this);
        }
    }
}
