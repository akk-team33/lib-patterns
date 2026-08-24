package de.team33.patterns.lazy.narvi;

import de.team33.patterns.exceptional.dione.XSupplier;

import java.util.function.Supplier;

import static de.team33.patterns.lazy.narvi.InitException.CNV;

/**
 * @see de.team33.patterns.lazy.narvi package
 * @deprecated consider module
 * <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/lazy-janus/apidocs/">lazy-janus</a>
 * as a replacement.
 */
@Deprecated
public final class ReLazy<T> extends ReMutual<T, RuntimeException, ReLazy<T>> implements Supplier<T> {

    private ReLazy(final Supplier<? extends T> initial) {
        super(initial::get);
    }

    /**
     * @deprecated see {@link ReLazy}.
     */
    @Deprecated
    public static <T> ReLazy<T> init(final Supplier<? extends T> initial) {
        return new ReLazy<>(initial);
    }

    /**
     * @deprecated see {@link ReLazy}.
     */
    @Deprecated
    public static <T> ReLazy<T> initEx(final XSupplier<? extends T, ?> initial) {
        return init(CNV.supplier(initial));
    }

    /**
     * @deprecated see {@link ReLazy}.
     */
    @Deprecated
    @Override
    public final T get() {
        return super.get();
    }
}
