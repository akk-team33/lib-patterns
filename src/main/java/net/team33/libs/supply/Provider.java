package net.team33.libs.supply;

import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;


/**
 * A {@link Provider} can provide a code segment ({@link Consumer} / {@link Function}) with a corresponding
 * parameter that is exclusively available to the relevant code segment for its runtime and in the executing
 * thread. The {@link Provider} will keep the parameter after its application for later applications,
 * so that not necessarily for each application a new instance must be created, which might be expensive.
 */
public class Provider<T> {

    private final Queue<T> stock = new ConcurrentLinkedQueue<>();
    private final Supplier<T> newItem;

    public Provider(final Supplier<T> newItem) {
        this.newItem = newItem;
    }

    /**
     * <p>Convenience method:</p>
     * <p>Runs a {@link Consumer} supplied with the appropriate input managed by this provider.
     * The input is available to the {@link Consumer} during its runtime and in the executing thread
     * exclusively, but it must not be "hijacked" out of this context!</p>
     * <p>Saves the explicit {@linkplain #provide() request} and {@linkplain #restore(Object) return}
     * of an item in a try-finally construct.</p>
     */
    public final void run(final Consumer<? super T> consumer) {
        get(item -> {
            consumer.accept(item);
            return null;
        });
    }

    /**
     * <p>Convenience method:</p>
     * <p>Performs a {@link Function} that is provided with a corresponding parameter managed by this provider
     * and returns its result. The parameter is exclusively available to the {@link Function} during its
     * runtime and in the executing thread, but it must not be "hijacked" out of this context!</p>
     * <p>Saves the explicit {@linkplain #provide() request} and {@linkplain #restore(Object) return}
     * of an item in a try-finally construct.</p>
     */
    public final <R> R get(final Function<? super T, ? extends R> function) {
        final T item = provide();
        try {
            return function.apply(item);
        } finally {
            restore(item);
        }
    }

    /**
     * <p>Provides an instance of the associated type for temporary use. It is intended that this instance
     * will only be used in the current thread, {@linkplain #restore(Object) returned to this provider}
     * after use, and then no longer used.</p>
     * <p>A typical application looks like this:</p>
     * <pre>
     * final T item = provide();
     * try {
     *     // do anything with the provided item ...
     * } finally {
     *     restore(item);
     * }
     * </pre>
     *
     * @see #restore(Object)
     */
    public final T provide() {
        return Optional.ofNullable(stock.poll()).orElseGet(newItem);
    }

    /**
     * <p>Returns an item previously {@linkplain #provide() provided} by this provider.</p>
     *
     * @see #provide()
     */
    public final void restore(final T item) {
        stock.add(item);
    }
}
