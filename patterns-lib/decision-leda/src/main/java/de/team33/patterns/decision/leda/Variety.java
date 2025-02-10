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

    @SafeVarargs
    public static <I> Variety<I> joined(final Predicate<I>... conditions) {
        return joined(Arrays.asList(conditions));
    }

    public static <I> Variety<I> joined(final Collection<? extends Predicate<? super I>> conditions) {
        return new Variety<>(BitOrder.MSB_FIRST, conditions);
    }

    public final Variety<I> withBitOrder(final BitOrder order) {
        return new Variety<>(order, conditions);
    }

    public final int scale() {
        return conditions.size();
    }

    public final int bound() {
        return 1 << conditions.size();
    }

    public final int apply(final I value) {
        return IntStream.range(0, conditions.size())
                        .map(index -> conditions.get(index).test(value) ? bit(index) : 0)
                        .reduce(0, Integer::sum);
    }

    private int bit(final int index) {
        return bitOp.applyAsInt(index);
    }
}
