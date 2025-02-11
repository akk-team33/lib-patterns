/**
 * Provides classes and utilities for implementing or handling complex decisions.
 * <p>
 * When decisions have to be made between several (more than two) possible results based on several independent
 * criteria, confusing code often results. Example:
 * <p>
 * Given an <em>Input</em> parameter that offers three independent criteria ...
 * <pre>
 * public interface Input {
 *
 *     boolean isConditionOne();
 *
 *     boolean isConditionTwo();
 *
 *     boolean isConditionThree();
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
 *         if (input.isConditionOne()) {
 *             if (input.isConditionTwo()) {
 *                 if (input.isConditionThree()) {
 *                     return C;
 *                 } else {
 *                     return D;
 *                 }
 *             } else {
 *                 if (input.isConditionThree()) {
 *                     return A;
 *                 } else {
 *                     return B;
 *                 }
 *             }
 *         } else {
 *             if (input.isConditionTwo()) {
 *                 if (input.isConditionThree()) {
 *                     return B;
 *                 } else {
 *                     return C;
 *                 }
 *             } else {
 *                 if (input.isConditionThree()) {
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
 * The <em>Decision</em> classes defined in this module allows a different implementation of the same decision cascade ...
 * <pre>
 * public enum Result {
 *
 *     A, B, C, D, E;
 *
 *     private static final Variety&lt;Input, Result&gt; VARIETY =
 *             Variety.joined(Input::isConditionOne, Input::isConditionTwo, Input::isConditionThree)
 *                    .replying(A, B, C, D, E, A, B, C);
 *
 *     public static Result map(final Input input) {
 *         return VARIETY.apply(input);
 *     }
 * }
 * </pre>
 * <p>
 * The interested programmer can decide for himself whether this is better.
 * In my opinion, maintainability have been improved.
 * Readability depends largely on whether the concept has been understood.
 *
 * @see <a href="https://de.wikipedia.org/wiki/Leda_(Mond)">Leda (Mond)</a>
 */
package de.team33.patterns.decision.leda;
