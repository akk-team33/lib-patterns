package de.team33.patterns.exceptional.v1;

import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * An unchecked exception dedicated to wrap checked exceptions.
 */
public class WrappedException extends RuntimeException {

    private static final String MISSING_CAUSE = "A " +
            WrappedException.class.getSimpleName() +
            " is expected to have a real cause but was <null>";

    /**
     * Initializes a new instance with the given message and cause.
     */
    public WrappedException(final String message, final Throwable cause) {
        this(requireNonNull(cause, MISSING_CAUSE), message);
    }

    /**
     * Initializes a new instance with the given cause and its {@link Throwable#getMessage() message}.
     */
    public WrappedException(final Throwable cause) {
        this(requireNonNull(cause, MISSING_CAUSE), null);
    }

    private WrappedException(final Throwable cause, final String message) {
        super(Optional.ofNullable(message)
                      .orElseGet(() -> stdMessage(cause)), cause);
    }

    private static String stdMessage(final Throwable cause) {
        return "wrapped: " + Optional.ofNullable(cause.getMessage())
                                     .orElseGet(() -> cause.getClass().getCanonicalName());
    }
}
