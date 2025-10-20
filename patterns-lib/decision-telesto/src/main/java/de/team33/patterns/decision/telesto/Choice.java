package de.team33.patterns.decision.telesto;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A functional implementation of a simple decision.
 * <p>
 * Typically used to implement multiple choices, e.g. based on an enum:
 * <pre>
 * public enum Result {
 *
 *     A, B, C, D, E;
 *
 *     public static Result map(final X x, final Y y, final Z z) {
 *         return Case.HEAD.apply(new Input(x, y, z));
 *     }
 *
 *     private enum Case implements Function&lt;Input, Result&gt; {
 *
 *         CASE_11_(Choice.on(Input::k3).reply(A).orReply(C)),
 *         CASE_10_(Choice.on(Input::k3).reply(B).orReply(E)),
 *         CASE_01_(Choice.on(Input::k3).reply(D).orReply(A)),
 *         CASE_00_(Choice.on(Input::k3).reply(D).orReply(B)),
 *         CASE_1__(Choice.on(Input::k2).apply(CASE_11_).orApply(CASE_10_)),
 *         CASE_0__(Choice.on(Input::k2).apply(CASE_01_).orApply(CASE_00_)),
 *         HEAD(Choice.on(Input::k1).apply(CASE_1__).orApply(CASE_0__));
 *
 *         private final Function&lt;Input, Result&gt; backing;
 *
 *         Case(Function&lt;Input, Result&gt; backing) {
 *             this.backing = backing;
 *         }
 *
 *         &#064;Override
 *         public Result apply(Input input) {
 *             return backing.apply(input);
 *         }
 *     }
 *
 *     private static class Input {
 *
 *         final X x;
 *         final Y y;
 *         final Z z;
 *
 *         Input(final X x, final Y y, final Z z) {
 *             this.x = x;
 *             this.y = y;
 *             this.z = z;
 *         }
 *
 *         final boolean k1() {
 *             return x.k1();
 *         }
 *
 *         final boolean k2() {
 *             return y.k2();
 *         }
 *
 *         final boolean k3() {
 *             return z.k3();
 *         }
 *     }
 * }
 * </pre>
 *
 * @param <P> The parameter type
 * @param <R> The result type
 * @see de.team33.patterns.decision.telesto package
 */
public class Choice<P, R> implements Function<P, R> {

    private final Predicate<P> condition;
    private final Function<P, R> positive;
    private final Function<P, R> negative;

    private Choice(final Predicate<P> condition, final Function<P, R> positive, final Function<P, R> negative) {
        this.condition = condition;
        this.positive = positive;
        this.negative = negative;
    }

    /**
     * Creates and returns a {@link Conditional} based on a given {@link Predicate condition}.
     * The first stage to finally build a {@link Choice}.
     */
    public static <P> Conditional<P> on(final Predicate<P> condition) {
        return new Conditional<P>(condition);
    }

    @Override
    public final R apply(final P parameter) {
        return (condition.test(parameter) ? positive : negative).apply(parameter);
    }

    /**
     * Represents the second stage to finally build a {@link Choice}.
     * <p>
     * It "knows" the condition as well as its positive reaction and allows defining a negative reaction
     * in case the condition is false.
     */
    @FunctionalInterface
    public interface Consequence<P, R> {

        /**
         * Finally creates and returns a {@link Choice} based on a given {@link Function negative reaction}.
         */
        Choice<P, R> orApply(Function<P, R> negative);

        /**
         * Finally creates and returns a {@link Choice} based on a given fixed value presumed as negative result.
         */
        default Choice<P, R> orReply(final R negative) {
            return orApply(any -> negative);
        }
    }

    /**
     * Represents the first stage to finally build a {@link Choice}.
     * <p>
     * It "knows" the condition and allows defining a consequence in case the condition is true.
     */
    public static class Conditional<P> {

        private final Predicate<P> condition;

        private Conditional(final Predicate<P> condition) {
            this.condition = condition;
        }

        /**
         * Creates and returns a {@link Consequence} based on a given {@link Function positive reaction}.
         * The second stage to finally build a {@link Choice}.
         */
        public final <R> Consequence<P, R> apply(final Function<P, R> positive) {
            return negative -> new Choice<>(condition, positive, negative);
        }

        /**
         * Creates and returns a {@link Consequence} based on a given fixed value presumed as positive result.
         * The second stage to finally build a {@link Choice}.
         */
        public final <R> Consequence<P, R> reply(final R positive) {
            return apply(any -> positive);
        }
    }
}
