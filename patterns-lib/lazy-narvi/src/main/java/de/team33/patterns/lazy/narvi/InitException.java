package de.team33.patterns.lazy.narvi;

import de.team33.patterns.exceptional.dione.Converter;

/**
 * @see de.team33.patterns.lazy.narvi package
 * @deprecated consider module
 * <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/lazy-janus/apidocs/">lazy-janus</a>
 * as a replacement.
 */
@Deprecated
public final class InitException extends RuntimeException {

    @SuppressWarnings("StaticMethodOnlyUsedInOneClass")
    static final Converter CNV = Converter.using(InitException::new);

    private InitException(final Throwable cause) {
        super(cause.getMessage(), cause);
    }
}
