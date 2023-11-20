package de.team33.patterns.decision.telesto;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A functional implementation of a simple decision.
 * <p>
 * Typically used to implement multiple choices, e.g. based on an enum:
 * <pre>
 *     public enum Result {
 *
 *         A, B, C, D, E;
 *
 *         public static Result map(final X x, final Y y, final Z z) {
 *             return Case.CASE_x_y_z.apply(new Input(x, y, z));
 *         }
 *
 *         private static class Input {
 *
 *             private static final SamplePlain.Y Y0 = new SamplePlain.Y();
 *
 *             final X x;
 *             final Y y;
 *             final Z z;
 *
 *             Input(final X x, final Y y, final Z z) {
 *                 this.x = x;
 *                 this.y = y;
 *                 this.z = z;
 *             }
 *
 *             final boolean isX() {
 *                 return x.toString().isEmpty();
 *             }
 *
 *             final boolean isY() {
 *                 return y.equals(Y0);
 *             }
 *
 *             final boolean isZ() {
 *                 return z.toString().equals("abc");
 *             }
 *         }
 *
 *         private enum Case implements Function&lt;Input, Result&gt; {
 *
 *             CASE_T_T_z(Choice.on(Input::isZ).reply(A).orReply(C)),
 *             CASE_T_F_z(Choice.on(Input::isZ).reply(B).orReply(E)),
 *             CASE_F_T_z(Choice.on(Input::isZ).reply(D).orReply(A)),
 *             CASE_F_F_z(Choice.on(Input::isZ).reply(D).orReply(B)),
 *             CASE_T_y_z(Choice.on(Input::isY).apply(CASE_T_T_z).orApply(CASE_T_F_z)),
 *             CASE_F_y_z(Choice.on(Input::isY).apply(CASE_F_T_z).orApply(CASE_F_F_z)),
 *             CASE_x_y_z(Choice.on(Input::isX).apply(CASE_T_y_z).orApply(CASE_F_y_z));
 *
 *             private final Function&lt;Input, Result&gt; backing;
 *
 *             Case(Function&lt;Input, Result&gt; backing) {
 *                 this.backing = backing;
 *             }
 *
 *             &#064;Override
 *             public Result apply(Input input) {
 *                 return backing.apply(input);
 *             }
 *         }
 *     }
 * </pre>
 *
 * @param <P> The parameter type
 * @param <R> The result type
 *
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
     * Starts the creation of a {@link Choice} giving a {@link Predicate condition}.
     */
    public static <P> Condition<P> on(final Predicate<P> condition) {
        return new Condition<P>(condition);
    }

    @Override
    public final R apply(final P parameter) {
        return (condition.test(parameter) ? positive : negative).apply(parameter);
    }

    /**
     * Represents the preliminary stage of a {@link Choice} that is only missing a negative result.
     */
    @FunctionalInterface
    public interface Consequence<P, R> {

        /**
         * Specifies a {@link Function} that will be applied when the associated {@link Predicate condition} is false
         * to a parameter.
         */
        Choice<P, R> orApply(Function<P, R> negative);

        /**
         * Specifies a fixed result that will be returned when the associated {@link Predicate condition} is false to
         * a parameter.
         */
        default Choice<P, R> orReply(final R negative) {
            return orApply(any -> negative);
        }
    }

    /**
     * Represents the start of the creation of a {@link Choice} on a given {@link Predicate condition}.
     */
    public static class Condition<P> {

        private final Predicate<P> condition;

        private Condition(final Predicate<P> condition) {
            this.condition = condition;
        }

        /**
         * Specifies a {@link Function} that will be applied when the associated {@link Predicate condition} is true to
         * a parameter.
         */
        public final <R> Consequence<P, R> apply(final Function<P, R> positive) {
            return negative -> new Choice<>(condition, positive, negative);
        }

        /**
         * Specifies a fixed result that will be returned when the associated {@link Predicate condition} is true to
         * a parameter.
         */
        public final <R> Consequence<P, R> reply(final R positive) {
            return apply(any -> positive);
        }
    }
}
