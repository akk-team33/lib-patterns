package de.team33.patterns.decision.leda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class ProVariety<I, R> {

    private static final String ILLEGAL_ARGUMENTS =
            "For %d independent conditions, %d possible replies must be defined - but %d are given: %n%n    %s%n";

    private final Variety<I> backing;
    private final List<R> results;

    private ProVariety(final Variety<I> backing, final Collection<? extends R> results) {
        this.backing = backing;
        if (backing.bound() == results.size()) {
            this.results = Collections.unmodifiableList(new ArrayList<>(results));
        } else {
            throw new IllegalArgumentException(String.format(ILLEGAL_ARGUMENTS, backing.scale(), backing.bound(), results.size(), results));
        }
    }

    public static abstract class Stage<I> {
        @SafeVarargs
        public final <R> ProVariety<I, R> replying(final R... results) {
            return replying(Arrays.asList(results));
        }

        public abstract <R> ProVariety<I, R> replying(Collection<? extends R> results);
    }

    @SafeVarargs
    public static <I> Stage<I> joined(final Predicate<I>... conditions) {
        return joined(Arrays.asList(conditions));
    }

    @SafeVarargs
    public static <I> Stage<I> joined(final BitOrder bitOrder, final Predicate<I>... conditions) {
        return joined(bitOrder, Arrays.asList(conditions));
    }

    public static <I> Stage<I> joined(final Collection<? extends Predicate<? super I>> conditions) {
        return joined(BitOrder.MSB_FIRST, conditions);
    }

    public static <I> Stage<I> joined(final BitOrder bitOrder,
                                      final Collection<? extends Predicate<? super I>> conditions) {
        return new Stage<I>() {
            @Override
            public <R> ProVariety<I, R> replying(final Collection<? extends R> results) {
                return new ProVariety<>(Variety.joined(bitOrder, conditions), results);
            }
        };
    }

    public final R apply(final I value) {
        return results.get(backing.apply(value));
    }

    /**
     * Returns a new instance that {@linkplain #apply(Object) applies} <em>this'</em> {@link Predicate}s
     * but the {@link BitOrder} given here.
     */
    public final ProVariety<I, R> with(final BitOrder order) {
        return new ProVariety<>(backing.with(order), results);
    }
}
