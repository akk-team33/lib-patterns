package de.team33.patterns.exceptional.dione;

/**
 * A kind of function that allows to throw a checked exception.
 *
 * @see java.util.function.BiFunction
 */
@FunctionalInterface
public interface XBiFunction<T, U, R, X extends Exception> {

    /**
     * Performs this operation on the given arguments and returns a result.
     *
     * @throws X if so.
     * @see java.util.function.BiFunction#apply(Object, Object)
     */
    R apply(T t, U u) throws X;
}
