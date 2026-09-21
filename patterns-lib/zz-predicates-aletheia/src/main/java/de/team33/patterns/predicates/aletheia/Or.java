package de.team33.patterns.predicates.aletheia;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

class Or<T> implements Predicate<T> {

    private final List<Predicate<? super T>> core;

    Or() {
        this.core = List.of();
    }

    private Or(final Or<T> head, final Stream<? extends Predicate<? super T>> tail) {
        this.core = Stream.concat(head.core.stream(), tail).toList();
    }

    @Override
    public boolean test(final T candidate) {
        return core.stream()
                   .anyMatch(predicate -> predicate.test(candidate));
    }

    @Override
    public Predicate<T> and(final Predicate<? super T> other) {
        if (Predicates.reject() == other) {
            return Predicates.reject();
        } else if (Predicates.accept() == other) {
            return this;
        } else {
            return Predicate.super.and(other);
        }
    }

    @Override
    public Predicate<T> or(final Predicate<? super T> other) {
        if (Predicates.accept() == other) {
            return Predicates.accept();
        } else if (Predicates.reject() == other) {
            return this;
        } else if (other instanceof final Or<? super T> otherOr) {
            return new Or<>(this, otherOr.core.stream());
        } else {
            return new Or<>(this, Stream.of(Objects.requireNonNull(other)));
        }
    }
}
