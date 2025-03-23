package de.team33.patterns.lazy.narvi;

import de.team33.patterns.exceptional.dione.Converter;
import de.team33.patterns.exceptional.dione.XSupplier;

import java.util.function.Supplier;

/**
 * Implements a {@link Supplier} that provides a virtually fixed value.
 * That value is only actually determined when it is accessed for the first time.
 * <p>
 * This implementation ensures that the {@linkplain #init(Supplier) originally defined initialization code}
 * is called at most once, even if there is concurrent access from multiple threads, unless the
 * initialization attempt causes an (unchecked) exception.
 * <p>
 * Once the value is established, unnecessary effort to synchronize competing* read accesses is avoided.
 * <p>
 * *Pure read accesses are of course not really competing.
 *
 * @see XLazy
 * @see ReLazy
 */
public final class Lazy<T> extends Mutual<T, RuntimeException> implements Supplier<T> {

    @SuppressWarnings("DeprecatedIsStillUsed")
    @Deprecated(forRemoval = true)
    private static final Converter CNV = Converter.using(Lazy.InitException::new);

    private Lazy(final Supplier<? extends T> initial) {
        super(initial::get);
    }

    /**
     * Returns a new instance giving a {@link Supplier} that defines the intended initialization of the
     * represented value.
     *
     * @param <T> The result type of the initialisation code.
     * @see #initEx(XSupplier)
     */
    public static <T> Lazy<T> init(final Supplier<? extends T> initial) {
        return new Lazy<>(initial);
    }

    /**
     * Returns a new instance giving an {@link XSupplier} that defines the intended initialization of the
     * represented value. The initialization code may throw a checked exception. If so, it is caught, wrapped in an
     * {@link de.team33.patterns.lazy.narvi.InitException}, and rethrown.
     *
     * @param <T> The result type of the initialisation code.
     * @see #init(Supplier)
     */
    public static <T> Lazy<T> initEx(final XSupplier<? extends T, ?> initial) {
        return init(CNV.supplier(initial));
    }

    /**
     * Executes the {@linkplain #init(Supplier) originally defined initialization code} once on the first call
     * and returns its result on that and every subsequent call without executing the initialization code again.
     * This method is thread safe.
     */
    @Override
    public final T get() {
        return super.get();
    }

    /**
     * @deprecated use {@link de.team33.patterns.lazy.narvi.InitException} instead!
     */
    @SuppressWarnings({"ClassNameSameAsAncestorName", "DeprecatedIsStillUsed"})
    @Deprecated(forRemoval = true)
    public static final class InitException extends de.team33.patterns.lazy.narvi.InitException {

        private InitException(final Throwable cause) {
            super(cause);
        }
    }
}
