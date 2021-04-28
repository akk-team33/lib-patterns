package de.team33.patterns.exceptional.v1;

import java.util.Optional;

/**
 * An unchecked exception that is used to signal an unexpected state, particularly an unexpected exception.
 */
public class ExpectationException extends RuntimeException {

    /**
     * Initializes a new instance with the given message.
     */
    public ExpectationException(final String message) {
        super(message);
    }

    /**
     * Initializes a new instance with the given message and cause.
     */
    public ExpectationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Initializes a new instance with the given cause and its {@link Throwable#getMessage() message}.
     */
    public ExpectationException(final Throwable cause) {
        super("Unexpected: " + messageOf(cause), cause);
    }

    private static String messageOf(final Throwable cause) {
        return Optional.ofNullable(cause)
                       .map(ExpectationException::messageOfNonNull)
                       .orElse(null);
    }

    private static String messageOfNonNull(final Throwable cause) {
        return Optional.ofNullable(cause.getMessage())
                       .orElseGet(() -> cause.getClass().getCanonicalName());
    }
}
