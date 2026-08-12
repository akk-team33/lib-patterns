package de.team33.patterns.decision.carpo;

import java.util.function.Function;

/**
 * @see <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/decision-thyone/">decision-thyone</a>
 * @see <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/decision-thyone/apidocs/">decision-thyone/apidocs</a>
 * @deprecated consider module
 * <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/decision-thyone/apidocs/">decision-thyone</a>
 * as a replacement.
 */
@Deprecated
@FunctionalInterface
public interface Cases<I, R> {

    /**
     * @deprecated see {@link Cases}.
     */
    @Deprecated
    default Choices<I, R> reply(final R result) {
        return apply(any -> result);
    }

    /**
     * @deprecated see {@link Cases}.
     */
    @Deprecated
    Choices<I, R> apply(final Function<I, R> method);

    /**
     * @deprecated see {@link Cases}.
     */
    @Deprecated
    interface Start<I> {

        /**
         * @deprecated see {@link Cases}.
         */
        @Deprecated
        default <R> Choices<I, R> reply(final R result) {
            return apply(any -> result);
        }

        /**
         * @deprecated see {@link Cases}.
         */
        @Deprecated
        <R> Choices<I, R> apply(final Function<I, R> function);
    }
}
