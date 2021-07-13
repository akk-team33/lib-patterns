package de.team33.patterns.exceptional.v1;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * An unchecked exception dedicated to wrap checked exceptions.
 */
public class WrappedException extends RuntimeException {

    private static final String MISSING_EXCEPTION =
            "Missing: an exception to be wrapped in a " + WrappedException.class.getSimpleName();

    /**
     * Initializes a new instance with the given message and cause.
     */
    public WrappedException(final String message, final Throwable cause) {
        super(toMessage(message, cause), toCause(cause));
    }

    /**
     * Initializes a new instance with the given cause and its {@link Throwable#getMessage() message}.
     */
    public WrappedException(final Throwable cause) {
        super(toMessage(null, cause), toCause(cause));
    }

    private static String toMessage(final String message, final Throwable cause) {
        final boolean messageIsMissing = (null == message);
        final boolean causeIsMissing = (null == cause);
        final List<String> result = Arrays.asList(
                (messageIsMissing || causeIsMissing) ? "Wrapped:" : null,
                causeIsMissing ? "nothing!?" : null,
                (causeIsMissing && !messageIsMissing) ? "Message:" : null,
                (messageIsMissing && !causeIsMissing) ? toMessage(cause) : null,
                message

        );
        return result.stream()
                     .filter(Objects::nonNull)
                     .collect(Collectors.joining(" "));
    }

    private static String toMessage(final Throwable cause) {
        return Optional.ofNullable(cause.getMessage())
                       .orElseGet(() -> cause.getClass().getCanonicalName());
    }

    private static Throwable toCause(final Throwable cause) {
        return (null != cause) ? cause : new IllegalStateException(MISSING_EXCEPTION);
    }
}
