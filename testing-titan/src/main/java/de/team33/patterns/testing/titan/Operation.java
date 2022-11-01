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
     * @param input The {@link Input} of this execution.
     * @return The result as specified by this operation.
     * @throws Exception If caused during the operation.
     */
    R operate(Input input) throws Exception;
}
