package de.team33.patterns.exceptional.dione;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * An unchecked exception dedicated to (temporarily) wrap checked exceptions.
 */
@SuppressWarnings("UnusedReturnValue")
public class WrappedException extends RuntimeException {

    private static final String MISSING_EXCEPTION = "Missing: an exception to be wrapped in a " + WrappedException.class.getSimpleName();

    private WrappedException(final Throwable cause, final String message) {
        super(message, cause);
    }

    /**
     * Initializes a new instance with the given message and cause.
     */
    public WrappedException(final String message, final Throwable cause) {
        this(toCause(cause), toMessage(message, cause));
    }

    private Revision<?> revision() {
        return Revision.of(getCause());
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
                message);
        return result.stream()
                     .filter(Objects::nonNull)
                     .collect(Collectors.joining(" "));
    }

    private static String toMessage(final Throwable cause) {
        return Optional.ofNullable(cause.getMessage()).orElseGet(() -> cause.getClass().getCanonicalName());
    }

    private static Throwable toCause(final Throwable cause) {
        return (null != cause) ? cause : new IllegalStateException(MISSING_EXCEPTION);
    }

    /**
     * Applies a given {@link Function mapping} to the {@linkplain #getCause() cause of this exception} if the given
     * {@link Predicate condition} applies and throws the result, otherwise this exception will be returned.
     *
     * @param condition A {@link Predicate} that is used to check the {@linkplain #getCause() cause of this exception}
     *                  for applicability.
     * @param mapping   A {@link Function} that converts the {@linkplain #getCause() cause of this exception} to a
     *                  specific type of exception to be thrown at that point.
     * @param <X>       The exception type that is intended as a result of the given mapping and that is thrown by this
     *                  method, if applicable.
     * @return this exception.
     * @throws X The mapped exception, if present.
     */
    public final <X extends Throwable>
    WrappedException reThrowCauseIf(final Predicate<? super Throwable> condition,
                                    final Function<? super Throwable, X> mapping) throws X {
        return revision().throwIf(condition, mapping, this);
    }

    /**
     * Rethrows the {@linkplain #getCause() cause of this exception} if it matches the given exception type,
     * otherwise this exception will be returned.
     *
     * @param xClass The {@link Class} that represents the type of exception that is expected.
     * @param <X>    The type of exception that is expected and, if applicable, thrown by this method.
     * @return this exception.
     * @throws X the {@linkplain #getCause() cause of this exception}, cast to the given type, if applicable.
     */
    public final <X extends Throwable> WrappedException reThrowCauseAs(final Class<X> xClass) throws X {
        return revision().reThrow(xClass, this);
    }
}
