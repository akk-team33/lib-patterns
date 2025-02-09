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
    private final Variety<T> backing;

    public PreDecision(final Variety<T> backing, final List<R> results) {
        if (backing.bound() == results.size()) {
            this.results = Collections.unmodifiableList(new ArrayList<>(results));
            this.backing = backing;
        } else {
            throw new IllegalArgumentException(String.format(ILLEGAL_ARGUMENTS, backing.scale(), backing.bound(), results.size(), results));
        }
    }

    public static abstract class Cascade<T> {
        @SafeVarargs
        public final <R> PreDecision<T, R> replying(final R... results) {
            return replying(Arrays.asList(results));
        }

        public abstract <R> PreDecision<T, R> replying(List<R> results);
    }

    @SafeVarargs
    public static <T> Cascade<T> basedOn(final Predicate<T>... conditions) {
        return new Cascade<T>() {
            @Override
            public <R> PreDecision<T, R> replying(final List<R> results) {
                return new PreDecision<>(Variety.joined(conditions), results);
            }
        };
    }

    public final R apply(final T value) {
        return results.get(backing.apply(value));
    }
}
