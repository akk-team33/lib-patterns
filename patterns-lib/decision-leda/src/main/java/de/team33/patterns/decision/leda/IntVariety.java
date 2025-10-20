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
 * @deprecated since 1.26.0 - consider module <em>decision-carpo</em> as replacement.
 *
 * @see <a href="https://www.team33.de/dev/patterns/1.x/patterns-lib/decision-carpo/">decision-carpo (1.x)</a>
 * @see <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/decision-carpo/">decision-carpo (2.x)</a>
 */
@Deprecated
public class IntVariety<I> {

    private static final String TOO_MANY_CONDITIONS = "Max. %d conditions can be handled - but %d are given.";

    private final List<Predicate<? super I>> conditions;
    private final IntUnaryOperator bitOp;

    private IntVariety(final BitOrder bitOrder, final Collection<? extends Predicate<? super I>> conditions) {
        final int size = conditions.size();
        if (Integer.SIZE < size) {
            throw new IllegalArgumentException(String.format(TOO_MANY_CONDITIONS, Integer.SIZE, size));
        } else {
            this.conditions = Collections.unmodifiableList(new ArrayList<>(conditions));
            this.bitOp = bitOrder.operator(size - 1);
        }
    }

    @Deprecated
    @SafeVarargs
    public static <I> IntVariety<I> joined(final Predicate<I>... conditions) {
        return joined(Arrays.asList(conditions));
    }

    @Deprecated
    @SafeVarargs
    public static <I> IntVariety<I> joined(final BitOrder bitOrder, final Predicate<I>... conditions) {
        return joined(bitOrder, Arrays.asList(conditions));
    }

    @Deprecated
    public static <I> IntVariety<I> joined(final Collection<? extends Predicate<? super I>> conditions) {
        return joined(BitOrder.MSB_FIRST, conditions);
    }

    @Deprecated
    public static <I> IntVariety<I> joined(final BitOrder bitOrder,
                                           final Collection<? extends Predicate<? super I>> conditions) {
        return new IntVariety<>(bitOrder, conditions);
    }

    @Deprecated
    public final IntVariety<I> with(final BitOrder order) {
        return new IntVariety<>(order, conditions);
    }

    @Deprecated
    public final int scale() {
        return conditions.size();
    }

    @Deprecated
    public final int bound() {
        return 1 << conditions.size();
    }

    private int bit(final int index) {
        return bitOp.applyAsInt(index);
    }

    @Deprecated
    public final int apply(final I argument) {
        return IntStream.range(0, conditions.size())
                        .map(index -> conditions.get(index).test(argument) ? bit(index) : 0)
                        .reduce(0, Integer::sum);
    }
}
