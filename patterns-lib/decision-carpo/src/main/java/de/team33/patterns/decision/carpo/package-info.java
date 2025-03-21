/**
 * Provides classes and utilities for implementing or handling complex decisions.
 * <p>
 * When decisions have to be made between several (more than two) possible results based on several independent
 * boolean criteria, confusing code often results. Example:
 * <p>
 * Given an <em>Input</em> parameter that offers three independent criteria ...
 * <pre>
 * public interface Input {
 *
 *     boolean isA();
 *
 *     boolean isB();
 *
 *     boolean isC();
 * }
 * </pre>
 * <p>
 * Now a choice should be made between five possible results <code>{A, B, C, D, E}</code> based on the
 * criteria of the <em>Input</em> parameter ...
 * <p>
 * A classic normalized implementation might look something like this ...
 * <pre>
 * public enum Result {
 *
 *     A, B, C, D, E;
 *
 *     public static Result map(final Input input) {
 *         if (input.isC()) {
 *             if (input.isB()) {
 *                 if (input.isA()) {
 *                     return C;
 *                 } else {
 *                     return D;
 *                 }
 *             } else {
 *                 if (input.isA()) {
 *                     return A;
 *                 } else {
 *                     return B;
 *                 }
 *             }
 *         } else {
 *             if (input.isB()) {
 *                 if (input.isA()) {
 *                     return B;
 *                 } else {
 *                     return C;
 *                 }
 *             } else {
 *                 if (input.isA()) {
 *                     return E;
 *                 } else {
 *                     return A;
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
 * The classes defined in this module allow a different implementation of the same decision cascade ...
 * <pre>
 * public enum Result {
 *
 *     A, B, C, D, E;
 *
 *     private static final Function&lt;Input, Result&gt; FUNCTION =
 *             Variety.joined(Input::isC, Input::isB, Input::isA)
 *                    .replying(A, E, C, B, B, A, D, C);
 *
 *     public static Result map(final Input input) {
 *         return FUNCTION.apply(input);
 *     }
 * }
 * </pre>
 * <p>
 * Another option requires a bit more code, but may offer a bit more readability...
 * <pre>
 * public enum Result {
 *
 *     A, B, C, D, E;
 *
 *     private static final Function&lt;Input, Result&gt; FUNCTION =
 *             Variety.joined(Input::isC, Input::isB, Input::isA)
 *                    .on(0b000, 0b101).reply(A)
 *                    .on(0b001).reply(E)
 *                    .on(0b010, 0b111).reply(C)
 *                    .on(0b011, 0b100).reply(B)
 *                    .on(0b110).reply(D)
 *                    .toFunction();
 *
 *     public static Result map(final Input input) {
 *         return FUNCTION.apply(input);
 *     }
 * }
 * </pre>
 *
 * @see <a href="https://de.wikipedia.org/wiki/Carpo_(Mond)" target="_blank">Carpo (Mond)</a>
 */
package de.team33.patterns.decision.carpo;
