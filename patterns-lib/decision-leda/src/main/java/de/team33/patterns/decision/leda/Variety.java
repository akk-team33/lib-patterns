package de.team33.patterns.decision.leda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
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

    private static final String ILLEGAL_ARGUMENTS = "Max. %d conditions can be handled - but %d are given.";

    private final List<Predicate<? super I>> conditions;

    private Variety(final Collection<? extends Predicate<? super I>> conditions) {
        if (Integer.SIZE >= conditions.size()) {
            this.conditions = Collections.unmodifiableList(new ArrayList<>(conditions));
        } else {
            throw new IllegalArgumentException(String.format(ILLEGAL_ARGUMENTS, Integer.SIZE, conditions.size()));
        }
    }

    @SafeVarargs
    public static <I> Variety<I> joined(final Predicate<I>... conditions) {
        return joined(Arrays.asList(conditions));
    }

    public static <I> Variety<I> joined(final Collection<? extends Predicate<? super I>> conditions) {
        return new Variety<>(conditions);
    }

    public final int scale() {
        return conditions.size();
    }

    public final int bound() {
        return 1 << conditions.size();
    }

    public final int apply(final I value) {
        return IntStream.range(0, conditions.size())
                        .map(i -> conditions.get(i).test(value) ? (1 << i) : 0)
                        .reduce(0, Integer::sum);
    }
}
