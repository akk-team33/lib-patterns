package de.team33.patterns.lazy.narvi;

import de.team33.patterns.exceptional.dione.Converter;
import de.team33.patterns.exceptional.dione.XSupplier;

/**
 * An unchecked exception type that may be thrown on {@link Lazy#get()} or {@link ReLazy#get()},
 * when their initialization code causes a checked exception.
 *
 * @see Lazy#initEx(XSupplier)
 * @see ReLazy#initEx(XSupplier)
 */
public final class InitException extends RuntimeException {

    @SuppressWarnings("StaticMethodOnlyUsedInOneClass")
    static final Converter CNV = Converter.using(InitException::new);

    private InitException(final Throwable cause) {
        super(cause.getMessage(), cause);
    }
}
