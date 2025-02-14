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
 * A tool for distinguishing cases that consist of multiple independent boolean decisions
 * related to an input of a particular type.
 * <p>
 * Use e.g. {@link #joined(Predicate[])} to get an instance.
 * <p>
 * The different cases are represented as <code>int</code> values that reflect the individual boolean decisions
 * when interpreted as bit patterns.
 *
 * @param <I> The input type.
 * @see #apply(Object)
 * @see #joined(Predicate[])
 * @see #joined(BitOrder, Predicate[])
 * @see #joined(Collection)
 * @see #joined(BitOrder, Collection)
 */
public class Variety<I> {

    private static final String TOO_MANY_CONDITIONS =
            "Max. %d predicates can be handled - but %d are given.";
    private static final String MISMATCHING_RESULTS =
            "For %d independent conditions, %d possible replies must be defined - but %d are given: %n%n    %s%n";

    private final List<Predicate<? super I>> predicates;
    private final IntUnaryOperator bitOp;

    private Variety(final BitOrder bitOrder, final Collection<? extends Predicate<? super I>> predicates) {
        final int size = predicates.size();
        if (Integer.SIZE < size) {
            throw new IllegalArgumentException(String.format(TOO_MANY_CONDITIONS, Integer.SIZE, size));
        } else {
            this.predicates = Collections.unmodifiableList(new ArrayList<>(predicates));
            this.bitOp = bitOrder.operator(size - 1);
        }
    }

    /**
     * Returns a new instance that {@linkplain #apply(Object) applies} the given {@link Predicate}s
     * and {@link BitOrder#MSB_FIRST}.
     *
     * @see #apply(Object)
     * @see #joined(BitOrder, Predicate[])
     * @see #joined(Collection)
     * @see #joined(BitOrder, Collection)
     */
    @SafeVarargs
    public static <I> Variety<I> joined(final Predicate<I>... conditions) {
        return joined(Arrays.asList(conditions));
    }

    /**
     * Returns a new instance that {@linkplain #apply(Object) applies} the given {@link Predicate}s
     * and the given {@link BitOrder}.
     *
     * @see #apply(Object)
     * @see #joined(Predicate[])
     * @see #joined(Collection)
     * @see #joined(BitOrder, Collection)
     */
    @SafeVarargs
    public static <I> Variety<I> joined(final BitOrder bitOrder, final Predicate<I>... predicates) {
        return joined(bitOrder, Arrays.asList(predicates));
    }

    /**
     * Returns a new instance that {@linkplain #apply(Object) applies} the given {@link Predicate}s
     * and {@link BitOrder#MSB_FIRST}.
     *
     * @see #apply(Object)
     * @see #joined(Predicate[])
     * @see #joined(BitOrder, Predicate[])
     * @see #joined(BitOrder, Collection)
     */
    public static <I> Variety<I> joined(final Collection<? extends Predicate<? super I>> predicates) {
        return joined(BitOrder.MSB_FIRST, predicates);
    }

    /**
     * Returns a new instance that {@linkplain #apply(Object) applies} the given {@link Predicate}s
     * and the given {@link BitOrder}.
     *
     * @see #apply(Object)
     * @see #joined(Predicate[])
     * @see #joined(BitOrder, Predicate[])
     * @see #joined(Collection)
     */
    public static <I> Variety<I> joined(final BitOrder bitOrder,
                                        final Collection<? extends Predicate<? super I>> predicates) {
        return new Variety<>(bitOrder, predicates);
    }

    /**
     * Returns a new instance that {@linkplain #apply(Object) applies} <em>this'</em> {@link Predicate}s
     * but the {@link BitOrder} given here.
     */
    public final Variety<I> with(final BitOrder order) {
        return new Variety<>(order, predicates);
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
        return predicates.size();
    }

    /**
     * Returns the number of possible different results of {@link #apply(Object)}.
     * Such a result is between <em>0</em> and <em>(bound() - 1)</em>.
     */
    public final int bound() {
        return 1 << predicates.size();
    }

    private int bit(final int index) {
        return bitOp.applyAsInt(index);
    }

    /**
     * Evaluates the predicates given during instantiation with the given <em>argument</em>,
     * creates a bit pattern from them and returns an <code>int</code> value that represents this bit pattern.
     * <p>
     * Let <em>scale</em> be the number of given predicates,
     * then the result is a value between <em>0</em> and <em>((2<sup>scale</sup>) - 1)</em>.
     *
     * @see #joined(Predicate[])
     * @see #joined(BitOrder, Predicate[])
     * @see #joined(Collection)
     * @see #joined(BitOrder, Collection)
     */
    public final int apply(final I argument) {
        return IntStream.range(0, predicates.size())
                        .map(index -> predicates.get(index).test(argument) ? bit(index) : 0)
                        .reduce(0, Integer::sum);
    }

    /**
     * Starts a {@link Cases}/{@link Choices} based build process to finally create a {@link Function}
     * that maps an input of type &lt;I&gt; to a result of type &lt;R&gt;.
     */
    public final Choices.Start<I> choices() {
        return Choices.start(this);
    }

    /**
     * Returns a new {@link Function} that applies the underlying predicates and bit-order and
     * {@linkplain Function#apply(Object) will return} one of the given results.
     */
    @SafeVarargs
    public final <R> Function<I, R> replying(final R... results) {
        return replying(Arrays.asList(results));
    }

    /**
     * Returns a new {@link Function} that applies the underlying predicates and bit-order and
     * {@linkplain Function#apply(Object) will return} one of the given results.
     */
    public final <R> Function<I, R> replying(final Collection<? extends R> results) {
        if (bound() == results.size()) {
            final List<R> resultList = Collections.unmodifiableList(new ArrayList<>(results));
            return input -> resultList.get(apply(input));
        } else {
            throw new IllegalArgumentException(
                    String.format(MISMATCHING_RESULTS, scale(), bound(), results.size(), results));
        }
    }
}
