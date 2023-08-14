package de.team33.patterns.exceptional.dione;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A tool that supports a revision and handling of an exception.
 * <p>
 * In general, there is little need for a special tool for differentiated handling of exceptions.
 * The possibilities of Java’s own language elements (try-catch) are compact, expressive and efficient.
 * <p>
 * The situation is different if the {@linkplain Throwable#getCause() cause of an exception} is to be handled
 * in a differentiated manner. This often results in extensive and sometimes difficult to read code.
 * In any case, the regular code takes a back seat.
 * <p>
 * For example, in order not to have to deal with it all the time, we would like to temporarily wrap
 * <em>checked</em> exceptions into <em>unchecked</em> exceptions. However, we would then like to bring the
 * <em>checked</em> exceptions back to the fore in order to enforce a structured approach. A {@link Revision}
 * supports the latter, as the following code example shows:
 * <pre>
 *         try {
 *             return somethingThatMayCauseAWrappedException();
 *         } catch (final WrappedException caught) {
 *             throw Revision.of(caught.getCause())
 *                           .reThrow(IOException.class)
 *                           .reThrow(SQLException.class)
 *                           .reThrow(URISyntaxException.class)
 *                           .close(ExpectationException::new);
 *         }
 * </pre>
 *
 * @see #of(Throwable)
 * @see #reThrow(Class)
 * @see #close(Function)
 */
public final class Revision<T extends Throwable> {

    private final T subject;

    private Revision(final T subject) {
        this.subject = subject;
    }

    /**
     * Returns a new instance to review and handle a given exception.
     *
     * @param subject the exception to be handled
     * @param <T>     the type of the given exception
     */
    public static <T extends Throwable> Revision<T> of(final T subject) {
        return new Revision<>(subject);
    }

    /**
     * Applies a given {@link Function mapping} to the {@linkplain #of(Throwable) associated exception} if the given
     * {@link Predicate condition} applies and throws the result, otherwise this {@link Revision} will be returned.
     *
     * @param condition A {@link Predicate} that is used to check the {@linkplain #of(Throwable) associated exception}
     *                  for applicability.
     * @param mapping   A {@link Function} that converts the {@linkplain #of(Throwable) associated exception} to a
     *                  specific type of exception to be thrown at that point.
     * @param <X>       The exception type that is intended as a result of the given mapping and that is thrown by this
     *                  method, if applicable.
     * @return This {@link Revision}, which can be continued if no exception has been thrown.
     * @throws X The converted exception, if present.
     */
    public final <X extends Throwable> Revision<T> throwIf(final Predicate<? super T> condition,
                                                           final Function<? super T, X> mapping) throws X {
        return throwIf(condition, mapping, this);
    }

    /**
     * Applies a given {@link Function mapping} to the {@linkplain #of(Throwable) associated exception} if the given
     * {@link Predicate condition} applies and throws the result, otherwise a given result will be returned.
     *
     * @param condition A {@link Predicate} that is used to check the {@linkplain #of(Throwable) associated exception}
     *                  for applicability.
     * @param mapping   A {@link Function} that converts the {@linkplain #of(Throwable) associated exception} to a
     *                  specific type of exception to be thrown at that point.
     * @param result    A predefined regular result in case the condition is not true.
     * @param <R>       The result type in case of a regular result.
     * @param <X>       The exception type that is intended as a result of the given mapping and that is thrown by this
     *                  method, if applicable.
     * @return The predefined result.
     * @throws X The converted exception, if present.
     */
    public final <R, X extends Throwable> R throwIf(final Predicate<? super T> condition,
                                                    final Function<? super T, X> mapping,
                                                    final R result) throws X {
        if (condition.test(subject)) {
            throw mapping.apply(subject);
        } else {
            return result;
        }
    }

    /**
     * Rethrows the {@linkplain #of(Throwable) associated exception} if it matches the given exception type.
     * Otherwise, this {@link Revision} will be returned. Example:
     * <pre>
     *         try {
     *             return somethingThatMayCauseAWrappedException();
     *         } catch (final WrappedException caught) {
     *             throw Revision.of(caught.getCause())
     *                           .reThrow(IOException.class)
     *                           .reThrow(SQLException.class)
     *                           .reThrow(URISyntaxException.class)
     *                           .close(ExpectationException::new);
     *         }
     * </pre>
     *
     * @param xClass The {@link Class} that represents the type of exception that is expected.
     * @param <X>    The type of exception that is expected and, if applicable, thrown by this method.
     * @return This {@link Revision}, which can be continued if no exception has been thrown.
     * @throws X the {@linkplain #of(Throwable) associated exception}, cast to the expected type, if applicable.
     * @see #of(Throwable)
     * @see #close(Function)
     */
    public final <X extends Throwable> Revision<T> reThrow(final Class<X> xClass) throws X {
        return reThrow(xClass, this);
    }

    /**
     * Rethrows the {@linkplain #of(Throwable) associated exception} if it matches the given exception type,
     * otherwise a given result will be returned.
     *
     * @param xClass The {@link Class} that represents the type of exception that is expected.
     * @param result A predefined regular result in case the exception is not of that type.
     * @param <R>    The result type in case of a regular result.
     * @param <X>    The type of exception that is expected and, if applicable, thrown by this method.
     * @return The predefined result.
     * @throws X the {@linkplain #of(Throwable) associated exception}, cast to the expected type, if applicable.
     */
    public final <R, X extends Throwable> R reThrow(final Class<X> xClass, final R result) throws X {
        return throwIf(xClass::isInstance, xClass::cast, result);
    }

    /**
     * Completes this revision, applies a given {@link Function mapping} to the
     * {@linkplain #of(Throwable) associated exception}, and returns its result.
     */
    public final <R> R close(final Function<? super T, R> mapping) {
        return mapping.apply(subject);
    }

    /**
     * Completes this revision and returns the {@linkplain #of(Throwable) associated exception} unchanged.
     */
    public final T close() {
        return subject;
    }

    /**
     * @deprecated use {@link #close(Function)} instead!
     */
    @Deprecated
    public final <R> R finish(final Function<? super T, R> mapping) {
        return close(mapping);
    }

    /**
     * @deprecated use {@link #close()} instead!
     */
    @Deprecated
    public final T finish() {
        return close();
    }
}
