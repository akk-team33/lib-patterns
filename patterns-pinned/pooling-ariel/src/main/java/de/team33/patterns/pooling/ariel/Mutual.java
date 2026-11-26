package de.team33.patterns.pooling.ariel;

import de.team33.patterns.exceptional.dione.XConsumer;
import de.team33.patterns.exceptional.dione.XFunction;
import de.team33.patterns.exceptional.dione.XSupplier;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

class Mutual<S, E extends Exception> {

    private static final Void VOID = null;

    private final Queue<XSupplier<S, E>> stock = new ConcurrentLinkedQueue<>();
    private final XSupplier<? extends XSupplier<S, E>, ? extends E> newItem;

    Mutual(final XSupplier<? extends XSupplier<S, E>, ? extends E> newItem) {
        this.newItem = newItem;
    }

    final <X extends Exception> void accept(final XConsumer<? super S, X> xConsumer) throws E, X {
        apply(subject -> {
            xConsumer.accept(subject);
            return VOID;
        });
    }

    final <R, X extends Exception> R apply(final XFunction<? super S, R, X> function) throws E, X {
        final XSupplier<S, E> stocked = stock.poll();
        final XSupplier<S, E> item = (null == stocked) ? newItem.get() : stocked;
        try {
            return function.apply(item.get());
        } finally {
            stock.add(item);
        }
    }

    /**
     * Returns the number of currently unused subjects in stock.
     */
    public final int size() {
        return stock.size();
    }
}
