package de.team33.patterns.lazy.narvi;

/**
 * An unchecked exception type that may be thrown, when the initialization code of a {@link Lazy} instance
 * causes a checked exception.
 */
public class LazyException extends RuntimeException {

    LazyException(Throwable cause) {
        super(cause.getMessage(), cause);
    }
}
