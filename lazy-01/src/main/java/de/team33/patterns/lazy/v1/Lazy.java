package de.team33.patterns.lazy.v1;

import java.util.function.Supplier;

/**
 * <p>Implements a {@link Supplier} that provides a virtually fixed value.
 * This value is only actually determined when it is accessed for the first time.</p>
 *
 * <p>This implementation ensures that the {@linkplain #Lazy(Supplier) originally defined initialization code}
 * is called at most once, even when there is concurrent access from multiple threads.</p>
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
