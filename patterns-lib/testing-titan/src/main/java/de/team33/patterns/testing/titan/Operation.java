package de.team33.patterns.testing.titan;

/**
 * An operation that is intended for multiple, parallel execution.
 *
 * @param <R> The result type of the operation.
 */
@FunctionalInterface
@SuppressWarnings("ProhibitedExceptionDeclared")
public interface Operation<R> {

    /**
     * Performs this operation.
     *
     * @param context The {@link Context} of this execution.
     * @return The result as specified by this operation.
     * @throws Exception If caused during the operation.
     */
    R operate(Context context) throws Exception;
}
