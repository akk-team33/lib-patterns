package de.team33.patterns.exceptional.e1;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;


/**
 * A tool class that can convert certain functional constructs defined in this package (e.g. {@link XFunction})
 * to more common ones (e.g. {@link Function}) that, when executed, will wrap any occurring checked exception in a
 * specific unchecked exception. Any unchecked exception that may occur will remain unaffected.
 *
 * @see Conversion
 */
public final class Converter {

    private final Function<Throwable, ? extends RuntimeException> wrapping;

    private Converter(final Function<Throwable, ? extends RuntimeException> wrapping) {
        this.wrapping = wrapping;
    }

    /**
     * Returns a new instance using a given wrapping method.
     *
     * @see Wrapping#method(BiFunction)
     * @see Wrapping#method(String, BiFunction)
     * @see Wrapping#varying(Function)
     * @see Wrapping#varying(String, Function)
     */
    public static Converter using(final Function<Throwable, ? extends RuntimeException> wrapping) {
        return new Converter(wrapping);
    }

    private static XBiFunction<Void, Void, Void, ?> normalized(final XRunnable<?> xRunnable) {
        return (t, u) -> {
            xRunnable.run();
            return null;
        };
    }

    private static <T> XBiFunction<T, Void, Void, ?> normalized(final XConsumer<T, ?> xConsumer) {
        return (t, u) -> {
            xConsumer.accept(t);
            return null;
        };
    }

    private static <T, U> XBiFunction<T, U, Void, ?> normalized(final XBiConsumer<T, U, ?> xBiConsumer) {
        return (t, u) -> {
            xBiConsumer.accept(t, u);
            return null;
        };
    }

    private static <R> XBiFunction<Void, Void, R, ?> normalized(final XSupplier<R, ?> xSupplier) {
        return (t, u) -> xSupplier.get();
    }

    private static <T> XBiFunction<T, Void, Boolean, ?> normalized(final XPredicate<T, ?> xPredicate) {
        return (t, u) -> xPredicate.test(t);
    }

    private static <T, U> XBiFunction<T, U, Boolean, ?> normalized(final XBiPredicate<T, U, ?> xBiPredicate) {
        return xBiPredicate::test;
    }

    private static <T, R> XBiFunction<T, Void, R, ?> normalized(final XFunction<T, R, ?> xFunction) {
        return (t, u) -> xFunction.apply(t);
    }

    @SuppressWarnings("ProhibitedExceptionThrown")
    private <T, U, R> R call(final XBiFunction<T, U, R, ?> xBiFunction, final T t, final U u) {
        try {
            return xBiFunction.apply(t, u);
        } catch (final RuntimeException caught) {
            throw caught;
        } catch (final Exception caught) {
            throw wrapping.apply(caught);
        }
    }

    /**
     * Converts an {@link XRunnable} that may throw a checked exception to a {@link Runnable} that,
     * when executed, wraps any occurring checked exception in a specific unchecked exception.
     *
     * @see #using(Function)
     */
    public final Runnable runnable(final XRunnable<?> xRunnable) {
        final XBiFunction<Void, Void, Void, ?> normal = normalized(xRunnable);
        return () -> call(normal, null, null);
    }

    /**
     * Converts an {@link XConsumer} that may throw a checked exception to a {@link Consumer} that,
     * when executed, wraps any occurring checked exception in a specific unchecked exception.
     *
     * @see #using(Function)
     */
    public final <T> Consumer<T> consumer(final XConsumer<T, ?> xConsumer) {
        final XBiFunction<T, Void, Void, ?> normal = normalized(xConsumer);
        return t -> call(normal, t, null);
    }

    /**
     * Converts an {@link XBiConsumer} that may throw a checked exception to a {@link BiConsumer} that,
     * when executed, wraps any occurring checked exception in a specific unchecked exception.
     *
     * @see #using(Function)
     */
    public final <T, U> BiConsumer<T, U> biConsumer(final XBiConsumer<T, U, ?> xBiConsumer) {
        final XBiFunction<T, U, Void, ?> normal = normalized(xBiConsumer);
        return (t, u) -> call(normal, t, u);
    }

    /**
     * Converts an {@link XSupplier} that may throw a checked exception to a {@link Supplier} that,
     * when executed, wraps any occurring checked exception in a specific unchecked exception.
     *
     * @see #using(Function)
     */
    public final <R> Supplier<R> supplier(final XSupplier<R, ?> xSupplier) {
        final XBiFunction<Void, Void, R, ?> normal = normalized(xSupplier);
        return () -> call(normal, null, null);
    }

    /**
     * Converts an {@link XPredicate} that may throw a checked exception to a {@link Predicate} that,
     * when executed, wraps any occurring checked exception in a specific unchecked exception.
     *
     * @see #using(Function)
     */
    public final <T> Predicate<T> predicate(final XPredicate<T, ?> xPredicate) {
        final XBiFunction<T, Void, Boolean, ?> normal = normalized(xPredicate);
        return t -> call(normal, t, null);
    }

    /**
     * Converts an {@link XBiPredicate} that may throw a checked exception to a {@link BiPredicate} that,
     * when executed, wraps any occurring checked exception in a specific unchecked exception.
     *
     * @see #using(Function)
     */
    public final <T, U> BiPredicate<T, U> biPredicate(final XBiPredicate<T, U, ?> xBiPredicate) {
        final XBiFunction<T, U, Boolean, ?> normal = normalized(xBiPredicate);
        return (t, u) -> call(normal, t, u);
    }

    /**
     * Converts an {@link XFunction} that may throw a checked exception to a {@link Function} that,
     * when executed, wraps any occurring checked exception in a specific unchecked exception.
     *
     * @see #using(Function)
     */
    public final <T, R> Function<T, R> function(final XFunction<T, R, ?> xFunction) {
        final XBiFunction<T, Void, R, ?> normal = normalized(xFunction);
        return t -> call(normal, t, null);
    }

    /**
     * Converts an {@link XBiFunction} that may throw a checked exception to a {@link BiFunction} that,
     * when executed, wraps any occurring checked exception in a specific unchecked exception.
     *
     * @see #using(Function)
     */
    public final <T, U, R> BiFunction<T, U, R> biFunction(final XBiFunction<T, U, R, ?> xBiFunction) {
        return (t, u) -> call(xBiFunction, t, u);
    }
}
