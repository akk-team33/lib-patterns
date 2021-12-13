package de.team33.patterns.testing.e1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Report {

    private final ArrayList<Throwable> problems;

    private Report(final Builder builder) {
        this.problems = new ArrayList<>(builder.problems);
    }

    public static Builder builder() {
        return new Builder();
    }

    public final <X extends Throwable> Report reThrow(final Class<X> xClass) throws X {
        final Optional<X> caught = streamCaught(xClass).reduce((head, tail) -> {
            head.addSuppressed(tail);
            return head;
        });
        if (caught.isPresent()) {
            throw caught.get();
        }
        return this;
    }

    @SafeVarargs
    public final <X extends Throwable> Stream<X> streamCaught(final Class<X> xClass,
                                                              final Class<? extends X> ... excluded) {
        return problems.stream()
                       .filter(problem -> xClass.isAssignableFrom(problem.getClass()))
                       .filter(problem -> included(problem, excluded))
                       .map(xClass::cast);
    }

    private boolean included(final Throwable problem, final Class<?>[] excluded) {
        Stream.of(excluded).anyMatch(ex -> ex.isAssignableFrom(problem.getClass()));
        throw new UnsupportedOperationException("not yet implemented");
    }

    public final <X extends Throwable> List<X> getCaught(final Class<X> xClass) {
        return streamCaught(xClass).collect(Collectors.toList());
    }

    public final List<Throwable> getCaught() {
        return getCaught(Throwable.class);
    }

    @SuppressWarnings("UnusedReturnValue")
    public static class Builder {

        private final List<Throwable> problems = Collections.synchronizedList(new LinkedList<>());

        public final Report build() {
            return new Report(this);
        }

        public final Builder add(final Throwable caught) {
            problems.add(caught);
            return this;
        }
    }
}
