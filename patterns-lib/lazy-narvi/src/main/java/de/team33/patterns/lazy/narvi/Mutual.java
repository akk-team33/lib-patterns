package de.team33.patterns.lazy.narvi;

import de.team33.patterns.exceptional.dione.XSupplier;

class Mutual<T, X extends Exception> {

    private volatile XSupplier<T, X> backing;

    Mutual(final XSupplier<? extends T, ? extends X> initial) {
        this.backing = provident(initial);
    }

    private XSupplier<T, X> provident(final XSupplier<? extends T, ? extends X> initial) {
        return new XSupplier<>() {

            @SuppressWarnings("SynchronizedMethod")
            @Override
            public synchronized T get() throws X {
                if (backing == this) {
                    backing = definite(initial.get());
                }
                return backing.get();
            }

            private XSupplier<T, X> definite(final T value) {
                return () -> value;
            }
        };
    }

    @SuppressWarnings("DesignForExtension")
    T get() throws X {
        return backing.get();
    }
}
