package de.team33.patterns.testing.e1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A report of multiple execution of a method.
 */
public class Report {

    private final List<Throwable> problems;

    private Report(final Builder builder) {
        this.problems = Collections.unmodifiableList(new ArrayList<>(builder.problems));
    }

    /**
     * Results in a new {@link Builder} instance.
     */
    public static Builder builder() {
        return new Builder();
    }

    @SafeVarargs
    public final <X extends Throwable> Report reThrow(final Class<X> xClass,
                                                      final Class<? extends X> ... ignorable) throws X {
        final Optional<X> caught = streamCaught(xClass, ignorable).reduce((head, tail) -> {
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
                                                              final Class<? extends X> ... ignorable) {
        return problems.stream()
                       .filter(xClass::isInstance)
                       .map(xClass::cast)
                       .filter(problem -> Stream.of(ignorable)
                                                .noneMatch(clss -> clss.isInstance(problem)));
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
