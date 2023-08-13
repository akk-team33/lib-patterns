package de.team33.patterns.exceptional.e1;

/**
 * A kind of runnable that allows to throw a checked exception.
 *
 * @see Runnable
 *
 * @deprecated Further development is discontinued and this package/module may be removed in a future release.
 * Successor is the module <em>exceptional-dione</em>.
 */
@Deprecated
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
