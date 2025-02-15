package de.team33.patterns.decision.carpo;

import java.util.function.Function;

/**
 * Represents a set of cases to be paired with a single choice.
 *
 * @param <I> an input type.
 * @param <R> a result type.
 * @see de.team33.patterns.decision.carpo package
 */
@FunctionalInterface
public interface Cases<I, R> {

    /**
     * Pairs these cases with a specific <em>result</em>, adds that pairing to the underlying {@link Choices},
     * and returns those {@link Choices}.
     */
    default Choices<I, R> reply(final R result) {
        return apply(any -> result);
    }

    /**
     * Pairs these cases with a specific <em>method</em>, adds that pairing to the underlying {@link Choices},
     * and returns those {@link Choices}.
     */
    Choices<I, R> apply(final Function<I, R> method);

    /**
     * Represents the first set of cases to be paired with a first choice.
     *
     * @param <I> an input type.
     * @see de.team33.patterns.decision.carpo package
     */
    interface Start<I> {

        /**
         * @see Cases#reply(Object)
         */
        default <R> Choices<I, R> reply(final R result) {
            return apply(any -> result);
        }

        /**
         * @see Cases#apply(Function)
         */
        <R> Choices<I, R> apply(final Function<I, R> function);
    }
}
