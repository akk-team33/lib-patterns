package de.team33.patterns.testing.titan.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

/**
 * Utility to redirect standard output to a file in test scenarios.
 */
@SuppressWarnings("UseOfSystemOutOrSystemErr")
public final class Redirected {

    private static final String UTF_8 = StandardCharsets.UTF_8.name();

    private Redirected() {
    }

    public static <X extends Exception> String outputOf(final Command<X> command) throws IOException, X {
        final PrintStream oldOut = System.out;
        try (final ByteArrayOutputStream stream = new ByteArrayOutputStream();
             final PrintStream newOut = new PrintStream(stream, false, UTF_8)) {
            System.setOut(newOut);
            command.run();
            newOut.flush();
            return stream.toString(UTF_8);
        } finally {
            System.setOut(oldOut);
        }
    }

    /**
     * A command that allows to throw a checked exception.
     */
    @FunctionalInterface
    public interface Command<X extends Exception> {

        /**
         * Performs this command.
         */
        void run() throws X;
    }
}
