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
 * public enum Result {
 *
 *     A, B, C, D, E;
 *
 *     public static Result map(final X x, final Y y, final Z z) {
 *         if (x.k1()) {
 *             if (y.k2()) {
 *                 if (z.k3()) {
 *                     return A;
 *                 } else {
 *                     return C;
 *                 }
 *             } else {
 *                 if (z.k3()) {
 *                     return B;
 *                 } else {
 *                     return E;
 *                 }
 *             }
 *         } else {
 *             if (y.k2()) {
 *                 if (z.k3()) {
 *                     return D;
 *                 } else {
 *                     return A;
 *                 }
 *             } else {
 *                 if (z.k3()) {
 *                     return D;
 *                 } else {
 *                     return B;
 *                 }
 *             }
 *         }
 *     }
 * }
 * </pre>
 * <p>
 * In practice, individual branches of the decision cascade can often be combined, which makes the code a little
 * shorter but hardly clearer. Each additional criterion would roughly double the required code of the decision
 * cascade.
 * <p>
 * The {@link de.team33.patterns.decision.telesto.Choice} class defined in this module allows a different
 * implementation of the same decision cascade ...
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
 * <p>
 * The interested programmer can decide for himself whether this is better.
 * In my opinion, maintainability have been improved.
 * Readability depends largely on whether the concept has been understood.
 * <p>
 * The private inner class <em>Input</em> is not always or necessarily needed, but it is helpful.
 * As of Java 17, it can be defined as <em>record</em>, which saves some boilerplate code.
 *
 * @see <a href="https://de.wikipedia.org/wiki/Telesto_(Mond)">Telesto</a>
 */
package de.team33.patterns.decision.telesto;
