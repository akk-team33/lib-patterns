package de.team33.patterns.exceptional.v1;

import java.util.function.Function;

/**
 * A tool that supports the differentiated handling of the {@linkplain Throwable#getCause() cause of an exception}.
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
 * back to the fore in order to enforce a structured approach. A {@link Cause} supports the latter, as the following
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
 * @see #reThrowIf(Class)
 * @see #map(Function)
 */
public final class Cause {

    private final Throwable cause;

    private Cause(final Throwable subject) {
        this.cause = subject.getCause();
    }

    /**
     * Returns a new instance to handle the {@link Throwable#getCause() cause} of a given exception.
     *
     * @param subject the exception to be handled
     * @param <T>     the type of the given exception
     */
    public static <T extends Throwable> Cause of(final T subject) {
        return new Cause(subject);
    }

    private static <X extends Throwable> void throwIfPresent(final X exception) throws X {
        if (null != exception) {
            throw exception;
        }
    }

    /**
     * Re-throws the {@link Throwable#getCause() cause} of the {@linkplain #of(Throwable) associated exception}
     * if it matches the given exception type. Otherwise this {@link Cause} will be returned. Example:
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
     * @see #map(Function)
     * @see #throwMappedCause(Function)
     */
    public final <X extends Throwable> Cause reThrowIf(final Class<X> type) throws X {
        throwIfPresent(type.isInstance(cause) ? type.cast(cause) : null);
        return this;
    }

    /**
     * Applies a given {@link Function mapping} to the {@link Throwable#getCause() cause} of the
     * {@linkplain #of(Throwable) associated exception} and throws the result if it is NOT {@code null}.
     * Otherwise this {@link Cause} will be returned.
     *
     * @param mapping A {@link Function} that converts the {@link Throwable#getCause() cause} of the
     *                {@linkplain #of(Throwable) associated exception} to a specific type of exception to be thrown at
     *                that point, or returns {@code null} if handling should continue.
     * @param <X>     The exception type that is intended as a result of the given mapping and that is thrown by this
     *                method, if applicable.
     * @return This handling, which can be continued if no exception has been thrown.
     * @throws X The converted exception, if present.
     * @see #reThrowIf(Class)
     */
    public final <X extends Throwable> Cause throwMappedCause(final Function<Throwable, X> mapping) throws X {
        throwIfPresent(mapping.apply(cause));
        return this;
    }

    /**
     * Returns the {@linkplain #of(Throwable) associated exception}.
     */
    public final Throwable reGet() {
        return cause;
    }

    /**
     * Applies a given {@link Function mapping} to the {@link Throwable#getCause() cause} of the
     * {@linkplain #of(Throwable) associated exception} and returns the result.
     */
    public final <X extends Throwable> X map(final Function<Throwable, X> mapping) {
        return mapping.apply(cause);
    }
}
