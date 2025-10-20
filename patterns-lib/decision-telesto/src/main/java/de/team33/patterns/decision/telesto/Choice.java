package de.team33.patterns.decision.telesto;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @deprecated since 1.26.0 - consider class <em>Variety</em> from module <em>decision-carpo</em> as replacement.
 *
 * @see <a href="https://www.team33.de/dev/patterns/1.x/patterns-lib/decision-carpo/">decision-carpo (1.x)</a>
 * @see <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/decision-carpo/">decision-carpo (2.x)</a>
 */
@Deprecated
public class Choice<P, R> implements Function<P, R> {

    private final Predicate<P> condition;
    private final Function<P, R> positive;
    private final Function<P, R> negative;

    private Choice(final Predicate<P> condition, final Function<P, R> positive, final Function<P, R> negative) {
        this.condition = condition;
        this.positive = positive;
        this.negative = negative;
    }

    @Deprecated
    public static <P> Conditional<P> on(final Predicate<P> condition) {
        return new Conditional<P>(condition);
    }

    @Deprecated
    @Override
    public final R apply(final P parameter) {
        return (condition.test(parameter) ? positive : negative).apply(parameter);
    }

    /**
     * @deprecated since 1.26.0
     */
    @FunctionalInterface
    @Deprecated
    public interface Consequence<P, R> {

        @Deprecated
        Choice<P, R> orApply(Function<P, R> negative);

        @Deprecated
        default Choice<P, R> orReply(final R negative) {
            return orApply(any -> negative);
        }
    }

    /**
     * @deprecated since 1.26.0
     */
    @Deprecated
    public static class Conditional<P> {

        private final Predicate<P> condition;

        private Conditional(final Predicate<P> condition) {
            this.condition = condition;
        }

        @Deprecated
        public final <R> Consequence<P, R> apply(final Function<P, R> positive) {
            return negative -> new Choice<>(condition, positive, negative);
        }

        @Deprecated
        public final <R> Consequence<P, R> reply(final R positive) {
            return apply(any -> positive);
        }
    }
}
