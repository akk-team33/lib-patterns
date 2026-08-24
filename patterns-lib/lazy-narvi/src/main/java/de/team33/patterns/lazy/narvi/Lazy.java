package de.team33.patterns.lazy.narvi;

import de.team33.patterns.exceptional.dione.XSupplier;

import java.util.function.Supplier;

import static de.team33.patterns.lazy.narvi.InitException.CNV;

/**
 * @see <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/lazy-janus/">lazy-janus</a>
 * @see <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/lazy-janus/apidocs/">lazy-janus/apidocs</a>
 * @deprecated consider class Lazy from module
 * <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/lazy-janus/apidocs/">lazy-janus</a>
 * as a replacement.
 */
@Deprecated
public final class Lazy<T> extends Mutual<T, RuntimeException> implements Supplier<T> {

    private Lazy(final Supplier<? extends T> initial) {
        super(initial::get);
    }

    /**
     * @deprecated see {@link Lazy}.
     */
    @Deprecated
    public static <T> Lazy<T> init(final Supplier<? extends T> initial) {
        return new Lazy<>(initial);
    }

    /**
     * @deprecated see {@link Lazy}.
     */
    @Deprecated
    public static <T> Lazy<T> initEx(final XSupplier<? extends T, ?> initial) {
        return init(CNV.supplier(initial));
    }

    /**
     * @deprecated see {@link Lazy}.
     */
    @Deprecated
    @Override
    public final T get() {
        return super.get();
    }
}
