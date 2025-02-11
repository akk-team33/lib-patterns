package de.team33.patterns.decision.leda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

/**
 * A tool for distinguishing cases that consist of multiple independent boolean decisions
 * related to an input of a particular type.
 * <p>
 * Use e.g. {@link #joined(Predicate[])} and {@link Stage#replying(Object[])} to get an instance.
 *
 * @param <I> The input type.
 * @param <R> The result type.
 * @see #apply(Object)
 * @see #joined(Predicate[])
 * @see #joined(BitOrder, Predicate[])
 * @see #joined(Collection)
 * @see #joined(BitOrder, Collection)
 * @see Stage#replying(Object[])
 * @see Stage#replying(Collection)
 */
public class Variety<I, R> {

    private static final String ILLEGAL_ARGUMENTS =
            "For %d independent conditions, %d possible replies must be defined - but %d are given: %n%n    %s%n";

    private final IntVariety<I> backing;
    private final List<R> results;

    private Variety(final IntVariety<I> backing, final Collection<? extends R> results) {
        this.backing = backing;
        if (backing.bound() == results.size()) {
            this.results = Collections.unmodifiableList(new ArrayList<>(results));
        } else {
            throw new IllegalArgumentException(String.format(ILLEGAL_ARGUMENTS, backing.scale(), backing.bound(), results.size(), results));
        }
    }

    /**
     * Represents a preliminary stage of instantiating a {@link Variety}.
     * <p>
     * Use e.g. {@link #joined(Predicate[])} to get an instance.
     * <p>
     * Use {@link #replying(Object[])} or {@link #replying(Collection)} to finally get a {@link Variety}.
     *
     * @param <I> The input type.
     * @see #joined(Predicate[])
     * @see #joined(BitOrder, Predicate[])
     * @see #joined(Collection)
     * @see #joined(BitOrder, Collection)
     */
    public static abstract class Stage<I> {

        /**
         * Returns a new {@link Variety} that applies the underlying predicates and bit-order and
         * {@linkplain Variety#apply(Object) will return} one of the given results.
         *
         * @see #replying(Collection)
         * @see Variety#apply(Object)
         */
        @SafeVarargs
        public final <R> Variety<I, R> replying(final R... results) {
            return replying(Arrays.asList(results));
        }

        /**
         * Returns a new {@link Variety} that applies the underlying predicates and bit-order and
         * {@linkplain Variety#apply(Object) will return} one of the given results.
         *
         * @see #replying(Object[])
         * @see Variety#apply(Object)
         */
        public abstract <R> Variety<I, R> replying(Collection<? extends R> results);
    }

    /**
     * Returns a new {@link Stage} that {@linkplain #apply(Object) applies} the given {@link Predicate}s
     * and {@link BitOrder#MSB_FIRST}.
     *
     * @see #apply(Object)
     * @see #joined(BitOrder, Predicate[])
     * @see #joined(Collection)
     * @see #joined(BitOrder, Collection)
     * @see Stage#replying(Object[])
     * @see Stage#replying(Collection)
     */
    @SafeVarargs
    public static <I> Stage<I> joined(final Predicate<I>... predicates) {
        return joined(Arrays.asList(predicates));
    }

    /**
     * Returns a new {@link Stage} that {@linkplain #apply(Object) applies} the given {@link Predicate}s
     * and the given {@link BitOrder}.
     *
     * @see #apply(Object)
     * @see #joined(Predicate[])
     * @see #joined(Collection)
     * @see #joined(BitOrder, Collection)
     */
    @SafeVarargs
    public static <I> Stage<I> joined(final BitOrder bitOrder, final Predicate<I>... conditions) {
        return joined(bitOrder, Arrays.asList(conditions));
    }

    /**
     * Returns a new {@link Stage} that {@linkplain #apply(Object) applies} the given {@link Predicate}s
     * and {@link BitOrder#MSB_FIRST}.
     *
     * @see #apply(Object)
     * @see #joined(Predicate[])
     * @see #joined(BitOrder, Predicate[])
     * @see #joined(BitOrder, Collection)
     */
    public static <I> Stage<I> joined(final Collection<? extends Predicate<? super I>> conditions) {
        return joined(BitOrder.MSB_FIRST, conditions);
    }

    /**
     * Returns a new {@link Stage} that {@linkplain #apply(Object) applies} the given {@link Predicate}s
     * and the given {@link BitOrder}.
     *
     * @see #apply(Object)
     * @see #joined(Predicate[])
     * @see #joined(BitOrder, Predicate[])
     * @see #joined(Collection)
     */
    public static <I> Stage<I> joined(final BitOrder bitOrder,
                                      final Collection<? extends Predicate<? super I>> conditions) {
        return new Stage<I>() {
            @Override
            public <R> Variety<I, R> replying(final Collection<? extends R> results) {
                return new Variety<>(IntVariety.joined(bitOrder, conditions), results);
            }
        };
    }

    /**
     * Returns a new instance that {@linkplain #apply(Object) applies} <em>this'</em> {@link Predicate}s
     * but the {@link BitOrder} given here.
     */
    public final Variety<I, R> with(final BitOrder order) {
        return new Variety<>(backing.with(order), results);
    }

    /**
     * Returns the number of predicates assigned at initialization.
     *
     * @see #joined(Predicate[])
     * @see #joined(BitOrder, Predicate[])
     * @see #joined(Collection)
     * @see #joined(BitOrder, Collection)
     */
    public final int scale() {
        return backing.scale();
    }

    /**
     * Returns the number of possible different results of {@link #apply(Object)} assigned at initialization.
     *
     * @see Stage#replying(Object[])
     * @see Stage#replying(Collection)
     */
    public final int size() {
        return results.size();
    }

    /**
     * Evaluates the underlying predicates (specified during instantiation) with the given argument,
     * creates a bit pattern from the results, uses this bit pattern as an index,
     * and finally returns a value (also specified during instantiation) associated with this index.
     *
     * @see #joined(Predicate[])
     * @see #joined(BitOrder, Predicate[])
     * @see #joined(Collection)
     * @see #joined(BitOrder, Collection)
     * @see Stage#replying(Object[])
     * @see Stage#replying(Collection)
     */
    public final R apply(final I argument) {
        return results.get(backing.apply(argument));
    }
}
