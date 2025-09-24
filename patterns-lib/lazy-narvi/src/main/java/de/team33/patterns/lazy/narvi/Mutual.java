package de.team33.patterns.lazy.narvi;

import de.team33.patterns.exceptional.dione.XSupplier;

class Mutual<T, X extends Exception> {

    private volatile XSupplier<T, X> backing;

    Mutual(final XSupplier<? extends T, ? extends X> initial) {
        this.backing = new Provident(initial);
    }

    @SuppressWarnings("MethodOnlyUsedFromInnerClass")
    private static <T, X extends Exception> XSupplier<T, X> definite(final T value) {
        return () -> value;
    }

    @SuppressWarnings("DesignForExtension")
    T get() throws X {
        return backing.get();
    }

    final boolean isProvident(final XSupplier<?,?> initial) {
        return (backing instanceof final Provident provident) && (initial == provident.initial);
    }

    final void reset(final XSupplier<? extends T, ? extends X> initial) {
        this.backing = new Provident(initial);
    }

    private final class Provident implements XSupplier<T, X> {

        private final XSupplier<? extends T, ? extends X> initial;

        private Provident(final XSupplier<? extends T, ? extends X> initial) {
            this.initial = initial;
        }

        @SuppressWarnings("SynchronizedMethod")
        @Override
        public synchronized T get() throws X {
            if (backing == this) {
                backing = definite(initial.get());
            }
            return backing.get();
        }
    }
}
