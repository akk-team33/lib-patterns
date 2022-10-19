package de.team33.patterns.testing.titan;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.synchronizedList;
import static java.util.Collections.unmodifiableList;

/**
 * A report of multiple executions of a method.
 *
 * @param <R> The type of result of the method to be run.
 */
public final class Report<R> {

    private final List<R> results;
    private final List<Throwable> throwables;

    @SuppressWarnings("WeakerAccess")
    Report(final Builder<R> builder) {
        this.results = unmodifiableList(new ArrayList<>(builder.results));
        this.throwables = unmodifiableList(new ArrayList<>(builder.throwables));
    }

    /**
     * Returns a {@link List} of all results that have accumulated during reporting.
     */
    public final List<R> getResults() {
        return results;
    }

    /**
     * Returns a {@link List} of all {@linkplain Throwable exceptions} that occurred during reporting.
     */
    public final List<Throwable> getThrowables() {
        return throwables;
    }

    /**
     * Returns a {@link List} of all {@linkplain Throwable exceptions} of a certain type that occurred during
     * reporting. Certain derived types can be excluded from the result.
     *
     * @param <X> The type of {@linkplain Throwable exceptions} to be listed.
     */
    @SuppressWarnings("OverloadedVarargsMethod")
    @SafeVarargs
    public final <X extends Throwable> List<X> getThrowables(final Class<X> xClass,
                                                             final Class<? extends X> ... ignorable) {
        return streamThrowables(xClass, ignorable).collect(Collectors.toList());
    }

    /**
     * Returns a {@link Stream} of all {@linkplain Throwable exceptions} of a certain type that occurred during
     * reporting. Certain derived types can be excluded from the resulting {@link Stream}.
     *
     * @param <X> The type of {@linkplain Throwable exceptions} to be streamed.
     */
    @SuppressWarnings("WeakerAccess")
    @SafeVarargs
    public final <X extends Throwable> Stream<X> streamThrowables(final Class<X> xClass,
                                                                  final Class<? extends X> ... ignorable) {
        return throwables.stream()
                         .filter(xClass::isInstance)
                         .map(xClass::cast)
                         .filter(throwable -> Stream.of(ignorable)
                                                    .noneMatch(iClass -> iClass.isInstance(throwable)));
    }

    /**
     * Re-throws the first {@linkplain Throwable exception} of a certain type that occurred during
     * reporting after all further {@linkplain Throwable exceptions} of that type have been
     * {@linkplain Throwable#addSuppressed(Throwable) added as suppressed}.
     * Certain derived types can be excluded from processing.
     *
     * @param <X> The type of {@linkplain Throwable exceptions} to be processed.
     */
    @SafeVarargs
    public final <X extends Throwable> Report<R> reThrow(final Class<X> xClass,
                                                         final Class<? extends X> ... ignorable) throws X {
        final Optional<X> caught = streamThrowables(xClass, ignorable).reduce((head, tail) -> {
            head.addSuppressed(tail);
            return head;
        });
        if (caught.isPresent()) {
            throw caught.get();
        }
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    static class Builder<R> {

        final List<Throwable> throwables = synchronizedList(new LinkedList<>());
        final List<R> results = synchronizedList(new LinkedList<>());

        final Report<R> build() {
            return new Report<>(this);
        }

        final Builder<R> add(final Throwable caught) {
            throwables.add(caught);
            return this;
        }

        final Builder<R> add(final R result) {
            results.add(result);
            return this;
        }
    }
}
