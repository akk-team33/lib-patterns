package de.team33.patterns.predicates.aletheia;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

class And<T> implements Predicate<T> {

    private final List<Predicate<? super T>> core;

    And() {
        this.core = List.of();
    }

    private And(final And<T> head, final Stream<? extends Predicate<? super T>> tail) {
        this.core = Stream.concat(head.core.stream(), tail).toList();
    }

    @Override
    public boolean test(final T candidate) {
        return core.stream()
                   .allMatch(predicate -> predicate.test(candidate));
    }

    @Override
    public Predicate<T> and(final Predicate<? super T> other) {
        if (Predicates.reject() == other) {
            return Predicates.reject();
        } else if (Predicates.accept() == other) {
            return this;
        } else if (other instanceof final And<? super T> otherAnd) {
            return new And<>(this, otherAnd.core.stream());
        } else {
            return new And<>(this, Stream.of(Objects.requireNonNull(other)));
        }
    }

    @Override
    public Predicate<T> or(final Predicate<? super T> other) {
        if (Predicates.accept() == other) {
            return Predicates.accept();
        } else if (Predicates.reject() == other) {
            return this;
        } else {
            return Predicate.super.or(other);
        }
    }
}
