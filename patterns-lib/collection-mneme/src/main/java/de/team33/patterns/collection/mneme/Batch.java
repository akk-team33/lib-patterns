package de.team33.patterns.collection.mneme;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;

final class Batch<E> implements Source<E> {

    private final List<E> core = new ArrayList<>();

    static <E, R> Collector<E, Batch<E>, R> collector(final Function<? super Batch<E>, ? extends R> finisher) {
        return Collector.of(Batch::new, Batch::append, Batch::append, finisher::apply);
    }

    private void append(final E element) {
        core.add(element);
    }

    private Batch<E> append(final Batch<? extends E> other) {
        core.addAll(other.core);
        return this;
    }

    @Override
    public final Stream<E> stream() {
        return core.stream();
    }
}
