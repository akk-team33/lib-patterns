package de.team33.samples.patterns.features.e1;

import de.team33.patterns.features.e1.FeatureHub;
import de.team33.patterns.features.e1.FeatureSet;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

class FeatureSetSample implements FeatureHub<FeatureSetSample> {

    static final Key<Random> RANDOM = sample -> new Random();
    static final Key<Date> DATE = sample -> new Date();
    static final Key<List<Throwable>> PROBLEMS = sample -> new LinkedList<>();
    private final FeatureHub<FeatureSetSample> backing;

    FeatureSetSample() {
        backing = new FeatureSet<>(this);
    }

    @Override
    public final <R> R get(final Function<? super FeatureSetSample, ? extends R> key) {
        return backing.get(key);
    }

    final int anyInt() {
        try {
            return get(RANDOM).nextInt();
        } catch (final RuntimeException e) {
            get(PROBLEMS).add(e);
            return -1;
        }
    }

    final long getTime() {
        try {
            return get(DATE).getTime();
        } catch (final RuntimeException e) {
            get(PROBLEMS).add(e);
            return -1;
        }
    }

    final List<Throwable> getProblems() {
        return new ArrayList<>(get(PROBLEMS));
    }

    @FunctionalInterface
    interface Key<R> extends Function<FeatureSetSample, R> {
    }
}
