package de.team33.patterns.pooling.v1;

import de.team33.patterns.exceptional.v1.XConsumer;
import de.team33.patterns.exceptional.v1.XFunction;
import de.team33.patterns.exceptional.v1.XSupplier;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A tool that makes instances of a certain type available for the course of operations that require such an instance.
 * The instances provided are referred to as <em>subject</em> in the following.
 * <p>
 * The tool maintains a number of such <em>subjects</em> and only creates as many as are actually required at most at
 * the same time in its application context. The <em>subjects</em> are retained and reused for subsequent operations.
 * <p>
 * In this respect, this tool is suitable for providing <em>subject</em> types whose instantiation is relatively
 * "expensive", which are rather unsuitable for concurrent access, but are designed for multiple or permanent use.
 * Database or other client-server connections, but also {@link java.util.Random} instances, may be an example.
 * <p>
 * Note: this implementation cannot detect when an internal operation is taking place in the course of an operation to
 * which the same <em>subject</em> could be made available.
 *
 * @param <S> The type of provided instances <em>(subjects)</em>
 * @param <X> A type of exception that may be caused by the creation of new <em>subject</em> instances.
 * @see Provider
 */
public class XProvider<S, X extends Exception> {

    private static final Void IRRELEVANT = null;

    private final Queue<S> stock = new ConcurrentLinkedQueue<>();
    private final XSupplier<S, X> newItem;

    public XProvider(final XSupplier<S, X> newItem) {
        this.newItem = newItem;
    }

    /**
     * Runs a given {@link Consumer} with a parameter provided for it. The parameter is kept for future use.
     * <p>
     * While the {@link Consumer} is being executed, the parameter is exclusively available to it, but must not be
     * "hijacked" from the context of the execution or the executing thread!
     */
    public final <EX extends Exception> void runEx(final XConsumer<? super S, EX> consumer) throws X, EX {
        getEx(parameter -> {
            consumer.accept(parameter);
            return IRRELEVANT;
        });
    }

    /**
     * Runs a given {@link Consumer} with a parameter provided for it. The parameter is kept for future use.
     * <p>
     * While the {@link Consumer} is being executed, the parameter is exclusively available to it, but must not be
     * "hijacked" from the context of the execution or the executing thread!
     */
    public final void run(final Consumer<? super S> consumer) throws X {
        runEx(consumer::accept);
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
    public final <R, EX extends Exception> R getEx(final XFunction<? super S, R, EX> function) throws X, EX {
        final S stocked = stock.poll();
        final S item = (null == stocked) ? newItem.get() : stocked;
        try {
            return function.apply(item);
        } finally {
            stock.add(item);
        }
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
    public final <R> R get(final Function<? super S, R> function) throws X {
        return getEx(function::apply);
    }
}
