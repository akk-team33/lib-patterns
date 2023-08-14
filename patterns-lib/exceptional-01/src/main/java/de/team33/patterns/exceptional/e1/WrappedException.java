package de.team33.patterns.exceptional.e1;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * An unchecked exception dedicated to temporarily wrap checked exceptions.
 *
 * @deprecated Further development is discontinued and this package/module may be removed in a future release.
 * Successor is the module <em>exceptional-dione</em>.
 */
@Deprecated
public class WrappedException extends RuntimeException {

    private static final String MISSING_EXCEPTION =
            "Missing: an exception to be wrapped in a " + WrappedException.class.getSimpleName();

    private final Revision<?> revision;

    private WrappedException(final Throwable cause, final String message) {
        super(message, cause);
        this.revision = Revision.of(cause);
    }

    /**
     * Initializes a new instance with the given message and cause.
     */
    public WrappedException(final String message, final Throwable cause) {
        this(toCause(cause), toMessage(message, cause));
    }

    /**
     * Initializes a new instance with the given cause and its {@link Throwable#getMessage() message}.
     */
    public WrappedException(final Throwable cause) {
        this(toCause(cause), toMessage(null, cause));
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

    public final <X extends Throwable>
    WrappedException reThrowCauseIf(final Predicate<? super Throwable> condition,
                                    final Function<? super Throwable, X> mapping) throws X {
        return revision.throwIf(condition, mapping, this);
    }

    public final <X extends Throwable> WrappedException reThrowCauseAs(final Class<X> xClass) throws X {
        return revision.reThrow(xClass, this);
    }
}
