package de.team33.patterns.lazy.e1;

import java.util.function.Supplier;

/**
 * Implements a {@link Supplier} that provides a virtually fixed value.
 * This value is only actually determined when it is accessed for the first time.
 * <p>
 * This implementation ensures that the {@linkplain #Lazy(Supplier) originally defined initialization code} is called
 * at most once, even if there is concurrent access from multiple threads, unless the initialization attempt causes a
 * {@link RuntimeException}.
 */
public class Lazy<T> extends LazyBase<T, RuntimeException> implements Supplier<T> {

    /**
     * Initializes a new instance giving a supplier that defines the intended initialization of the represented value.
     */
    public Lazy(final Supplier<T> initial) {
        super(initial::get);
    }

    @Override
    public final T get() {
        return backing.get();
    }
}
