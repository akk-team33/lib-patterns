package de.team33.patterns.io.thalassa;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Properties;

/**
 * A {@link FileIO} implementation for reading and writing {@link Properties} files.
 * <p>
 * The complete set of properties is held in memory when reading.
 * Writing replaces the current file content with the supplied properties.
 * The written file contains a generated comment identifying the creation time and this class as the source.
 */
public final class PropertiesIO extends FileIO<Properties> {

    private PropertiesIO(final Path path, final Charset charset) {
        super(path, charset, PropertiesIO::readProps, PropertiesIO::writeProps);
    }

    private static Properties readProps(final BufferedReader reader) throws IOException {
        final Properties properties = new Properties();
        properties.load(reader);
        return properties;
    }

    private static void writeProps(final BufferedWriter writer, final Properties properties) throws IOException {
        properties.store(writer, "%s - by %s".formatted(LocalDateTime.now().toString(),
                                                        PropertiesIO.class.getCanonicalName()));
    }

    /**
     * Creates a new {@code PropertiesIO} for the given file using the specified character set.
     *
     * @param path    the file to read from and write to
     * @param charset the character set used for reading and writing
     * @return a {@code PropertiesIO} for the specified file
     */
    public static PropertiesIO by(final Path path, final Charset charset) {
        return new PropertiesIO(path, charset);
    }

    /**
     * Creates a new {@code PropertiesIO} for the given file using UTF-8.
     *
     * @param path the file to read from and write to
     * @return a {@code PropertiesIO} for the specified file
     */
    public static PropertiesIO by(final Path path) {
        return by(path, StandardCharsets.UTF_8);
    }

    /**
     * Creates an {@link Input} that reads the specified classpath resource as a {@link Properties} instance.
     * <p>
     * The resulting input only supports reading.
     * The underlying input stream is automatically closed after reading.
     *
     * @param refClass     the class used to resolve the resource
     * @param resourceName the resource name
     * @param charset      the character set used for reading
     * @return an {@code Input} producing the loaded properties
     */
    public static Input<Properties> by(final Class<?> refClass, final String resourceName, final Charset charset) {
        return Reading.by(refClass, resourceName)
                      .input(charset, PropertiesIO::readProps);
    }

    /**
     * Creates an {@link Input} that reads the specified classpath resource as a {@link Properties} instance,
     * using UTF-8 decoding.
     * <p>
     * The resulting input only supports reading. The underlying input stream
     * is automatically closed after reading.
     *
     * @param refClass     the class used to resolve the resource
     * @param resourceName the resource name
     * @return an {@code Input} producing the loaded properties
     */
    public static Input<Properties> by(final Class<?> refClass, final String resourceName) {
        return by(refClass, resourceName, StandardCharsets.UTF_8);
    }

    /**
     * Convenience method to read the full content of a classpath resource specified by the given <em>refClass</em>
     * and <em>resourceName</em> as {@link Properties}, using UTF-8 decoding.
     *
     * @throws UncheckedIOException if an I/O error occurs while reading
     * @see #by(Class, String)
     * @see #readUnchecked()
     */
    public static Properties read(final Class<?> refClass, final String resourceName) {
        return by(refClass, resourceName).readUnchecked();
    }

    /**
     * Convenience method to read the full content of a file specified by the given <em>path</em>
     * as {@link Properties}, using UTF-8 decoding.
     *
     * @throws UncheckedIOException if an I/O error occurs while reading
     * @see #by(Path)
     * @see #readUnchecked()
     */
    public static Properties read(final Path path) {
        return by(path).readUnchecked();
    }

    /**
     * Convenience method to write the given <em>properties</em> to a file specified by the given <em>path</em>,
     * using UTF-8 encoding.
     *
     * @throws UncheckedIOException if an I/O error occurs while writing
     * @see #by(Path)
     * @see #writeUnchecked(Object)
     */
    public static void write(final Properties properties, final Path path) {
        by(path).writeUnchecked(properties);
    }
}
