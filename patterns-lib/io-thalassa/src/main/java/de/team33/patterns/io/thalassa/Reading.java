package de.team33.patterns.io.thalassa;

import de.team33.patterns.exceptional.dione.XFunction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;

/**
 * Represents a source from which new {@link InputStream} instances can be
 * obtained.
 * <p>
 * Each invocation of {@link #newInputStream()} is expected to return a new,
 * independent input stream. The provided factory methods create
 * {@code Reading} instances for common data sources such as files and classpath
 * resources.
 */
@FunctionalInterface
public interface Reading {

    /**
     * Returns a {@code Reading} that opens an input stream for the given file.
     *
     * @param path    the path to read from
     * @param options the options specifying how the file is opened
     * @return a {@code Reading} for the specified file
     */
    static Reading by(final Path path, final OpenOption... options) {
        return () -> Files.newInputStream(path, options);
    }

    /**
     * Returns a {@code Reading} that opens an input stream for the specified classpath resource.
     * <p>
     * The input stream returned by {@link #newInputStream()} may be {@code null}
     * if the requested resource cannot be found.
     *
     * @param refClass     the class used to resolve the resource
     * @param resourceName the resource name
     * @return a {@code Reading} for the specified resource
     */
    static Reading by(final Class<?> refClass, final String resourceName) {
        return () -> refClass.getResourceAsStream(resourceName);
    }

    /**
     * Opens and returns a new input stream.
     *
     * @return a newly opened input stream
     * @throws IOException if the input stream cannot be opened
     */
    InputStream newInputStream() throws IOException;

    /**
     * Returns an {@link Input} that applies the given function to a newly
     * opened input stream.
     * <p>
     * The input stream is automatically closed after the function has been
     * applied, regardless of whether it completes normally or exceptionally.
     *
     * @param method the function to process the input stream
     * @param <T>    the type of the computed result
     * @return an {@code Input} using the given processing function
     */
    default <T> Input<T> input(final XFunction<? super InputStream, ? extends T, ? extends IOException> method) {
        return () -> {
            try (final InputStream in = newInputStream()) {
                return method.apply(in);
            }
        };
    }

    /**
     * Returns an {@link Input} that applies the given function to a buffered
     * character reader using the specified character set.
     * <p>
     * The underlying input stream and reader are automatically closed after
     * the function has been applied.
     *
     * @param charset the character set used to decode the input
     * @param method  the function to process the buffered reader
     * @param <T>     the type of the computed result
     * @return an {@code Input} using the given processing function
     */
    default <T> Input<T> input(final Charset charset,
                               final XFunction<? super BufferedReader, ? extends T, ? extends IOException> method) {
        return input(Util.readMethod(method, charset));
    }
}
