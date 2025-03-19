package de.team33.patterns.lazy.narvi;

import de.team33.patterns.exceptional.dione.XSupplier;

import java.util.function.Supplier;

/**
 * Implements a kind of supplier that provides a virtually fixed value*.
 * That value is only actually determined when it is accessed for the <em>first time</em>**.
 * <p>
 * This implementation ensures that the {@linkplain #init(XSupplier) originally defined initialization code}
 * is called at most once*, even if there is concurrent access from multiple threads, unless the
 * initialization attempt causes an exception.
 * <p>
 * Once the value is established, unnecessary effort to synchronize competing*** read accesses is avoided.
 * <p>
 * *until {@link #reset()}.<br>
 * **after initialization or after latest {@link #reset()}.<br>
 * ***Pure read accesses are of course not really competing.
 *
 * @see XLazy
 * @see ReLazy
 */
public final class XReLazy<T, X extends Exception> extends Mutual<T, X> implements XSupplier<T, X> {

    private final XSupplier<? extends T, ? extends X> initial;

    private XReLazy(final XSupplier<? extends T, ? extends X> initial) {
        super(initial);
        this.initial = initial;
    }

    /**
     * Returns a new instance giving a {@link Supplier} that defines the intended initialization of the
     * represented value.
     *
     * @param <T> The result type of the initialisation code.
     * @param <X> The exception type that may occur within initialisation.
     */
    public static <T, X extends Exception> XReLazy<T, X> init(final XSupplier<? extends T, ? extends X> initial) {
        return new XReLazy<>(initial);
    }

    /**
     * Executes the {@linkplain #init(XSupplier) originally defined initialization code} once on the first call
     * and returns its result on that and every subsequent call without executing the initialization code again.
     * This method is thread safe.
     */
    @Override
    public final T get() throws X {
        return super.get();
    }

    /**
     * Resets <em>this</em> to the initial state and returns <em>this</em>.
     */
    public final XReLazy<T, X> reset() {
        reset(initial);
        return this;
    }
}
