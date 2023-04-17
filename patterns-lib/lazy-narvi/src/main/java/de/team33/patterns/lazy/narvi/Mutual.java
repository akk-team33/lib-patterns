package de.team33.patterns.lazy.narvi;

import de.team33.patterns.exceptional.e1.XSupplier;

class Mutual<T, X extends Exception> {

    private volatile XSupplier<T, X> backing;

    Mutual(final XSupplier<? extends T, ? extends X> initial) {
        this.backing = new Initial(initial);
    }

    T get() throws X {
        return backing.get();
    }

    private class Initial implements XSupplier<T, X> {

        private final XSupplier<? extends T, ? extends X> initial;

        private Initial(final XSupplier<? extends T, ? extends X> initial) {
            this.initial = initial;
        }

        @Override
        public final synchronized T get() throws X {
            if (backing == this) {
                final T result = initial.get();
                backing = () -> result;
            }
            return backing.get();
        }
    }
}
