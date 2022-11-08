package de.team33.patterns.lazy.narvi;

import de.team33.patterns.exceptional.e1.XSupplier;

/**
 * Implements a kind of supplier that provides a virtually fixed value.
 * That value is only actually determined when it is accessed for the first time.
 * <p>
 * This implementation ensures that the {@linkplain #XLazy(XSupplier) originally defined initialization code}
 * is called at most once, even if there is concurrent access from multiple threads, unless the
 * initialization attempt causes an exception.
 * <p>
 * Once the value is established, unnecessary effort to synchronize competing* read accesses is avoided.
 * <p>
 * *Pure read accesses are of course not really competing.
 */
public class XLazy<T, X extends Exception> extends Mutual<T, X> implements XSupplier<T, X> {

    /**
     * Initializes a new instance giving a supplier that defines the intended initialization of the
     * represented value.
     */
    public XLazy(final XSupplier<? extends T, ? extends X> initial) {
        super(initial);
    }

    /**
     * Executes the {@linkplain #XLazy(XSupplier) originally defined initialization code} on the first call and
     * returns its result on that and every subsequent call without calling the supplier again.
     *
     * @throws X if the {@linkplain #XLazy(XSupplier) originally defined initialization code} throws one.
     */
    @Override
    public final T get() throws X {
        return backing.get();
    }
}