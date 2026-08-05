package de.team33.patterns.io.thalassa;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

/**
 * A {@link FileIO} implementation for reading and writing complete file contents as strings.
 * <p>
 * The complete text content of the underlying file is held in memory when reading.
 * Writing replaces the current file content with the supplied text.
 * The character encoding is defined by the configured {@link Charset}.
 */
public class TextIO extends FileIO<String> {

    private TextIO(final Path path, final Charset charset) {
        super(path, charset, TextIO::readString, TextIO::writeString);
    }

    /**
     * Reads the complete content from the given reader as a string.
     *
     * @param reader the reader to read from
     * @return the complete text content of the reader
     * @throws IOException if an I/O error occurs while reading
     */
    static String readString(final BufferedReader reader) throws IOException {
        try (final StringWriter writer = new StringWriter()) {
            reader.transferTo(writer);
            return writer.toString();
        }
    }

    /**
     * Writes the given text to the specified writer.
     *
     * @param writer the writer to write to
     * @param text   the text to write
     * @throws IOException if an I/O error occurs while writing
     */
    static void writeString(final Writer writer, final String text) throws IOException {
        writer.write(text);
    }

    /**
     * Creates a new {@code TextIO} for the given file using the specified character encoding.
     *
     * @param path    the file to read from and write to
     * @param charset the character encoding used for reading and writing
     * @return a {@code TextIO} for the specified file
     */
    public static TextIO by(final Path path, final Charset charset) {
        return new TextIO(path, charset);
    }

    /**
     * Creates a new {@code TextIO} for the given file using UTF-8 encoding.
     *
     * @param path the file to read from and write to
     * @return a {@code TextIO} for the specified file
     */
    public static TextIO by(final Path path) {
        return by(path, StandardCharsets.UTF_8);
    }

    /**
     * Creates an {@link Input} that reads the complete content of the specified classpath resource as a string.
     * <p>
     * The resulting input only supports reading.
     * The underlying input stream is automatically closed after reading.
     *
     * @param refClass     the class used to resolve the resource
     * @param resourceName the resource name
     * @param charset      the character encoding used for reading
     * @return an {@code Input} producing the resource content as a string
     */
    public static Input<String> by(final Class<?> refClass, final String resourceName, final Charset charset) {
        return Reading.by(refClass, resourceName)
                      .input(charset, TextIO::readString);
    }

    /**
     * Creates an {@link Input} that reads the complete content of the specified classpath resource as a string,
     * using UTF-8 decoding.
     * <p>
     * The resulting input only supports reading. The underlying input stream
     * is automatically closed after reading.
     *
     * @param refClass     the class used to resolve the resource
     * @param resourceName the resource name
     * @return an {@code Input} producing the resource content as a string
     */
    public static Input<String> by(final Class<?> refClass, final String resourceName) {
        return by(refClass, resourceName, StandardCharsets.UTF_8);
    }

    /**
     * Convenience method to read the full content of a classpath resource specified by the given <em>refClass</em>
     * and <em>resourceName</em> as a string, using UTF-8 decoding.
     *
     * @throws UncheckedIOException if an I/O error occurs while reading
     * @see #by(Class, String)
     * @see #readUnchecked()
     */
    public static String read(final Class<?> refClass, final String resourceName) {
        return by(refClass, resourceName).readUnchecked();
    }

    /**
     * Convenience method to read the full content of a file specified by the given <em>path</em> as a string,
     * using UTF-8 decoding.
     *
     * @throws UncheckedIOException if an I/O error occurs while reading
     * @see #by(Path)
     * @see #readUnchecked()
     */
    public static String read(final Path path) {
        return by(path).readUnchecked();
    }

    /**
     * Convenience method to write the given <em>text</em> to a file specified by the given <em>path</em>,
     * using UTF-8 encoding.
     *
     * @throws UncheckedIOException if an I/O error occurs while writing
     * @see #by(Path)
     * @see #writeUnchecked(Object) ()
     */
    public static void write(final String text, final Path path) {
        by(path).writeUnchecked(text);
    }
}