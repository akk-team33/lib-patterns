package de.team33.patterns.pooling.ariel;

import de.team33.patterns.exceptional.dione.XConsumer;
import de.team33.patterns.exceptional.dione.XFunction;
import de.team33.patterns.exceptional.dione.XSupplier;

import java.util.function.Consumer;
import java.util.function.Function;

class ProviderBase<I> extends Mutual<I, RuntimeException> {

    ProviderBase(final XSupplier<XSupplier<I, RuntimeException>, RuntimeException> newItem) {
        super(newItem);
    }

    /**
     * Runs a given {@link Consumer} with a parameter provided for it. The parameter is kept for future use.
     * <p>
     * While the {@link Consumer} is being executed, the parameter is exclusively available to it, but must not be
     * "hijacked" from the context of the execution or the executing thread!
     */
    public final void run(final Consumer<? super I> consumer) {
        accept(consumer::accept);
    }

    /**
     * Runs a given {@link XConsumer} with a parameter provided for it. The parameter is kept for future use.
     * <p>
     * While the {@link XConsumer} is being executed, the parameter is exclusively available to it, but must not be
     * "hijacked" from the context of the execution or the executing thread!
     *
     * @param <X> A type of exception that may be caused by the given {@link XConsumer}.
     * @throws X if the execution of the given {@link XConsumer} causes one.
     */
    public final <X extends Exception> void runEx(final XConsumer<? super I, X> xConsumer) throws X {
        accept(xConsumer);
    }

    /**
     * Calls a given {@link Function} with a parameter provided for it and returns its result.
     * The parameter is kept for future use.
     * <p>
     * While the {@link Function} is being called, the parameter is exclusively available to it, but must not be
     * "hijacked" from the context of the call or the executing thread!
     *
     * @param <R> The result type of the given {@link Function}
     */
    public final <R> R get(final Function<? super I, R> function) {
        return apply(function::apply);
    }

    /**
     * Calls a given {@link XFunction} with a parameter provided for it and returns its result.
     * The parameter is kept for future use.
     * <p>
     * While the {@link XFunction} is being called, the parameter is exclusively available to it, but must not be
     * "hijacked" from the context of the call or the executing thread!
     *
     * @param <R> The result type of the given {@link XFunction}
     * @param <X> A type of exception that may be caused by the given {@link XFunction}.
     * @throws X if the execution of the given {@link XFunction} causes one.
     */
    public final <R, X extends Exception> R getEx(final XFunction<? super I, R, X> xFunction) throws X {
        return apply(xFunction);
    }
}
