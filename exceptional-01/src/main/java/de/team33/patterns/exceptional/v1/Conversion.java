package de.team33.patterns.exceptional.v1;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * A utility class that can convert certain functional constructs that may throw checked exceptions
 * (e.g. {@link XFunction}) into more common constructs (e.g. {@link Function}) that will wrap such exceptions in
 * {@link WrappedException}s.
 *
 * @see Converter
 */
public final class Conversion {

    private static final Converter CONVERTER = Converter.using(WrappedException::new);

    private Conversion() {
    }

    /**
     * Wraps an {@link XRunnable} that may throw a checked exception as {@link Runnable} that,
     * when executed, wraps any occurring checked exception as {@link WrappedException}.
     */
    public static Runnable runnable(final XRunnable<?> xRunnable) {
        return CONVERTER.runnable(xRunnable);
    }

    /**
     * Wraps an {@link XConsumer} that may throw a checked exception as {@link Consumer} that,
     * when executed, wraps any occurring checked exception as {@link WrappedException}.
     */
    public static <T> Consumer<T> consumer(final XConsumer<T, ?> xConsumer) {
        return CONVERTER.consumer(xConsumer);
    }

    /**
     * Wraps an {@link XBiConsumer} that may throw a checked exception as {@link BiConsumer} that,
     * when executed, wraps any occurring checked exception as {@link WrappedException}.
     */
    public static <T, U> BiConsumer<T, U> biConsumer(final XBiConsumer<T, U, ?> xBiConsumer) {
        return CONVERTER.biConsumer(xBiConsumer);
    }

    /**
     * Wraps an {@link XSupplier} that may throw a checked exception as {@link Supplier} that,
     * when executed, wraps any occurring checked exception as {@link WrappedException}.
     */
    public static <R> Supplier<R> supplier(final XSupplier<R, ?> xSupplier) {
        return CONVERTER.supplier(xSupplier);
    }

    /**
     * Wraps an {@link XPredicate} that may throw a checked exception as {@link Predicate} that,
     * when executed, wraps any occurring checked exception as {@link WrappedException}.
     */
    public static <T> Predicate<T> predicate(final XPredicate<T, ?> xPredicate) {
        return CONVERTER.predicate(xPredicate);
    }

    /**
     * Wraps an {@link XBiPredicate} that may throw a checked exception as {@link BiPredicate} that,
     * when executed, wraps any occurring checked exception as a specific unchecked exception.
     */
    public static  <T, U> BiPredicate<T, U> biPredicate(final XBiPredicate<T, U, ?> xBiPredicate) {
        return CONVERTER.biPredicate(xBiPredicate);
    }

    /**
     * Wraps an {@link XFunction} that may throw a checked exception as {@link Function} that,
     * when executed, wraps any occurring checked exception as {@link WrappedException}.
     */
    public static <T, R> Function<T, R> function(final XFunction<T, R, ?> xFunction) {
        return CONVERTER.function(xFunction);
    }

    /**
     * Wraps an {@link XBiFunction} that may throw a checked exception as {@link BiFunction} that,
     * when executed, wraps any occurring checked exception as a specific unchecked exception.
     */
    public static <T, U, R> BiFunction<T, U, R> biFunction(final XBiFunction<T, U, R, ?> xBiFunction) {
        return CONVERTER.biFunction(xBiFunction);
    }
}
