package de.team33.patterns.lazy.narvi;

import de.team33.patterns.exceptional.dione.XSupplier;

/**
 * @see de.team33.patterns.lazy.narvi package
 * @deprecated consider module
 * <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/lazy-janus/apidocs/">lazy-janus</a>
 * as a replacement.
 */
@Deprecated
public final class XReLazy<T, X extends Exception> extends ReMutual<T, X, XReLazy<T, X>> implements XSupplier<T, X> {

    private XReLazy(final XSupplier<? extends T, ? extends X> initial) {
        super(initial);
    }

    /**
     * @deprecated see {@link XReLazy}.
     */
    @Deprecated
    public static <T, X extends Exception> XReLazy<T, X> init(final XSupplier<? extends T, ? extends X> initial) {
        return new XReLazy<>(initial);
    }

    /**
     * @deprecated see {@link XReLazy}.
     */
    @Deprecated
    @Override
    public final T get() throws X {
        return super.get();
    }
}
