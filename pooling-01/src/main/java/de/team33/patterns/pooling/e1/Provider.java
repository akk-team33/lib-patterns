package de.team33.patterns.pooling.e1;

import de.team33.patterns.exceptional.e1.XConsumer;
import de.team33.patterns.exceptional.e1.XFunction;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

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
 * <p>
 * This implementation does not support checked exceptions to occur while creating new <em>subject</em> instances.
 *
 * @param <S> The type of provided instances <em>(subjects)</em>.
 * @see XProvider
 */
public class Provider<S> extends Mutual<S, RuntimeException> {

    /**
     * Initializes a new instance giving a {@link Supplier} that defines the intended initialization of a
     * new <em>subject</em>.
     */
    public Provider(final Supplier<S> newItem) {
        super(newItem::get);
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
    public final <X extends Exception> void runEx(final XConsumer<? super S, X> consumer) throws X {
        call(consumer);
    }

    /**
     * Runs a given {@link Consumer} with a parameter provided for it. The parameter is kept for future use.
     * <p>
     * While the {@link Consumer} is being executed, the parameter is exclusively available to it, but must not be
     * "hijacked" from the context of the execution or the executing thread!
     */
    public final void run(final Consumer<? super S> consumer) {
        call(consumer::accept);
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
    public final <R, X extends Exception> R getEx(final XFunction<? super S, R, X> function) throws X {
        return apply(function);
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
    public final <R> R get(final Function<? super S, R> function) {
        return apply(function::apply);
    }
}
