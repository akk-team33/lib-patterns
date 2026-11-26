package de.team33.patterns.exceptional.dione;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static de.team33.patterns.exceptional.dione.Mutual.normalized;


/**
 * A tool class that can convert certain functional constructs defined in this module (e.g. {@link XFunction})
 * to more common ones (e.g. {@link Function}) that, when executed, will wrap any occurring checked exception in a
 * specific unchecked exception. Any unchecked exception that may occur will remain unaffected.
 * <p>
 * Use {@link #using(Function)} to get an instance.
 *
 * @see Conversion
 */
public final class Converter {

    private final Function<? super Throwable, ? extends RuntimeException> wrapping;

    private Converter(final Function<? super Throwable, ? extends RuntimeException> wrapping) {
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
    public static Converter using(final Function<? super Throwable, ? extends RuntimeException> wrapping) {
        return new Converter(wrapping);
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
     * when executed, wraps any occurring checked exception as a specific unchecked exception.
     *
     * @see #using(Function)
     */
    public final Runnable runnable(final XRunnable<?> xRunnable) {
        final XBiFunction<Void, Void, Void, ?> normal = normalized(xRunnable);
        return () -> call(normal, null, null);
    }

    /**
     * Converts an {@link XConsumer} that may throw a checked exception to a {@link Consumer} that,
     * when executed, wraps any occurring checked exception as a specific unchecked exception.
     *
     * @see #using(Function)
     */
    public final <T> Consumer<T> consumer(final XConsumer<? super T, ?> xConsumer) {
        final XBiFunction<T, Void, Void, ?> normal = normalized(xConsumer);
        return t -> call(normal, t, null);
    }

    /**
     * Converts an {@link XBiConsumer} that may throw a checked exception to a {@link BiConsumer} that,
     * when executed, wraps any occurring checked exception as a specific unchecked exception.
     *
     * @see #using(Function)
     */
    public final <T, U> BiConsumer<T, U> biConsumer(final XBiConsumer<? super T, ? super U, ?> xBiConsumer) {
        final XBiFunction<T, U, Void, ?> normal = normalized(xBiConsumer);
        return (t, u) -> call(normal, t, u);
    }

    /**
     * Converts an {@link XSupplier} that may throw a checked exception to a {@link Supplier} that,
     * when executed, wraps any occurring checked exception as a specific unchecked exception.
     *
     * @see #using(Function)
     */
    public final <R> Supplier<R> supplier(final XSupplier<? extends R, ?> xSupplier) {
        final XBiFunction<Void, Void, R, ?> normal = normalized(xSupplier);
        return () -> call(normal, null, null);
    }

    /**
     * Converts an {@link XPredicate} that may throw a checked exception to a {@link Predicate} that,
     * when executed, wraps any occurring checked exception as a specific unchecked exception.
     *
     * @see #using(Function)
     */
    public final <T> Predicate<T> predicate(final XPredicate<? super T, ?> xPredicate) {
        final XBiFunction<T, Void, Boolean, ?> normal = normalized(xPredicate);
        return t -> call(normal, t, null);
    }

    /**
     * Converts an {@link XBiPredicate} that may throw a checked exception to a {@link BiPredicate} that,
     * when executed, wraps any occurring checked exception as a specific unchecked exception.
     *
     * @see #using(Function)
     */
    public final <T, U> BiPredicate<T, U> biPredicate(final XBiPredicate<? super T, ? super U, ?> xBiPredicate) {
        final XBiFunction<T, U, Boolean, ?> normal = normalized(xBiPredicate);
        return (t, u) -> call(normal, t, u);
    }

    /**
     * Converts an {@link XFunction} that may throw a checked exception to a {@link Function} that,
     * when executed, wraps any occurring checked exception as a specific unchecked exception.
     *
     * @see #using(Function)
     */
    public final <T, R> Function<T, R> function(final XFunction<? super T, ? extends R, ?> xFunction) {
        final XBiFunction<T, Void, R, ?> normal = normalized(xFunction);
        return t -> call(normal, t, null);
    }

    /**
     * Converts an {@link XBiFunction} that may throw a checked exception to a {@link BiFunction} that,
     * when executed, wraps any occurring checked exception as a specific unchecked exception.
     *
     * @see #using(Function)
     */
    public final <T, U, R> BiFunction<T, U, R>
    biFunction(final XBiFunction<? super T, ? super U, ? extends R, ?> xBiFunction) {
        return (t, u) -> call(xBiFunction, t, u);
    }

    /**
     * Runs a given {@link XRunnable} that wraps a checked exception that may occur as a specific unchecked exception.
     *
     * @see #using(Function)
     */
    public final void run(final XRunnable<?> xRunnable) {
        call(normalized(xRunnable), null, null);
    }

    /**
     * Returns the result of a given {@link XSupplier} and wraps any checked exception that may occur as a
     * specific unchecked exception.
     *
     * @see #using(Function)
     *
     * @param <R> The result type.
     */
    public final <R> R get(final XSupplier<R, ?> xSupplier) {
        return call(normalized(xSupplier), null, null);
    }
}
