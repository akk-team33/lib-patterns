package de.team33.patterns.lazy.narvi;

import de.team33.patterns.exceptional.dione.Converter;
import de.team33.patterns.exceptional.dione.XSupplier;

import java.util.function.Supplier;

/**
 * Implements a kind of {@link Supplier} that provides a virtually fixed value.
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
public final class ReLazy<T> extends Mutual<T, RuntimeException> implements Supplier<T> {

    private static final Converter CNV = Converter.using(InitException::new);
    private final Supplier<? extends T> initial;

    private ReLazy(final Supplier<? extends T> initial) {
        super(initial::get);
        this.initial = initial;
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
     * Executes the {@linkplain #init(Supplier) originally defined initialization code} once on the first call
     * and returns its result on that and every subsequent call without executing the initialization code again.
     * This method is thread safe.
     */
    @Override
    public final T get() {
        return super.get();
    }

    public final ReLazy<T> reset() {
        reset(initial::get);
        return this;
    }

    /**
     * An unchecked exception type that may be thrown, when the {@linkplain #initEx(XSupplier) initialization code}
     * of a {@link ReLazy} instance causes a checked exception.
     */
    public static final class InitException extends RuntimeException {

        private InitException(final Throwable cause) {
            super(cause.getMessage(), cause);
        }
    }
}
