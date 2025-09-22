package de.team33.patterns.lazy.narvi;

import de.team33.patterns.exceptional.dione.XSupplier;

import java.util.function.BooleanSupplier;

class ReMutual<T, X extends Exception, R extends ReMutual<T, X, R>> extends Mutual<T, X, R> {

    private final XSupplier<? extends T, ? extends X> initial;

    ReMutual(final XSupplier<? extends T, ? extends X> initial) {
        super(initial);
        this.initial = initial;
    }

    T getAfterResetIf(final BooleanSupplier condition) throws X {
        if (condition.getAsBoolean()) {
            reset();
        }
        return get();
    }

    /**
     * Resets <em>this</em> to the initial state and returns <em>this</em>.
     */
    public final R reset() {
        return reset(initial);
    }
}
