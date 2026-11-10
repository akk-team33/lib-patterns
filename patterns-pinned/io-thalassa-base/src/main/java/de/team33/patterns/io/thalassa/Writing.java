package de.team33.patterns.io.thalassa;

import de.team33.patterns.exceptional.dione.XBiConsumer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;

/**
 * Represents a destination from which new {@link OutputStream} instances can be
 * obtained.
 * <p>
 * Each invocation of {@link #newOutputStream()} is expected to return a new,
 * independent output stream. The provided factory methods create
 * {@code Writing} instances for common output destinations such as files.
 */
@FunctionalInterface
public interface Writing {

    /**
     * Returns a {@code Writing} that opens an output stream for the given file.
     *
     * @param path    the path to write to
     * @param options the options specifying how the file is opened
     * @return a {@code Writing} for the specified file
     */
    static Writing by(final Path path, final OpenOption... options) {
        return () -> {
            Files.createDirectories(path.getParent());
            return Files.newOutputStream(path, options);
        };
    }

    /**
     * Opens and returns a new output stream.
     *
     * @return a newly opened output stream
     * @throws IOException if the output stream cannot be opened
     */
    OutputStream newOutputStream() throws IOException;

    /**
     * Returns an {@link Output} that applies the given function to a newly
     * opened output stream.
     * <p>
     * The output stream is automatically closed after the function has been
     * applied, regardless of whether it completes normally or exceptionally.
     *
     * @param method the function to process the output stream
     * @param <T>    the type of values to write
     * @return an {@code Output} using the given processing function
     */
    default <T> Output<T> output(final XBiConsumer<? super OutputStream, ? super T, ? extends IOException> method) {
        return origin -> {
            try (final OutputStream out = newOutputStream()) {
                method.accept(out, origin);
            }
        };
    }

    /**
     * Returns an {@link Output} that applies the given function to a buffered
     * character writer using the specified character set.
     * <p>
     * The underlying output stream and writer are automatically closed after
     * the function has been applied.
     *
     * @param charset the character set used to encode the output
     * @param method  the function to process the buffered writer
     * @param <T>     the type of values to write
     * @return an {@code Output} using the given processing function
     */
    default <T> Output<T> output(final Charset charset,
                                 final XBiConsumer<? super BufferedWriter, ? super T, ? extends IOException> method) {
        return output(Util.writeMethod(method, charset));
    }
}
