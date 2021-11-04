package de.team33.samples.patterns.features.e1;

import de.team33.patterns.features.e1.FeatureSet;
import de.team33.patterns.features.e1.Key;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

class FeatureSetSample {

    static final Key<Random> RANDOM = Random::new;
    static final Key<Date> DATE = Date::new;
    static final Key<List<Throwable>> PROBLEMS = LinkedList::new;

    private final FeatureSet features = new FeatureSet();

    final int anyInt() {
        try {
            return features.get(RANDOM).nextInt();
        } catch (final RuntimeException e) {
            features.get(PROBLEMS).add(e);
            return -1;
        }
    }

    final long getTime() {
        try {
            return features.get(DATE).getTime();
        } catch (final RuntimeException e) {
            features.get(PROBLEMS).add(e);
            return -1;
        }
    }

    final List<Throwable> getProblems() {
        return features.get(PROBLEMS);
    }
}
