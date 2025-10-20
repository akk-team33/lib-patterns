package de.team33.patterns.decision.thyone;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public class Distinction<I> {

    private final List<Predicate<? super I>> criteria;

    private Distinction(final List<Predicate<? super I>> criteria) {
        this.criteria = criteria;
    }

    @SafeVarargs
    public static <T> Distinction<T> chain(final Predicate<T> ... criteria) {
        return new Distinction<>(List.of(criteria));
    }

    public static <T> Distinction<T> chain(final Collection<? extends Predicate<? super T>> criteria) {
        return new Distinction<>(List.copyOf(criteria));
    }

    public int apply(final I input) {
        final int bound = criteria.size();
        for (int index = 0; index < bound; ++index) {
            if (criteria.get(index).test(input))
                return index;
        }
        return bound;
    }
}
