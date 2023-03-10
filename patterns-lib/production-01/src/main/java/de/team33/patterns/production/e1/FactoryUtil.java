package de.team33.patterns.production.e1;

import java.util.function.Consumer;
import java.util.logging.Logger;

import static java.lang.String.format;

/**
 * Constants and methods for dealing with FactoryHubs.
 */
public class FactoryUtil {

    private static final Logger LOG = Logger.getLogger(FactoryHub.class.getCanonicalName());
    private static final String UNKNOWN_TOKEN =
            "unknown token:%n" +
            "- type of token   : %s%n" +
            "- value* of token : %s%n" +
            "  *(string representation)%n";

    /**
     * A {@link Consumer} to be used with {@link FactoryHub.Collector#setUnknownTokenListener(Consumer)}
     * that does nothing.
     */
    public static final Consumer<Object> ACCEPT_UNKNOWN_TOKEN = token -> {
    };

    /**
     * A {@link Consumer} to be used with {@link FactoryHub.Collector#setUnknownTokenListener(Consumer)}
     * that logs the event via {@linkplain Logger java logging}.
     */
    public static final Consumer<Object> LOG_UNKNOWN_TOKEN = token -> {
        LOG.info(() -> unknownTokenMessage(token));
    };

    /**
     * A {@link Consumer} to be used with {@link FactoryHub.Collector#setUnknownTokenListener(Consumer)}
     * that throws an {@link IllegalArgumentException}.
     */
    public static final Consumer<Object> DENY_UNKNOWN_TOKEN = token -> {
        throw new IllegalArgumentException(unknownTokenMessage(token));
    };

    /**
     * Returns an <em>unknown token message</em> for a given token.
     */
    public static String unknownTokenMessage(final Object token) {
        return format(UNKNOWN_TOKEN, classOf(token), token);
    }

    @SuppressWarnings("ReturnOfNull")
    private static Class<?> classOf(final Object subject) {
        return (subject == null) ? null : subject.getClass();
    }
}
