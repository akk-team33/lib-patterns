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
     * Runs a {@link Consumer} supplied with the appropriate input managed by this provider.
     * The input is available to the {@link Consumer} during its runtime and in the executing thread
     * exclusively, but it must not be "hijacked" out of this context!
     */
    public final void run(final Consumer<? super T> consumer) {
        get(item -> {
            consumer.accept(item);
            return null;
        });
    }

    /**
     * Performs a {@link Function} that is provided with a corresponding parameter managed by this provider
     * and returns its result. The parameter is exclusively available to the {@link Function} during its
     * runtime and in the executing thread, but it must not be "hijacked" out of this context!
     */
    public final <R> R get(final Function<? super T, ? extends R> function) {
        final T item = Optional.ofNullable(stock.poll()).orElseGet(newItem);
        try {
            return function.apply(item);
        } finally {
            stock.add(item);
        }
    }
}
