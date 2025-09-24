package de.team33.patterns.lazy.narvi;

import de.team33.patterns.exceptional.dione.XSupplier;

class ReMutual<T, X extends Exception, R extends ReMutual<T, X, R>> extends Mutual<T, X> {

    private final XSupplier<? extends T, ? extends X> initial;

    ReMutual(final XSupplier<? extends T, ? extends X> initial) {
        super(initial);
        this.initial = initial;
    }

    /**
     * Resets <em>this</em> to its initial state and returns <em>this</em>.
     */
    @SuppressWarnings("unchecked")
    public final R reset() {
        reset(initial);
        return (R) this;
    }
}
