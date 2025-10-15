package de.team33.patterns.proving.kerberos;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

@SuppressWarnings("BooleanParameter")
public final class Guard<I, E extends Exception> {

    private final Predicate<I> condition;
    private final Function<I, String> toMessage;
    private final Function<String, E> toException;

    private Guard(final Predicate<I> condition,
                  final Function<I, String> toMessage,
                  final Function<String, E> toException) {
        this.condition = condition;
        this.toMessage = toMessage;
        this.toException = toException;
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

    /**
     * Returns a {@link Guard} that can prove that an input of type {@code <I>} meets a given <em>condition</em>
     * or throws an {@link IllegalArgumentException} with a message supplied by <em>toMessage</em>
     * if the input does not satisfy that <em>condition</em>.
     */
    public static <I> Guard<I, IllegalArgumentException> proving(final Predicate<I> condition,
                                                                 final Function<I, String> toMessage) {
        return new Guard<>(condition, toMessage, IllegalArgumentException::new);
    }

    public <K extends I> K proved(final K input) throws E {
        prove(condition.test(input), () -> toMessage.apply(input), toException);
        return input;
    }
}
