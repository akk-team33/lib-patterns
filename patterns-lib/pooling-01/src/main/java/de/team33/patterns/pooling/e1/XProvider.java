package de.team33.patterns.pooling.e1;

import de.team33.patterns.exceptional.dione.XConsumer;
import de.team33.patterns.exceptional.dione.XFunction;
import de.team33.patterns.exceptional.dione.XSupplier;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A tool that makes instances of a certain type available for the course of operations that require such an instance.
 * The instances provided are referred to as <em>item</em> in the following.
 * <p>
 * The tool maintains a number of such <em>items</em> and only creates as many as are actually required at most at
 * the same time in its application context. The <em>items</em> are retained and reused for subsequent operations.
 * <p>
 * In this respect, this tool is suitable for providing <em>item</em> types whose instantiation is relatively
 * "expensive", which are rather unsuitable for concurrent access, but are designed for multiple or permanent use.
 * Database or other client-server connections may be an example.
 * <p>
 * Note: this implementation cannot detect when an internal operation is taking place in the course of an operation to
 * which the same <em>item</em> could be made available.
 * <p>
 * This implementation supports checked exceptions to occur while creating new <em>item</em> instances.
 *
 * @param <I> The type of provided instances <em>(items)</em>
 * @param <E> A type of exception that may be caused by the creation of new <em>item</em> instances.
 * @see Provider
 * @see RProvider
 *
 * @deprecated Further development is discontinued and this package/module may be removed in a future release.
 * Successor is the module <em>pooling-ariel</em>.
 */
@Deprecated
public class XProvider<I, E extends Exception> extends Mutual<I, E> {

    /**
     * Initializes a new instance giving an {@link XSupplier} that defines the intended initialization of a
     * new <em>item</em>.
     */
    public XProvider(final XSupplier<I, E> newItem) {
        super(newItem);
    }

    /**
     * Runs a given {@link Consumer} with a parameter provided for it. The parameter is kept for future use.
     * <p>
     * While the {@link Consumer} is being executed, the parameter is exclusively available to it, but must not be
     * "hijacked" from the context of the execution or the executing thread!
     *
     * @throws E if the initialization of a new <em>item</em> causes one.
     */
    public final void run(final Consumer<? super I> consumer) throws E {
        accept(consumer::accept);
    }

    /**
     * Runs a given {@link XConsumer} with a parameter provided for it. The parameter is kept for future use.
     * <p>
     * While the {@link XConsumer} is being executed, the parameter is exclusively available to it, but must not be
     * "hijacked" from the context of the execution or the executing thread!
     *
     * @param <X> A type of exception that may be caused by the given {@link XConsumer}.
     * @throws E if the initialization of a new <em>item</em> causes one.
     * @throws X if the execution of the given {@link XConsumer} causes one.
     */
    public final <X extends Exception> void runEx(final XConsumer<? super I, X> xConsumer) throws E, X {
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
     * @throws E if the initialization of a new <em>item</em> causes one.
     */
    public final <R> R get(final Function<? super I, R> function) throws E {
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
     * @throws E if the initialization of a new <em>item</em> causes one.
     * @throws X if the execution of the given {@link XFunction} causes one.
     */
    public final <R, X extends Exception> R getEx(final XFunction<? super I, R, X> xFunction) throws E, X {
        return apply(xFunction);
    }
}
