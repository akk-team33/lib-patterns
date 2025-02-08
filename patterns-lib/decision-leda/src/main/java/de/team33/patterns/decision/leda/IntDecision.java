package de.team33.patterns.decision.leda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class IntDecision<T> {

    private final List<Predicate<T>> conditions;

    IntDecision(final List<Predicate<T>> conditions) {
        this.conditions = Collections.unmodifiableList(new ArrayList<>(conditions));
    }

    public static <T> IntDecision<T> basedOn(final Predicate<T>... conditions) {
        return new IntDecision<>(Arrays.asList(conditions));
    }

    public final int apply(final T value) {
        return IntStream.range(0, conditions.size())
                        .map(i -> conditions.get(i).test(value) ? (1 << i) : 0)
                        .reduce(0, Integer::sum);
    }
}
