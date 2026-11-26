package de.team33.patterns.exceptional.dione;

/**
 * A kind of runnable that allows to throw a checked exception.
 *
 * @see Runnable
 */
@FunctionalInterface
public interface XRunnable<X extends Exception> {

    /**
     * Performs this operation.
     *
     * @throws X if so.
     * @see Runnable#run()
     */
    void run() throws X;
}
