package de.team33.patterns.exceptional.e1;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A utility class that provides wrapping methods for exceptions.
 */
public final class Wrapping {

    private Wrapping() {
    }

    /**
     * Results in a wrapping method such as is required in some places in this library to wrap an exception in another
     * exception.
     * <p>
     * This variant is mainly intended to use a typical constructor of an exception, which requires two parameters,
     * namely the message text and the causing exception <em>(see below)</em>. The message text is taken from the
     * causing exception so that the resulting method only needs the causing exception as a parameter.
     *
     * @see RuntimeException#RuntimeException(String, Throwable)
     */
    public static <X1 extends Throwable, X2 extends Throwable>
    Function<X1, X2> method(final BiFunction<String, X1, X2> biFunction) {
        return x1 -> biFunction.apply(x1.getMessage(), x1);
    }

    /**
     * Results in a wrapping method such as is required in some places in this library to wrap an exception in another
     * exception.
     * <p>
     * This variant is intended to use a predefined message text and a typical constructor of an exception, which
     * requires two parameters, namely the message text and the causing exception <em>(see below)</em> so that the
     * resulting method only needs the causing exception as a parameter.
     *
     * @see RuntimeException#RuntimeException(String, Throwable)
     */
    public static <X1 extends Throwable, X2 extends Throwable>
    Function<X1, X2> method(final String message, final BiFunction<String, X1, X2> biFunction) {
        return x1 -> biFunction.apply(message, x1);
    }

    /**
     * Results in a wrapping method such as is required in some places in this library to wrap an exception in another
     * exception.
     * <p>
     * This variant is mainly intended to use a typical constructor of an exception, which requires a single parameter,
     * namely the message text <em>(see below)</em>. The causing exception is set just after the construction of the
     * wrapping exception. The message text is taken from the causing exception so that the resulting method only needs
     * the causing exception as a parameter.
     *
     * @see RuntimeException#RuntimeException(String)
     */
    public static <X1 extends Throwable, X2 extends Throwable>
    Function<X1, X2> varying(final Function<String, X2> function) {
        return x1 -> apply(x1, x1.getMessage(), function);
    }

    /**
     * Results in a wrapping method such as is required in some places in this library to wrap an exception in another
     * exception.
     * <p>
     * This variant is intended to use a predefined message text and a typical constructor of an exception, which
     * requires a single parameter, namely the message text <em>(see below)</em>. The causing exception is set just
     * after the construction of the wrapping exception so that the resulting method only needs the causing exception
     * as a parameter.
     *
     * @see RuntimeException#RuntimeException(String)
     */
    public static <X1 extends Throwable, X2 extends Throwable>
    Function<X1, X2> varying(final String message, final Function<String, X2> function) {
        return x1 -> apply(x1, message, function);
    }

    private static <X1 extends Throwable, X2 extends Throwable>
    X2 apply(final X1 x1, final String message, final Function<String, X2> function) {
        final X2 x2 = function.apply(message);
        x2.initCause(x1);
        return x2;
    }
}
