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

    public final Runnable runnable(final Runnable original) {
        return () -> run(original);
    }

    public final <I> Consumer<I> consumer(final Consumer<I> original) {
        return input -> accept(original, input);
    }

    public final <I1, I2> BiConsumer<I1, I2> biConsumer(final BiConsumer<I1, I2> original) {
        return (i1, i2) -> accept(original, i1, i2);
    }

    public final <O> Supplier<O> supplier(final Supplier<O> original) {
        return () -> get(original);
    }

    public final <I, O> Function<I, O> function(final Function<I, O> original) {
        return input -> apply(original, input);
    }

    public final <I1, I2, O> BiFunction<I1, I2, O> biFunction(final BiFunction<I1, I2, O> original) {
        return (i1, i2) -> apply(original, i1, i2);
    }
}
