package de.team33.patterns.pooling.e1;

import de.team33.patterns.exceptional.e1.XConsumer;
import de.team33.patterns.exceptional.e1.XFunction;
import de.team33.patterns.exceptional.e1.XSupplier;

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
 * Database or other client-server connections may be an example.
 * <p>
 * Note: this implementation cannot detect when an internal operation is taking place in the course of an operation to
 * which the same <em>subject</em> could be made available.
 *
 * @param <S> The type of provided instances <em>(subjects)</em>
 * @param <E> A type of exception that may be caused by the creation of new <em>subject</em> instances.
 * @see Provider
 */
public class XProvider<S, E extends Exception> {

    private static final Void IRRELEVANT = null;

    private final Queue<S> stock = new ConcurrentLinkedQueue<>();
    private final XSupplier<S, E> newItem;

    /**
     * Initializes a new instance giving an {@link XSupplier} that defines the intended initialization of a
     * new <em>subject</em>.
     */
    public XProvider(final XSupplier<S, E> newItem) {
        this.newItem = newItem;
    }

    /**
     * Runs a given {@link XConsumer} with a parameter provided for it. The parameter is kept for future use.
     * <p>
     * While the {@link XConsumer} is being executed, the parameter is exclusively available to it, but must not be
     * "hijacked" from the context of the execution or the executing thread!
     *
     * @param <X> A type of exception that may be caused by the given {@link XConsumer}.
     * @throws E if the initialization of a new <em>subject</em> causes one.
     * @throws X if the execution of the given {@link XConsumer} causes one.
     */
    public final <X extends Exception> void runEx(final XConsumer<? super S, X> consumer) throws E, X {
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
     *
     * @throws E if the initialization of a new <em>subject</em> causes one.
     */
    public final void run(final Consumer<? super S> consumer) throws E {
        runEx(consumer::accept);
    }

    /**
     * Calls a given {@link XFunction} with a parameter provided for it and returns its result.
     * The parameter is kept for future use.
     * <p>
     * While the {@link XFunction} is being called, the parameter is exclusively available to it, but must not be
     * "hijacked" from the context of the call or the executing thread!
     *
     * @param <R> The result type of the given {@link Function}
     * @param <X> A type of exception that may be caused by the given {@link XConsumer}.
     * @throws E if the initialization of a new <em>subject</em> causes one.
     * @throws X if the execution of the given {@link XConsumer} causes one.
     */
    public final <R, X extends Exception> R getEx(final XFunction<? super S, R, X> function) throws E, X {
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
     * @throws E if the initialization of a new <em>subject</em> causes one.
     */
    public final <R> R get(final Function<? super S, R> function) throws E {
        return getEx(function::apply);
    }
}
