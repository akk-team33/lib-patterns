package de.team33.patterns.lazy.narvi;

import de.team33.patterns.exceptional.e1.Converter;
import de.team33.patterns.exceptional.e1.XSupplier;

import java.util.function.Supplier;

/**
 * Implements a kind of supplier that provides a virtually fixed value.
 * That value is only actually determined when it is accessed for the first time.
 * <p>
 * This implementation ensures that the {@linkplain #Lazy(Supplier) originally defined initialization code}
 * is called at most once, even if there is concurrent access from multiple threads, unless the
 * initialization attempt causes a {@link RuntimeException}.
 * <p>
 * Once the value is established, unnecessary effort to synchronize competing* read accesses is avoided.
 * <p>
 * *Pure read accesses are of course not really competing.
 */
public class Lazy<T> {

    private static final Converter CNV = Converter.using(InitException::new);

    private volatile Supplier<T> backing;

    private Lazy(final Supplier<? extends T> initial) {
        this.backing = new Initial(initial);
    }

    /**
     * Returns a new {@link Lazy} instance giving a supplier that defines the intended initialization of the
     * represented value.
     *
     * @param <T> The result type of the initialisation code.
     */
    public static <T> Lazy<T> init(final Supplier<? extends T> initial) {
        return new Lazy<>(initial);
    }

    /**
     * Wrappes an initialization code that may throw a checked exception into a normal {@link Supplier}
     * <p>
     * Wraps initialization code that might throw a checked exception in a 'normal' {@link Supplier},
     * which in turn wraps such an exception in a {@link InitException} (an unchecked exception).
     *
     * @param <T> The result type of the initialisation code.
     */
    public static <T> Supplier<T> supplier(final XSupplier<T, ?> xSupplier) {
        return CNV.supplier(xSupplier);
    }

    /**
     * Executes the {@linkplain #init(Supplier) originally defined initialization code} once on the first call
     * and returns its result on that and every subsequent call without executing the initialization code again.
     * This method is thread safe.
     */
    public final T get() {
        return backing.get();
    }

    private class Initial implements Supplier<T> {

        private final Supplier<? extends T> initial;

        private Initial(final Supplier<? extends T> initial) {
            this.initial = initial;
        }

        @Override
        public final synchronized T get() {
            if (backing == this) {
                final T result = initial.get();
                backing = () -> result;
            }
            return backing.get();
        }
    }

    /**
     * An unchecked exception type that may be thrown, when the initialization code of a {@link Lazy} instance
     * causes a checked exception.
     */
    public static class InitException extends RuntimeException {

        private InitException(final Throwable cause) {
            super(cause.getMessage(), cause);
        }
    }
}
