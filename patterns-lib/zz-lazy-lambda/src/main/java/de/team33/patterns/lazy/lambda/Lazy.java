package de.team33.patterns.lazy.lambda;

import java.util.function.Supplier;

/**
 * Implements a {@link Supplier} that represents a fixed value.
 * That value is only actually determined when it is accessed for the first time.
 * <p>
 * This implementation ensures that the {@linkplain #init(Supplier) originally defined initialization code}
 * is called at most once, even if there is concurrent access from multiple threads, unless the
 * initialization attempt causes an (unchecked) exception.
 * <p>
 * Once the value is established, unnecessary effort to synchronize competing* read accesses is avoided.
 * <p>
 * *Pure read accesses are of course not really competing.
 */
public class Lazy<T> implements Supplier<T> {

    private volatile Supplier<T> backing;

    private Lazy(final Supplier<? extends T> initial) {
        this.backing = new Initial(initial);
    }

    /**
     * Returns a new instance giving a {@link Supplier} that defines the intended initialization
     * of the represented value.
     *
     * @param <T> The result type of the initialization code.
     */
    public static <T> Lazy<T> init(final Supplier<? extends T> initial) {
        return new Lazy<>(initial);
    }

    /**
     * Returns the represented value.
     * <p>
     * Executes the {@linkplain #init(Supplier) originally defined initialization code} once on the first call
     * and returns its result on that and every subsequent call without executing the initialization code again.
     * <p>
     * This implementation is thread safe.
     */
    public final T get() {
        return backing.get();
    }

    private final class Initial implements Supplier<T> {

        private final Supplier<? extends T> original;

        private Initial(final Supplier<? extends T> original) {
            this.original = original;
        }

        @SuppressWarnings("SynchronizedMethod")
        @Override
        public synchronized T get() {
            if (backing == this) {
                final T value = original.get();
                backing = () -> value;
            }
            return backing.get();
        }
    }
}
