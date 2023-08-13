package de.team33.patterns.lazy.narvi;

import de.team33.patterns.exceptional.e1.XSupplier;

class Mutual<T, X extends Exception> {

    private volatile XSupplier<T, X> backing;

    Mutual(final XSupplier<T, X> initial) {
        this.backing = provident(initial);
    }

    private XSupplier<T, X> definite(final T value) {
        return () -> value;
    }

    private XSupplier<T, X> provident(final XSupplier<T, X> initial) {
        return new XSupplier<T, X>() {

            @Override
            public synchronized T get() throws X {
                if (backing == this) {
                    backing = definite(initial.get());
                }
                return backing.get();
            }
        };
    }

    T get() throws X {
        return backing.get();
    }
}
