package de.team33.patterns.pooling.v1;

import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A {@link Provider} can execute a <em>code segment</em> ({@link Consumer} / {@link Function}) while it exclusively
 * provides a suitable <em>parameter</em> for it.
 * <p>
 * The <em>parameter</em> is typically used by the <em>code segment</em> to perform its task, but is not suitable as
 * part of the result, as it would otherwise be beyond the control of the {@link Provider}. Likewise, it must not be
 * passed on to asynchronous threads if the <em>code segment</em> interacts with any.
 * <p>
 * A {@link Provider} is particularly suitable for the administration of <em>parameters</em> whose instantiation is
 * relatively "expensive" on the one hand, but which, on the other hand, cannot be instantiated as a static singleton
 * without any problems, because they are, for example, not thread-safe.
 * <p>
 * Example:
 * <pre>
 * import java.util.Random;
 *
 * public class Example {
 *
 *     private static final Provider&lt;Random&gt; RANDOM = new Provider&lt;&gt;(Random::new);
 *
 *     public void apply() {
 *         final int anyInt = RANDOM.get(Random::nextInt);
 *         System.out.append("anyInt = ")
 *                   .println(anyInt);
 *     }
 * }
 * </pre>
 *
 * @param <P> The type of provided parameters
 */
public class Provider<P> {

    private static final Object IRRELEVANT = null;

    private final Queue<P> stock = new ConcurrentLinkedQueue<>();
    private final Supplier<P> newItem;

    public Provider(final Supplier<P> newItem) {
        this.newItem = newItem;
    }

    /**
     * Runs a given {@link Consumer} with a parameter provided for it. The parameter is kept for future use.
     * <p>
     * While the {@link Consumer} is being executed, the parameter is exclusively available to it, but must not be
     * "hijacked" from the context of the execution or the executing thread!
     */
    public final void run(final Consumer<? super P> consumer) {
        get(parameter -> {
            consumer.accept(parameter);
            return IRRELEVANT;
        });
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
    public final <R> R get(final Function<? super P, ? extends R> function) {
        final P item = Optional.ofNullable(stock.poll())
                               .orElseGet(newItem);
        try {
            return function.apply(item);
        } finally {
            stock.add(item);
        }
    }
}
