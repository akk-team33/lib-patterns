package de.team33.patterns.exceptional.e1;

import java.util.Optional;

/**
 * An unchecked exception that is used to signal an unexpected state, particularly an unexpected exception.
 */
public class ExpectationException extends RuntimeException {

    /**
     * Initializes a new instance with the given message.
     */
    public ExpectationException(final String message) {
        this(new Parameters(message, null));
    }

    /**
     * Initializes a new instance with the given message and cause.
     */
    public ExpectationException(final String message, final Throwable cause) {
        this(new Parameters(message, cause));
    }

    /**
     * Initializes a new instance with the given cause and its {@link Throwable#getMessage() message}.
     */
    public ExpectationException(final Throwable cause) {
        this(new Parameters(null, cause));
    }

    private ExpectationException(final Parameters parameters) {
        super(parameters.message, parameters.cause);
    }

    private static final class Parameters {

        private final String message;
        private final Throwable cause;

        private Parameters(final String message, final Throwable cause) {
            this.message = (null != message) ? message : "Unexpected: " + Optional.ofNullable(cause)
                                                                                  .map(Parameters::toMessage)
                                                                                  .orElse("unknown condition");
            this.cause = cause;
        }

        private static String toMessage(final Throwable cause) {
            return Optional.ofNullable(cause.getMessage())
                           .orElseGet(() -> cause.getClass().getCanonicalName());
        }
    }
}
