package de.team33.patterns.proving.kerberos;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

@SuppressWarnings("BooleanParameter")
public final class Guard<I, E extends Exception> {

    private static final String DEFAULT_MESSAGE = "prove failed";
    private final Predicate<? super I> condition;
    private final Function<? super String, ? extends E> toException;
    private final Function<? super I, String> toMessage;

    private Guard(final Predicate<? super I> condition,
                  final Function<? super String, ? extends E> toException,
                  final Function<? super I, String> toMessage) {
        this.condition = condition;
        this.toMessage = toMessage;
        this.toException = toException;
    }

    /**
     * Proves that the given <em>condition</em> is {@code true}.
     * Otherwise, throws an exception provided by <em>toException</em> with a message supplied by <em>toMessage</em>.
     */
    public static <E extends Exception> void prove(final boolean condition,
                                                   final Function<? super String, E> toException,
                                                   final Supplier<String> toMessage) throws E {
        if (!condition) {
            throw toException.apply(toMessage.get());
        }
    }

    /**
     * Proves that the given <em>condition</em> is {@code true}.
     * Otherwise, throws an {@link IllegalStateException} with a message supplied by <em>toMessage</em>.
     */
    public static void prove(final boolean condition, final Supplier<String> toMessage) {
        prove(condition, IllegalStateException::new, toMessage);
    }

    /**
     * Proves that the given <em>condition</em> is {@code true}.
     * Otherwise, throws an {@link IllegalStateException} with a default message.
     */
    public static void prove(final boolean condition) {
        prove(condition, () -> DEFAULT_MESSAGE);
    }

    /**
     * Returns a {@link Guard} that can prove that an input of type {@code <I>} meets a given <em>condition</em>
     * or throws an {@link IllegalArgumentException} with a message supplied by <em>toMessage</em>
     * if the input does not satisfy that <em>condition</em>.
     */
    public static <I> Guard<I, IllegalArgumentException> proving(final Predicate<? super I> condition,
                                                                 final Function<? super I, String> toMessage) {
        return new Guard<>(condition, IllegalArgumentException::new, toMessage);
    }

    /**
     * Returns a {@link Guard} that can prove that an input of type {@code <I>} meets a given <em>condition</em>
     * or throws an {@link IllegalArgumentException} with a message supplied by <em>toMessage</em>
     * if the input does not satisfy that <em>condition</em>.
     */
    public static <I> Guard<I, IllegalArgumentException> proving(final Predicate<? super I> condition) {
        return new Guard<>(condition, IllegalArgumentException::new, any -> DEFAULT_MESSAGE);
    }

    public <K extends I> K proved(final K input) throws E {
        prove(condition.test(input), toException, () -> toMessage.apply(input));
        return input;
    }

    @SuppressWarnings("ParameterHidesMemberVariable")
    public <X extends Exception> Guard<I, X> thatThrows(final Function<? super String, ? extends X> toException) {
        return new Guard<>(condition, toException, toMessage);
    }
}
