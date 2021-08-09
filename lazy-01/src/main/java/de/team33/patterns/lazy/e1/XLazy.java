package de.team33.patterns.lazy.e1;

import de.team33.patterns.exceptional.e1.XSupplier;

/**
 * Implements an {@link XSupplier} that provides a virtually fixed value.
 * This value is only actually determined when it is accessed for the first time.
 * <p>
 * This implementation ensures that the {@linkplain #XLazy(XSupplier) originally defined initialization code}
 * is called at most once, even if there is concurrent access from multiple threads, unless the
 * initialization attempt causes an exception.
 */
public class XLazy<T, X extends Exception> extends LazyBase<T, X> implements XSupplier<T, X> {

    /**
     * Initializes a new instance giving a supplier that defines the intended initialization of the
     * represented value.
     */
    public XLazy(final XSupplier<T, X> initial) {
        super(initial);
    }

    @Override
    public final T get() throws X {
        return backing.get();
    }
}
