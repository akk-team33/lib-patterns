package de.team33.patterns.collection.mneme;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;

final class Stage<E> implements Source<E> {

    private final List<E> core = new ArrayList<>();

    static <E, R> Collector<E, Stage<E>, R> collector(final Function<? super Stage<E>, ? extends R> finisher) {
        return Collector.of(Stage::new, Stage::append, Stage::append, finisher::apply);
    }

    private void append(final E element) {
        core.add(element);
    }

    private Stage<E> append(final Stage<? extends E> other) {
        core.addAll(other.core);
        return this;
    }

    @Override
    public final Stream<E> stream() {
        return core.stream();
    }
}
