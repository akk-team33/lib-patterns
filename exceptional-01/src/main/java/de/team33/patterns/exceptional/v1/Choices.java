package de.team33.patterns.exceptional.v1;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A tool that supports the differentiated handling of an exception, especially its {@link Throwable#getCause() cause}.
 * <p>
 * In general, there is little need for a special tool for differentiated handling of exceptions.
 * The possibilities of Java’s own language elements (try-catch) are compact, expressive and efficient.
 * <p>
 * The situation is different if the {@linkplain Throwable#getCause() cause of an exception} is to be handled in a
 * differentiated manner. This often results in extensive and sometimes difficult to read code.
 * In any case, the regular code takes a back seat.
 * <p>
 * For example, in order not to have to deal with it all the time, we would like to temporarily wrap <em>checked</em>
 * exceptions into <em>unchecked</em> exceptions. However, we would then like to bring the <em>checked</em> exceptions
 * back to the fore in order to enforce a structured approach. A {@link Choices} supports the latter, as the following
 * code example shows:
 * <pre>
 * try {
 *     doSomethingThatMayThrowAWrappedException();
 * } catch (final WrappedException caught) {
 *     // We want to unwrap the cause of the caught exception and rethrow
 *     // it as a certain type of exception that meets our expectations ...
 *     throw Handling.of(caught)
 *                   .reThrowCauseIf(IOException.class)
 *                   .reThrowCauseIf(SQLException.class)
 *                   .reThrowCauseIf(URISyntaxException.class)
 *                   // Technically, it could happen that our expectations are not met.
 *                   // To be on the safe side, this should lead to a meaningful exception ...
 *                   .mappedCause(ExpectationException::new);
 * }
 * </pre>
 *
 * @see #of(Throwable)
 * @see #reThrow(Class)
 */
public final class Choices<T extends Throwable> {

    private final T subject;

    private Choices(final T subject) {
        this.subject = subject;
    }

    /**
     * Returns a new instance to handle a given exception.
     *
     * @param subject the exception to be handled
     * @param <T>     the type of the given exception
     */
    public static <T extends Throwable> Choices<T> of(final T subject) {
        return new Choices<>(subject);
    }

    /**
     * Re-throws the {@link Throwable#getCause() cause} of the {@linkplain #of(Throwable) associated exception}
     * if it matches the given exception type. Otherwise this {@link Choices} will be returned. Example:
     * <pre>
     * try {
     *     doSomethingThatMayThrowAWrappedException();
     * } catch (final WrappedException caught) {
     *     // We want to unwrap the cause of the caught exception and rethrow
     *     // it as a certain type of exception that meets our expectations ...
     *     throw Handling.of(caught)
     *                   .reThrowCauseIf(IOException.class)
     *                   .reThrowCauseIf(SQLException.class)
     *                   .reThrowCauseIf(URISyntaxException.class)
     *                   // Technically, it could happen that our expectations are not met.
     *                   // To be on the safe side, this should lead to a meaningful exception ...
     *                   .mappedCause(ExpectationException::new);
     * }
     * </pre>
     *
     * @param type The {@link Class} that represents the type of exception that is expected.
     * @param <X>  The type of exception that is expected and, if applicable, thrown by this method.
     * @return This handling, which can be continued if no exception has been thrown.
     * @throws X the {@linkplain #of(Throwable) associated exception}, cast to the expected type, if applicable.
     * @see #of(Throwable)
     */
    public final <X extends Throwable> Choices<T> reThrow(final Class<X> type) throws X {
        return when(type::isInstance).reThrow(type::cast);
    }

    public final Conditional<T> when(final Predicate<? super T> condition) {
        if (condition.test(subject))
            return new Positive();
        else
            return new Negative();
    }

    /**
     * Returns the {@linkplain #of(Throwable) associated exception}.
     */
    public final T finish() {
        return subject;
    }

    /**
     * Applies a given {@link Function mapping} to the {@linkplain #of(Throwable) associated exception} and returns the
     * result.
     */
    public final <X extends Throwable> X finish(final Function<? super T, X> mapping) {
        return mapping.apply(subject);
    }

    public interface Conditional<T extends Throwable> {

        <X extends Throwable> Choices<T> reThrow(final Function<T, X> mapping) throws X;
    }

    private class Negative implements Conditional<T> {
        @Override
        public <X extends Throwable> Choices<T> reThrow(final Function<T, X> mapping) throws X {
            return Choices.this;
        }
    }

    private class Positive implements Conditional<T> {
        @Override
        public <X extends Throwable> Choices<T> reThrow(final Function<T, X> mapping) throws X {
            throw mapping.apply(subject);
        }
    }
}
