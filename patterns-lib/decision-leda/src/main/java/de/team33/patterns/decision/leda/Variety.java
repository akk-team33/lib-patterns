package de.team33.patterns.decision.leda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;
import java.util.stream.IntStream;

/**
 * A tool for distinguishing cases that consist of multiple independent boolean decisions
 * related to an input of a particular type.
 * <p>
 * Use {@link #joined(Predicate[])} to get an instance.
 * <p>
 * The different cases are represented as <code>int</code> values that reflect the individual boolean decisions
 * when interpreted as bit patterns.
 *
 * @param <I> The input type.
 */
public class Variety<I> {

    private static final String TOO_FEW_CONDITIONS = "Min. one condition must be given - but there are none.";
    private static final String TOO_MANY_CONDITIONS = "Max. %d conditions can be handled - but %d are given.";

    private final List<Predicate<? super I>> conditions;
    private final IntUnaryOperator bitOp;

    private Variety(final BitOrder bitOrder, final Collection<? extends Predicate<? super I>> conditions) {
        final int size = conditions.size();
        if (1 > size) {
            throw new IllegalArgumentException(TOO_FEW_CONDITIONS);
        } else if (Integer.SIZE < size) {
            throw new IllegalArgumentException(String.format(TOO_MANY_CONDITIONS, Integer.SIZE, size));
        } else {
            this.conditions = Collections.unmodifiableList(new ArrayList<>(conditions));
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
    public static <I> Variety<I> joined(final BitOrder bitOrder, final Predicate<I>... conditions) {
        return joined(bitOrder, Arrays.asList(conditions));
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
    public static <I> Variety<I> joined(final Collection<? extends Predicate<? super I>> conditions) {
        return joined(BitOrder.MSB_FIRST, conditions);
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
                                        final Collection<? extends Predicate<? super I>> conditions) {
        return new Variety<>(bitOrder, conditions);
    }

    /**
     * Returns a new instance that {@linkplain #apply(Object) applies} <em>this'</em> {@link Predicate}s
     * but the {@link BitOrder} given here.
     */
    public final Variety<I> withBitOrder(final BitOrder order) {
        return new Variety<>(order, conditions);
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
        return conditions.size();
    }

    /**
     * Returns the number of possible different results of {@link #apply(Object)}.
     * Such a result is between <em>0</em> and <em>(bound() - 1)</em>.
     * @return
     */
    public final int bound() {
        return 1 << conditions.size();
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
        return IntStream.range(0, conditions.size())
                        .map(index -> conditions.get(index).test(argument) ? bit(index) : 0)
                        .reduce(0, Integer::sum);
    }
}
