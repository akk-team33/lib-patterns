package de.team33.patterns.lazy.narvi;

import de.team33.patterns.exceptional.dione.XSupplier;

import java.util.function.Supplier;

/**
 * Implements a {@linkplain XSupplier kind of supplier} that provides a virtually <em>fixed value</em><sup>(1)</sup>.
 * That value is only actually determined when it is accessed for the <em>first time</em><sup>(2)</sup>.
 * <p>
 * This implementation ensures that the {@linkplain #init(XSupplier) originally defined initialization code}
 * is called at most once<sup>(1)</sup>, even if there is concurrent access from multiple threads, unless the
 * initialization attempt causes an exception.
 * <p>
 * Once the value is established, unnecessary effort to synchronize competing<sup>(3)</sup> read accesses is avoided.
 * <p>
 * <sup>(1)</sup> until {@link #reset()}.<br>
 * <sup>(2)</sup> after initialization or after latest {@link #reset()}.<br>
 * <sup>(3)</sup> Pure read accesses are of course not really competing.
 *
 * @see XLazy
 * @see ReLazy
 */
public final class XReLazy<T, X extends Exception> extends ReMutual<T, X, XReLazy<T, X>> implements XSupplier<T, X> {

    private XReLazy(final XSupplier<? extends T, ? extends X> initial) {
        super(initial);
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
     * {@inheritDoc}
     * <p>
     * This implementation executes the {@linkplain #init(XSupplier) originally defined initialization code} once
     * on the first call<sup>(1)</sup> and returns its result on that and every subsequent call without executing
     * the initialization code again<sup>(2)</sup>.
     * <p>
     * This implementation is thread safe.
     * <p>
     * <sup>(1)</sup> after initialization or after latest {@link #reset()}.<br>
     * <sup>(2)</sup> until next {@link #reset()}.
     */
    @Override
    public final T get() throws X {
        return super.get();
    }
}
