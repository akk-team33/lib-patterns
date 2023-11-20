/**
 * Provides classes and utilities for implementing or handling decisions.
 * <p>
 * When decisions have to be made between several (more than two) possible results based on several independent
 * criteria, confusing code often results. Example:
 * <p>
 * A choice should be made between five possible results <code>{A, B, C, D, E}</code> based on three independent
 * criteria <code>{k1, k2, k3}</code>, which result from three different input parameters <code>{x, y, z}</code>.
 * <p>
 * A classic normalized implementation might look something like this ...
 * <pre>
 *     // Type of input parameter for criterion k1 ...
 *     public class X {
 *     }
 *
 *     // Type of input parameter for criterion k2 ...
 *     public class Y {
 *     }
 *
 *     // Type of input parameter for criterion k3 ...
 *     public class Z {
 *     }
 *
 *     public enum Result {
 *
 *         A, B, C, D, E;
 *
 *         // Criterion 1 ...
 *         private static final Predicate&lt;X&gt; k1 = ...;
 *
 *         // Criterion 2 ...
 *         private static final Predicate&lt;Y&gt; k2 = ...;
 *
 *         // Criterion 3 ...
 *         private static final Predicate&lt;Z&gt; k3 = ...;
 *
 *         public static Result map(final X x, final Y y, final Z z) {
 *             if (k1.test(x)) {
 *                 if (k2.test(y)) {
 *                     if (k3.test(z)) {
 *                         return A;
 *                     } else {
 *                         return C;
 *                     }
 *                 } else {
 *                     if (k3.test(z)) {
 *                         return B;
 *                     } else {
 *                         return E;
 *                     }
 *                 }
 *             } else {
 *                 if (k2.test(y)) {
 *                     if (k3.test(z)) {
 *                         return D;
 *                     } else {
 *                         return A;
 *                     }
 *                 } else {
 *                     if (k3.test(z)) {
 *                         return D;
 *                     } else {
 *                         return B;
 *                     }
 *                 }
 *             }
 *         }
 *     }
 * </pre>
 * <p>
 * In practice, individual branches of the decision cascade can often be combined, which makes the code a little
 * shorter but hardly clearer. Each additional criterion would roughly double the required code of the decision
 * cascade.
 * <p>
 * The {@link de.team33.patterns.decision.telesto.Choice} class defined in this module allows a different
 * implementation of the same decision cascade, reusing the classes X, Y and Z as input parameters of the
 * criteria ...
 *
 * <pre>
 *     public enum Result {
 *
 *         A, B, C, D, E;
 *
 *         public static Result map(final X x, final Y y, final Z z) {
 *             return Case.K_1_2_3.apply(new Input(x, y, z));
 *         }
 *
 *         private static class Input {
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
 *             // Criterion 1 ...
 *             final boolean isK1() {
 *                 return ...; // check this.x
 *             }
 *
 *             // Criterion 2 ...
 *             final boolean isK2() {
 *                 return ...; // check this.y
 *             }
 *
 *             // Criterion 3 ...
 *             final boolean isK3() {
 *                 return ...; // check this.z
 *             }
 *         }
 *
 *         private enum Case implements Function&lt;Input, Result&gt; {
 *
 *             K_T_T_3(Choice.on(Input::isK3).reply(A).orReply(C)),
 *             K_T_F_3(Choice.on(Input::isK3).reply(B).orReply(E)),
 *             K_F_T_3(Choice.on(Input::isK3).reply(D).orReply(A)),
 *             K_F_F_3(Choice.on(Input::isK3).reply(D).orReply(B)),
 *             K_T_2_3(Choice.on(Input::isK2).apply(K_T_T_3).orApply(K_T_F_3)),
 *             K_F_2_3(Choice.on(Input::isK2).apply(K_F_T_3).orApply(K_F_F_3)),
 *             K_1_2_3(Choice.on(Input::isK1).apply(K_T_2_3).orApply(K_F_2_3));
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
 * <p>
 * The interested programmer can decide for himself whether this is better.
 * In my opinion, clarity and maintainability have been improved.
 * Readability depends largely on whether the concept has been understood.
 *
 * @see <a href="https://de.wikipedia.org/wiki/Telesto_(Mond)">Telesto</a>
 */
package de.team33.patterns.decision.telesto;
