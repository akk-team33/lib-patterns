package de.team33.patterns.exceptional.dione;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Ignoring<X extends Exception> {

    private final Set<Class<?>> exceptionClasses;

    private Ignoring(final Class<X> exceptionClass, final Class<? extends RuntimeException>[] more) {
        this.exceptionClasses = Stream.concat(Stream.of(exceptionClass), Stream.of(more))
                                      .collect(Collectors.toSet());
    }

    @SafeVarargs
    public static <X extends Exception> Ignoring<X> any(final Class<X> exceptionClass,
                                                        final Class<? extends RuntimeException>... more) {
        return new Ignoring<>(exceptionClass, more);
    }

    public final void run(final XRunnable<X> runnable) {
        apply(Mutual.normalized(runnable));
    }

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
