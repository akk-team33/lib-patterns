package net.team33.patterns;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("SynchronizedMethod")
public class Monitor {

    public final synchronized void run(final Runnable runnable) {
        runnable.run();
    }

    public final synchronized <I> void accept(final Consumer<I> consumer, final I input) {
        consumer.accept(input);
    }

    public final synchronized <I1, I2> void accept(final BiConsumer<I1, I2> consumer,
                                                   final I1 input1, final I2 input2) {
        consumer.accept(input1, input2);
    }

    public final synchronized <O> O get(final Supplier<O> supplier) {
        return supplier.get();
    }

    public final synchronized <I, O> O apply(final Function<I, O> function, final I input) {
        return function.apply(input);
    }

    public final synchronized <I1, I2, O> O apply(final BiFunction<I1, I2, O> function,
                                                  final I1 input1, final I2 input2) {
        return function.apply(input1, input2);
    }
}
