package de.team33.patterns.proving.kerberos;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

@SuppressWarnings("BooleanParameter")
public final class Guard {

    private static final String DEFAULT_MESSAGE = "prove failed";

    private Guard() {
    }

    /**
     * Proves that the given <em>condition</em> is {@code true}.
     * Otherwise, throws an {@link IllegalStateException} with a default message.
     */
    public static void prove(final boolean condition) {
        prove(condition, () -> DEFAULT_MESSAGE);
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
     * Otherwise, throws an exception provided by <em>toException</em> with a message supplied by <em>toMessage</em>.
     */
    public static <E extends Exception> void prove(final boolean condition,
                                                   final Supplier<String> toMessage,
                                                   final Function<? super String, E> toException) throws E {
        if (!condition) {
            throw toException.apply(toMessage.get());
        }
    }

    /**
     * Proves that a given <em>candidate</em> meets the given <em>condition</em>.
     * Otherwise, throws an {@link IllegalArgumentException} with a message supplied by <em>toMessage</em>.
     *
     * @param <T> The type of candidate.
     * @return The <em>candidate</em> if it meets the <em>condition</em>.
     * @throws IllegalArgumentException if the <em>candidate</em> does not meet the <em>condition</em>.
     */
    public static <T> T proved(final T candidate,
                               final Predicate<? super T> condition,
                               final Function<? super T, String> toMessage) {
        return proved(candidate, condition, toMessage, IllegalArgumentException::new);
    }

    /**
     * Proves that a given <em>candidate</em> meets the given <em>condition</em>.
     * Otherwise, throws an exception provided by <em>toException</em> with a message supplied by <em>toMessage</em>.
     *
     * @param <T> The type of candidate.
     * @param <E> The type of exception.
     * @return The <em>candidate</em> if it meets the <em>condition</em>.
     * @throws E if the <em>candidate</em> does not meet the <em>condition</em>.
     */
    public static <T, E extends Exception> T proved(final T candidate,
                                                    final Predicate<? super T> condition,
                                                    final Function<? super T, String> toMessage,
                                                    final Function<? super String, E> toException) throws E {
        prove(condition.test(candidate), () -> toMessage.apply(candidate), toException);
        return candidate;
    }
}
