package de.team33.patterns.lazy.narvi;

import de.team33.patterns.exceptional.dione.XSupplier;

/**
 * @see de.team33.patterns.lazy.narvi package
 * @deprecated consider module
 * <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/lazy-janus/apidocs/">lazy-janus</a>
 * as a replacement.
 */
@Deprecated
public final class XLazy<T, X extends Exception> extends Mutual<T, X> implements XSupplier<T, X> {

    private XLazy(final XSupplier<? extends T, ? extends X> initial) {
        super(initial);
    }

    /**
     * @deprecated see {@link XLazy}.
     */
    @Deprecated
    public static <T, X extends Exception> XLazy<T, X> init(final XSupplier<? extends T, ? extends X> initial) {
        return new XLazy<>(initial);
    }

    /**
     * @deprecated see {@link XLazy}.
     */
    @Deprecated
    @Override
    public final T get() throws X {
        return super.get();
    }
}
