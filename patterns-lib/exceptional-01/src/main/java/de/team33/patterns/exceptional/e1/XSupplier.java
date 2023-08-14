package de.team33.patterns.exceptional.e1;

/**
 * A kind of supplier that allows to throw a checked exception.
 *
 * @see java.util.function.Supplier
 *
 * @deprecated Further development is discontinued and this package/module may be removed in a future release.
 * Successor is the module <em>exceptional-dione</em>.
 */
@Deprecated
@FunctionalInterface
public interface XSupplier<T, X extends Exception> {

    /**
     * Performs this operation and returns a result.
     *
     * @throws X if so.
     * @see java.util.function.Supplier#get()
     */
    T get() throws X;
}
