package de.team33.patterns.exceptional.dione;

/**
 * A kind of supplier that allows to throw a checked exception.
 *
 * @see java.util.function.Supplier
 */
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
