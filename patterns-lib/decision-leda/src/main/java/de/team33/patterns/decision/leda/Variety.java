package de.team33.patterns.decision.leda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

/**
 * @deprecated since 1.26.0 - consider module <em>decision-carpo</em> as replacement.
 *
 * @see <a href="https://www.team33.de/dev/patterns/1.x/patterns-lib/decision-carpo/">decision-carpo (1.x)</a>
 * @see <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/decision-carpo/">decision-carpo (2.x)</a>
 */
@Deprecated
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
     * @deprecated since 1.26.0 - consider module <em>decision-carpo</em> as replacement.
     *
     * @see <a href="https://www.team33.de/dev/patterns/1.x/patterns-lib/decision-carpo/">decision-carpo (1.x)</a>
     * @see <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/decision-carpo/">decision-carpo (2.x)</a>
     */
    @Deprecated
    public static abstract class Stage<I> {

        @Deprecated
        @SafeVarargs
        public final <R> Variety<I, R> replying(final R... results) {
            return replying(Arrays.asList(results));
        }

        @Deprecated
        public abstract <R> Variety<I, R> replying(Collection<? extends R> results);
    }

    @Deprecated
    @SafeVarargs
    public static <I> Stage<I> joined(final Predicate<I>... predicates) {
        return joined(Arrays.asList(predicates));
    }

    @Deprecated
    @SafeVarargs
    public static <I> Stage<I> joined(final BitOrder bitOrder, final Predicate<I>... conditions) {
        return joined(bitOrder, Arrays.asList(conditions));
    }

    @Deprecated
    public static <I> Stage<I> joined(final Collection<? extends Predicate<? super I>> conditions) {
        return joined(BitOrder.MSB_FIRST, conditions);
    }

    @Deprecated
    public static <I> Stage<I> joined(final BitOrder bitOrder,
                                      final Collection<? extends Predicate<? super I>> conditions) {
        return new Stage<I>() {
            @Override
            public <R> Variety<I, R> replying(final Collection<? extends R> results) {
                return new Variety<>(IntVariety.joined(bitOrder, conditions), results);
            }
        };
    }

    @Deprecated
    public final Variety<I, R> with(final BitOrder order) {
        return new Variety<>(backing.with(order), results);
    }

    @Deprecated
    public final int scale() {
        return backing.scale();
    }

    @Deprecated
    public final int size() {
        return results.size();
    }

    @Deprecated
    public final R apply(final I argument) {
        return results.get(backing.apply(argument));
    }
}
