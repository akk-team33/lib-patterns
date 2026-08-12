package de.team33.patterns.decision.carpo;

import java.util.*;
import java.util.function.Function;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;
import java.util.stream.IntStream;

/**
 * @see <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/decision-thyone/">decision-thyone</a>
 * @see <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/decision-thyone/apidocs/">decision-thyone/apidocs</a>
 * @deprecated consider module
 * <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/decision-thyone/apidocs/">decision-thyone</a>
 * as a replacement.
 */
@Deprecated
@SuppressWarnings("WeakerAccess")
public final class Variety<I> {

    private static final String TOO_MANY_CRITERIA =
            "Max. %d criteria can be handled - but %d are given.";
    private static final String MISMATCHING_RESULTS =
            "For %d independent criteria, %d possible replies must be defined - but %d are given: %n%n    %s%n";

    private final List<Predicate<? super I>> criteria;
    private final IntUnaryOperator bitOp;

    private Variety(final BitOrder bitOrder, final Collection<? extends Predicate<? super I>> criteria) {
        final int size = criteria.size();
        if (Integer.SIZE < size) {
            throw new IllegalArgumentException(String.format(TOO_MANY_CRITERIA, Integer.SIZE, size));
        } else {
            this.criteria = List.copyOf(criteria);
            this.bitOp = bitOrder.operator(size - 1);
        }
    }

    /**
     * @deprecated see {@link Variety}.
     */
    @Deprecated
    @SuppressWarnings("OverloadedVarargsMethod")
    @SafeVarargs
    public static <I> Variety<I> joined(final Predicate<I>... criteria) {
        return joined(Arrays.asList(criteria));
    }

    /**
     * @deprecated see {@link Variety}.
     */
    @Deprecated
    @SuppressWarnings("OverloadedVarargsMethod")
    @SafeVarargs
    public static <I> Variety<I> joined(final BitOrder bitOrder, final Predicate<I>... criteria) {
        return joined(bitOrder, Arrays.asList(criteria));
    }

    /**
     * @deprecated see {@link Variety}.
     */
    @Deprecated
    public static <I> Variety<I> joined(final Collection<? extends Predicate<? super I>> criteria) {
        return joined(BitOrder.MSB_FIRST, criteria);
    }

    /**
     * @deprecated see {@link Variety}.
     */
    @Deprecated
    public static <I> Variety<I> joined(final BitOrder bitOrder,
                                        final Collection<? extends Predicate<? super I>> criteria) {
        return new Variety<>(bitOrder, criteria);
    }

    /**
     * @deprecated see {@link Variety}.
     */
    @Deprecated
    public final Variety<I> with(final BitOrder order) {
        return new Variety<>(order, criteria);
    }

    /**
     * @deprecated see {@link Variety}.
     */
    @Deprecated
    public final int scale() {
        return criteria.size();
    }

    /**
     * @deprecated see {@link Variety}.
     */
    @Deprecated
    public final int bound() {
        return 1 << criteria.size();
    }

    private int bit(final int index) {
        return bitOp.applyAsInt(index);
    }

    /**
     * @deprecated see {@link Variety}.
     */
    @Deprecated
    public final int apply(final I input) {
        return IntStream.range(0, criteria.size())
                        .map(index -> criteria.get(index).test(input) ? bit(index) : 0)
                        .reduce(0, Integer::sum);
    }

    /**
     * @deprecated see {@link Variety}.
     */
    @Deprecated
    public final Cases.Start<I> on(final int... bitSets) {
        return Choices.start(this).on(bitSets);
    }

    /**
     * @deprecated see {@link Variety}.
     */
    @Deprecated
    @SuppressWarnings("OverloadedVarargsMethod")
    @SafeVarargs
    public final <R> Function<I, R> replying(final R... results) {
        return replying(Arrays.asList(results));
    }

    /**
     * @deprecated see {@link Variety}.
     */
    @Deprecated
    public final <R> Function<I, R> replying(final Collection<? extends R> results) {
        if (bound() == results.size()) {
            //noinspection Java9CollectionFactory
            final List<R> resultList = Collections.unmodifiableList(new ArrayList<>(results));
            return input -> resultList.get(apply(input));
        } else {
            throw new IllegalArgumentException(
                    String.format(MISMATCHING_RESULTS, scale(), bound(), results.size(), results));
        }
    }
}
