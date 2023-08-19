package de.team33.patterns.exceptional.dione;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * A utility class that can convert certain functional constructs defined in this package (e.g. {@link XFunction})
 * to more common ones (e.g. {@link Function}) that, when executed, will wrap any occurring checked exception in a
 * {@link WrappedException}. Any unchecked exception that may occur will remain unaffected.
 *
 * @see Converter
 */
public final class Conversion {

    private static final Converter CONVERTER = Converter.using(WrappedException::new);

    private Conversion() {
    }

    /**
     * Converts an {@link XRunnable} that may throw a checked exception to a {@link Runnable} that,
     * when executed, wraps any occurring checked exception as a {@link WrappedException}.
     */
    public static Runnable runnable(final XRunnable<?> xRunnable) {
        return CONVERTER.runnable(xRunnable);
    }

    /**
     * Converts an {@link XConsumer} that may throw a checked exception to a {@link Consumer} that,
     * when executed, wraps any occurring checked exception as a {@link WrappedException}.
     */
    public static <T> Consumer<T> consumer(final XConsumer<T, ?> xConsumer) {
        return CONVERTER.consumer(xConsumer);
    }

    /**
     * Converts an {@link XBiConsumer} that may throw a checked exception to a {@link BiConsumer} that,
     * when executed, wraps any occurring checked exception as a {@link WrappedException}.
     */
    public static <T, U> BiConsumer<T, U> biConsumer(final XBiConsumer<T, U, ?> xBiConsumer) {
        return CONVERTER.biConsumer(xBiConsumer);
    }

    /**
     * Converts an {@link XSupplier} that may throw a checked exception to a {@link Supplier} that,
     * when executed, wraps any occurring checked exception as a {@link WrappedException}.
     */
    public static <R> Supplier<R> supplier(final XSupplier<R, ?> xSupplier) {
        return CONVERTER.supplier(xSupplier);
    }

    /**
     * Converts an {@link XPredicate} that may throw a checked exception to a {@link Predicate} that,
     * when executed, wraps any occurring checked exception as a {@link WrappedException}.
     */
    public static <T> Predicate<T> predicate(final XPredicate<T, ?> xPredicate) {
        return CONVERTER.predicate(xPredicate);
    }

    /**
     * Converts an {@link XBiPredicate} that may throw a checked exception to a {@link BiPredicate} that,
     * when executed, wraps any occurring checked exception as a {@link WrappedException}.
     */
    public static  <T, U> BiPredicate<T, U> biPredicate(final XBiPredicate<T, U, ?> xBiPredicate) {
        return CONVERTER.biPredicate(xBiPredicate);
    }

    /**
     * Converts an {@link XFunction} that may throw a checked exception to a {@link Function} that,
     * when executed, wraps any occurring checked exception as a {@link WrappedException}.
     */
    public static <T, R> Function<T, R> function(final XFunction<T, R, ?> xFunction) {
        return CONVERTER.function(xFunction);
    }

    /**
     * Converts an {@link XBiFunction} that may throw a checked exception to a {@link BiFunction} that,
     * when executed, wraps any occurring checked exception as a {@link WrappedException}.
     */
    public static <T, U, R> BiFunction<T, U, R> biFunction(final XBiFunction<T, U, R, ?> xBiFunction) {
        return CONVERTER.biFunction(xBiFunction);
    }

    /**
     * Runs a given {@link XRunnable} that wraps a checked exception that may occur as a {@link WrappedException}.
     */
    public static void run(final XRunnable<?> xRunnable) {
        CONVERTER.run(xRunnable);
    }

    /**
     * Returns the result of a given {@link XSupplier} and wraps any checked exception that may occur as a
     * {@link WrappedException}.
     *
     * @param <R> The result type.
     */
    public static <R> R get(final XSupplier<R, ?> xSupplier) {
        return CONVERTER.get(xSupplier);
    }
}
