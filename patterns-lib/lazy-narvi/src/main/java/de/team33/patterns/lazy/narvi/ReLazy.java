package de.team33.patterns.lazy.narvi;

import de.team33.patterns.exceptional.dione.XSupplier;

import java.util.function.Supplier;

import static de.team33.patterns.lazy.narvi.InitException.CNV;

/**
 * Implements a {@link Supplier} that provides a virtually <em>fixed value</em><sup>(1)</sup>.
 * That value is only actually determined when it is accessed for the <em>first time</em><sup>(2)</sup>.
 * <p>
 * This implementation ensures that the {@linkplain #init(Supplier) originally defined initialization code}
 * is called at most once<sup>(1)</sup>, even if there is concurrent access from multiple threads, unless the
 * initialization attempt causes an (unchecked) exception.
 * <p>
 * Once the value is established, unnecessary effort to synchronize competing<sup>(3)</sup> read accesses is avoided.
 * <p>
 * <sup>(1)</sup> until {@link #reset()}.<br>
 * <sup>(2)</sup> after initialization or after latest {@link #reset()}.<br>
 * <sup>(3)</sup> Pure read accesses are of course not really competing.
 *
 * @see Lazy
 * @see XReLazy
 */
public final class ReLazy<T> extends ReMutual<T, RuntimeException, ReLazy<T>> implements Supplier<T> {

    private ReLazy(final Supplier<? extends T> initial) {
        super(initial::get);
    }

    /**
     * Returns a new instance giving a {@link Supplier} that defines the intended initialization of the
     * represented value.
     *
     * @param <T> The result type of the initialisation code.
     * @see #initEx(XSupplier)
     */
    public static <T> ReLazy<T> init(final Supplier<? extends T> initial) {
        return new ReLazy<>(initial);
    }

    /**
     * Returns a new instance giving an {@link XSupplier} that defines the intended initialization of the
     * represented value. The initialization code may throw a checked exception. If so, it is caught, wrapped in an
     * {@link InitException}, and rethrown.
     *
     * @param <T> The result type of the initialisation code.
     * @see #init(Supplier)
     */
    public static <T> ReLazy<T> initEx(final XSupplier<? extends T, ?> initial) {
        return init(CNV.supplier(initial));
    }

    /**
     * {@inheritDoc}
     * <p>
     * This implementation executes the {@linkplain #init(Supplier) originally defined initialization code} once
     * on the first call<sup>(1)</sup> and returns its result on that and every subsequent call without executing
     * the initialization code again<sup>(2)</sup>.
     * <p>
     * This implementation is thread safe.
     * <p>
     * <sup>(1)</sup> after creation or after latest {@link #reset()}.<br>
     * <sup>(2)</sup> until next {@link #reset()}.
     */
    @Override
    public final T get() {
        return super.get();
    }
}
