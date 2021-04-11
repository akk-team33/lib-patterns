package de.team33.patterns.exceptional.v1;

/**
 * A kind of function that allows to throw a checked exception.
 *
 * @see java.util.function.Function
 */
@FunctionalInterface
public interface XFunction<T, R, X extends Exception> {

    /**
     * Performs this operation on the given argument and returns a result.
     *
     * @throws X if so.
     * @see java.util.function.Function#apply(Object)
     */
    R apply(T t) throws X;
}
