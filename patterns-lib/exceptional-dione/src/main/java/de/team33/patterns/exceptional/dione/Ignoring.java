package de.team33.patterns.exceptional.dione;

import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A tool class that can ignore a specific (checked) exception type when executing a corresponding code fragment.
 * <p>
 * Use {@link #any(Class, Class[])} to get an instance.
 */
public class Ignoring<X extends Exception> {

    private final Set<Class<?>> exceptionClasses;

    private Ignoring(final Class<X> exceptionClass, final Class<? extends RuntimeException>[] more) {
        this.exceptionClasses = Stream.concat(Stream.of(exceptionClass), Stream.of(more))
                                      .collect(Collectors.toSet());
    }

    /**
     * Returns a new instance by a given (checked) <em>exceptionClass</em> to be ignored.
     * Optionally, <em>additional</em> (unchecked) exception types can be specified which should also be ignored.
     */
    @SafeVarargs
    public static <X extends Exception> Ignoring<X> any(final Class<X> exceptionClass,
                                                        final Class<? extends RuntimeException>... additional) {
        return new Ignoring<>(exceptionClass, additional);
    }

    /**
     * Runs a given {@link XRunnable} ignoring an exception that may occur.
     *
     * @see #any(Class, Class[])
     */
    public final void run(final XRunnable<X> runnable) {
        apply(Mutual.normalized(runnable));
    }

    /**
     * Returns the result of running a given {@link XSupplier} ignoring an exception that may occur.
     *
     * @see #any(Class, Class[])
     *
     * @param <R> The result type.
     */
    public final <R> Optional<R> get(final XSupplier<R, X> supplier) {
        return Optional.ofNullable(apply(Mutual.normalized(supplier)));
    }

    private <R> R apply(final XBiFunction<Void, Void, R, X> function) {
        try {
            return function.apply(null, null);
        } catch (final Exception e) {
            if (exceptionClasses.stream().anyMatch(ec -> ec.isInstance(e))) {
                return null;
            } else if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            } else {
                // should not happen at all! ...
                throw new ExpectationException(e);
            }
        }
    }
}