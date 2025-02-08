package de.team33.patterns.decision.leda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class PreDecision<T, R> {

    private static final String ILLEGAL_ARGUMENTS =
            "For %d independent conditions, %d possible replies must be defined - but %d are given: %n%n    %s%n";

    private final List<R> results;
    private final IntDecision<T> backing;

    public PreDecision(final List<Predicate<T>> conditions, final List<R> results) {
        final int expectedSize = 1 << conditions.size();
        if (expectedSize == results.size()) {
            this.results = Collections.unmodifiableList(new ArrayList<>(results));
            this.backing = new IntDecision<>(conditions);
        } else {
            throw new IllegalArgumentException(String.format(ILLEGAL_ARGUMENTS, conditions.size(), expectedSize, results.size(), results));
        }
    }

    public interface Cascade<T> {
        <R> PreDecision<T, R> replying(R... results);
    }

    @SafeVarargs
    public static <T> Cascade<T> basedOn(final Predicate<T>... conditions) {
        return new Cascade<T>() {
            @SafeVarargs
            @Override
            public final <R> PreDecision<T, R> replying(final R... results) {
                return new PreDecision<>(Arrays.asList(conditions), Arrays.asList(results));
            }
        };
    }

    public final R apply(final T value) {
        return results.get(backing.apply(value));
    }
}
