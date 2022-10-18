package de.team33.patterns.pooling.e1;

import de.team33.patterns.exceptional.e1.XConsumer;
import de.team33.patterns.exceptional.e1.XFunction;
import de.team33.patterns.exceptional.e1.XSupplier;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

class Mutual<S, E extends Exception> {

    private static final Void VOID = null;

    private final Queue<S> stock = new ConcurrentLinkedQueue<>();
    private final XSupplier<S, E> newItem;

    Mutual(final XSupplier<S, E> newItem) {
        this.newItem = newItem;
    }

    final <X extends Exception> void accept(final XConsumer<? super S, X> xConsumer) throws E, X {
        apply(subject -> {
            xConsumer.accept(subject);
            return VOID;
        });
    }

    final <R, X extends Exception> R apply(final XFunction<? super S, R, X> function) throws E, X {
        final S stocked = stock.poll();
        final S item = (null == stocked) ? newItem.get() : stocked;
        try {
            return function.apply(item);
        } finally {
            stock.add(item);
        }
    }
}
