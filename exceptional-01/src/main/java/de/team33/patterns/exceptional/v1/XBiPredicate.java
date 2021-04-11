package de.team33.patterns.exceptional.v1;

/**
 * A kind of predicate that allows to throw a checked exception.
 *
 * @see java.util.function.BiPredicate
 */
@FunctionalInterface
public interface XBiPredicate<T, U, X extends Exception> {

    /**
     * Performs this operation on the given arguments and returns a {@code boolean} result.
     *
     * @throws X if so.
     * @see java.util.function.Predicate#test(Object)
     */
    boolean test(T t, U u) throws X;
}
