package de.team33.patterns.exceptional.e1;

/**
 * A kind of consumer that allows to throw a checked exception.
 *
 * @see java.util.function.Consumer
 *
 * @deprecated Further development is discontinued and this package/module may be removed in a future release.
 * Successor is the module <em>exceptional-dione</em>.
 */
@Deprecated
@FunctionalInterface
public interface XConsumer<T, X extends Exception> {

    /**
     * Performs this operation on the given argument.
     *
     * @throws X if so.
     * @see java.util.function.Consumer#accept(Object)
     */
    void accept(T t) throws X;
}
