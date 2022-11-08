package de.team33.patterns.lazy.narvi;

import de.team33.patterns.exceptional.e1.XSupplier;

@SuppressWarnings("BoundedWildcard")
class Mutual<T, X extends Exception> {

    @SuppressWarnings("PackageVisibleField")
    volatile XSupplier<T, X> backing;

    Mutual(final XSupplier<T, X> initial) {
        this.backing = initiation(initial);
    }

    private XSupplier<T, X> initiation(final XSupplier<T, X> initial) {
        return new XSupplier<T, X>() {
            private final Object monitor = new Object();

            @SuppressWarnings("NonPrivateFieldAccessedInSynchronizedContext")
            @Override
            public final T get() throws X {
                synchronized (monitor) {
                    if (backing == this) {
                        final T result = initial.get();
                        backing = () -> result;
                    }
                }
                return backing.get();
            }
        };
    }
}
