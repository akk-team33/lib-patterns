package de.team33.patterns.proving.kerberos;

import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("BooleanParameter")
public final class Guard {

    private Guard() {
    }

    /**
     * Proves that the given <em>condition</em> is {@code true}.
     * Otherwise, throws an exception provided by <em>toException</em> with a message supplied by <em>toMessage</em>.
     */
    public static <E extends Exception> void prove(final boolean condition, final Supplier<String> toMessage,
                                                   final Function<? super String, E> toException) throws E {
        if (!condition) {
            throw toException.apply(toMessage.get());
        }
    }

    /**
     * Proves that the given <em>condition</em> is {@code true}.
     * Otherwise, throws an {@link IllegalStateException} with a message supplied by <em>toMessage</em>.
     */
    public static void prove(final boolean condition, final Supplier<String> toMessage) {
        prove(condition, toMessage, IllegalStateException::new);
    }

    /**
     * Proves that the given <em>condition</em> is {@code true}.
     * Otherwise, throws an {@link IllegalStateException} with a default message.
     */
    public static void prove(final boolean condition) {
        prove(condition, () -> "prove failed");
    }
}
