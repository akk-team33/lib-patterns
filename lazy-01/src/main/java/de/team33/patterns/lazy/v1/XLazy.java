package de.team33.patterns.lazy.v1;

import de.team33.patterns.exceptional.v1.XSupplier;

/**
 * <p>Implements an {@link XSupplier} that provides a virtually fixed value.
 * This value is only actually determined when it is accessed for the first time.</p>
 *
 * <p>This implementation ensures that the {@linkplain #XLazy(XSupplier) originally defined initialization code}
 * is called at most once, even when there is concurrent access from multiple threads.</p>
 */
public class XLazy<T, X extends Exception> extends LazyBase<T, X> implements XSupplier<T, X> {

    /**
     * Initializes a new instance giving a supplier that defines the intended initialization of the represented value.
     */
    public XLazy(final XSupplier<T, X> initial) {
        super(initial);
    }

    /**
     * <p>Returns the represented value.</p>
     * <p>That value was determined once with the first access using the
     * {@linkplain #XLazy(XSupplier) originally defined initialization code}.</p>
     */
    @Override
    public final T get() throws X {
        return backing.get();
    }
}
