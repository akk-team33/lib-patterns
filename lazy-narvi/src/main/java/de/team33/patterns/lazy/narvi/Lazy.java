package de.team33.patterns.lazy.narvi;

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

    private volatile Supplier<T> backing;

    /**
     * Initializes a new instance giving a supplier that defines the intended initialization of the
     * represented value.
     */
    public Lazy(final Supplier<? extends T> initial) {
        this.backing = new Initial(initial);
    }

    /**
     * Executes the {@linkplain #Lazy(Supplier) originally defined initialization code} on the first call and
     * returns its result on that and every subsequent call without calling the supplier again.
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
        public final T get() {
            synchronized (this) {
                if (backing == this) {
                    final T result = initial.get();
                    backing = () -> result;
                }
            }
            return backing.get();
        }
    }
}
