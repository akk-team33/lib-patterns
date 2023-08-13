package de.team33.patterns.exceptional.e1;

/**
 * A kind of predicate that allows to throw a checked exception.
 *
 * @see java.util.function.Predicate
 *
 * @deprecated Further development is discontinued and this package/module may be removed in a future release.
 * Successor is the module <em>exceptional-dione</em>.
 */
@Deprecated
@FunctionalInterface
public interface XPredicate<T, X extends Exception> {

    /**
     * Performs this operation on the given argument and returns a {@code boolean} result.
     *
     * @throws X if so.
     * @see java.util.function.Predicate#test(Object)
     */
    boolean test(T t) throws X;
}
