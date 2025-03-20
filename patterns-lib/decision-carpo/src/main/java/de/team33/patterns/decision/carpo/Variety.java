package de.team33.patterns.decision.carpo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;
import java.util.stream.IntStream;

/**
 * A tool that combines the results of multiple independent boolean criteria
 * relating to an input of a specific type &lt;I&gt; into an int value,
 * which, when interpreted as bit patterns, reflect a combination of the boolean results.
 * <p>
 * Use e.g. {@link #joined(Predicate[])} to get an instance.
 * <p>
 * <b>Example:</b>
 * <pre>
 * public enum Result {
 *
 *     A, B, C, D, E, F, G, H;
 *
 *     private static final Variety&lt;Input&gt; VARIETY = Variety.joined(Input::isC, Input::isB, Input::isA);
 *
 *     public static Result of(final Input input) {
 *         return Result.values()[VARIETY.apply(input)];
 *     }
 * }
 * </pre>
 *
 * @param <I> The input type.
 * @see #apply(Object)
 * @see #joined(Predicate[])
 * @see #joined(BitOrder, Predicate[])
 * @see #joined(Collection)
 * @see #joined(BitOrder, Collection)
 * @see de.team33.patterns.decision.carpo package
 */
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
            //noinspection Java9CollectionFactory
            this.criteria = Collections.unmodifiableList(new ArrayList<>(criteria));
            this.bitOp = bitOrder.operator(size - 1);
        }
    }

    /**
     * Returns a new instance that {@linkplain #apply(Object) applies} an <em>input</em> to the given <em>criteria</em>
     * and sorts their results as bits using {@link BitOrder#MSB_FIRST}.
     *
     * @see #apply(Object)
     * @see #joined(BitOrder, Predicate[])
     * @see #joined(Collection)
     * @see #joined(BitOrder, Collection)
     */
    @SuppressWarnings("OverloadedVarargsMethod")
    @SafeVarargs
    public static <I> Variety<I> joined(final Predicate<I>... criteria) {
        return joined(Arrays.asList(criteria));
    }

    /**
     * Returns a new instance that {@linkplain #apply(Object) applies} an <em>input</em> to the given <em>criteria</em>
     * and sorts their results as bits using the given {@link BitOrder}.
     *
     * @see #apply(Object)
     * @see #joined(Predicate[])
     * @see #joined(Collection)
     * @see #joined(BitOrder, Collection)
     */
    @SuppressWarnings("OverloadedVarargsMethod")
    @SafeVarargs
    public static <I> Variety<I> joined(final BitOrder bitOrder, final Predicate<I>... criteria) {
        return joined(bitOrder, Arrays.asList(criteria));
    }

    /**
     * Returns a new instance that {@linkplain #apply(Object) applies} an <em>input</em> to the given <em>criteria</em>
     * and sorts their results as bits using {@link BitOrder#MSB_FIRST}.
     *
     * @see #apply(Object)
     * @see #joined(Predicate[])
     * @see #joined(BitOrder, Predicate[])
     * @see #joined(BitOrder, Collection)
     */
    public static <I> Variety<I> joined(final Collection<? extends Predicate<? super I>> criteria) {
        return joined(BitOrder.MSB_FIRST, criteria);
    }

    /**
     * Returns a new instance that {@linkplain #apply(Object) applies} an <em>input</em> to the given <em>criteria</em>
     * and sorts their results as bits using the given {@link BitOrder}.
     *
     * @see #apply(Object)
     * @see #joined(Predicate[])
     * @see #joined(BitOrder, Predicate[])
     * @see #joined(Collection)
     */
    public static <I> Variety<I> joined(final BitOrder bitOrder,
                                        final Collection<? extends Predicate<? super I>> criteria) {
        return new Variety<>(bitOrder, criteria);
    }

    /**
     * Returns a new instance that {@linkplain #apply(Object) applies} <em>this'</em> criteria
     * but the {@link BitOrder} given here.
     */
    public final Variety<I> with(final BitOrder order) {
        return new Variety<>(order, criteria);
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
        return criteria.size();
    }

    /**
     * Returns the number of possible different results of {@link #apply(Object)}.
     * Such a result is between <em>0</em> and <em>(bound() - 1)</em> (inclusive).
     */
    public final int bound() {
        return 1 << criteria.size();
    }

    private int bit(final int index) {
        return bitOp.applyAsInt(index);
    }

    /**
     * Evaluates the predicates given during instantiation with the given <em>input</em>,
     * creates a bit pattern from them and returns an {@code int} value that represents this bit pattern.
     * <p>
     * Let <em>scale</em> be the number of given predicates,
     * then the result is a value between <em>0</em> and <em>((2<sup>scale</sup>) - 1)</em>.
     *
     * @see #joined(Predicate[])
     * @see #joined(BitOrder, Predicate[])
     * @see #joined(Collection)
     * @see #joined(BitOrder, Collection)
     */
    public final int apply(final I input) {
        return IntStream.range(0, criteria.size())
                        .map(index -> criteria.get(index).test(input) ? bit(index) : 0)
                        .reduce(0, Integer::sum);
    }

    /**
     * Starts a {@link Cases}/{@link Choices} based build process to finally create a {@link Function}
     * that maps an input of type &lt;I&gt; to a result of a specific type.
     *
     * @see Choices#on(int...)
     */
    public final Cases.Start<I> on(final int... bitSets) {
        return Choices.start(this).on(bitSets);
    }

    /**
     * Returns a new {@link Function} that applies the underlying predicates and bit-order and
     * {@linkplain Function#apply(Object) will return} one of the given results.
     *
     * @see #replying(Collection)
     */
    @SuppressWarnings("OverloadedVarargsMethod")
    @SafeVarargs
    public final <R> Function<I, R> replying(final R... results) {
        return replying(Arrays.asList(results));
    }

    /**
     * Returns a new {@link Function} that applies the underlying predicates and bit-order and
     * {@linkplain Function#apply(Object) will return} one of the given results.
     *
     * @see #replying(Object[])
     */
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
