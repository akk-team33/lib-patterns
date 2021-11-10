package de.team33.samples.patterns.features.e1;

import de.team33.patterns.features.e1.FeatureHub;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

class FeatureHubSample implements FeatureHub<FeatureHubSample> {

    static final Feature<Random> RANDOM = sample -> new Random();
    static final Feature<Date> DATE = sample -> new Date();
    static final Feature<List<Throwable>> PROBLEMS = sample -> new LinkedList<>();

    private Supplier<FeatureHub<FeatureHubSample>> innerHub = new Supplier<FeatureHub<FeatureHubSample>>() {
        @SuppressWarnings({"ObjectEquality", "SynchronizedMethod"})
        @Override
        public synchronized FeatureHub<FeatureHubSample> get() {
            if (this == innerHub) {
                final FeatureHub<FeatureHubSample> result = new FeatureMap<>(FeatureHubSample.this);
                innerHub = () -> result;
            }
            return innerHub.get();
        }
    };

    @Override
    public final <R> R get(final Key<FeatureHubSample, R> key) {
        return innerHub.get().get(key);
    }

    final Random getRandom() {
        return get(RANDOM);
    }

    final Date getDate() {
        return get(DATE);
    }

    final List<Throwable> getProblems() {
        return new ArrayList<>(get(PROBLEMS));
    }

    @FunctionalInterface
    interface Feature<R> extends FeatureHub.Key<FeatureHubSample, R> {}
}
