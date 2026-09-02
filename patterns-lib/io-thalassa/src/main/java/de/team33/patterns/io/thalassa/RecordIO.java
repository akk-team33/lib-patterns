package de.team33.patterns.io.thalassa;

import de.team33.patterns.records.triton.Triton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

/**
 * A {@link FileIO} implementation for reading and writing Java {@link Record records} in JSON format.
 * <p>
 * Instances of this class map between JSON documents and record instances using {@link Triton}.
 * <p>
 * Unless specified otherwise, UTF-8 is used as character set.
 *
 * @param <T> the record type to be read and written.
 */
public class RecordIO<T extends Record> extends FileIO<T> {

    private RecordIO(final Class<T> recordClass, final Path path, final Charset charset) {
        super(path, charset, reader -> readRecord(recordClass, reader), RecordIO::writeString);
    }

    private static <T extends Record> T readRecord(final Class<T> recordClass,
                                                   final BufferedReader reader) throws IOException {
        return Triton.toRecord(recordClass, TextIO.readString(reader));
    }

    private static <T extends Record> void writeString(final Writer writer,
                                                       final T value) throws IOException {
        TextIO.writeString(writer, Triton.toJson(value));
    }

    /**
     * Creates a new {@code RecordIO} for the given file using the specified character set.
     *
     * @param recordClass The record type.
     * @param path        The target file.
     * @param charset     The character set used for reading and writing.
     * @return A {@code RecordIO} instance.
     */
    public static <T extends Record> RecordIO<T> by(final Class<T> recordClass,
                                                    final Path path,
                                                    final Charset charset) {
        return new RecordIO<>(recordClass, path, charset);
    }

    /**
     * Creates a new {@code RecordIO} for the given file using UTF-8.
     *
     * @param recordClass The record type.
     * @param path        The target file.
     * @return A {@code RecordIO} instance.
     */
    public static <T extends Record> RecordIO<T> by(final Class<T> recordClass,
                                                    final Path path) {
        return by(recordClass, path, StandardCharsets.UTF_8);
    }

    /**
     * Creates an {@link Input} that reads JSON encoded record instances from the specified classpath resource.
     *
     * @param recordClass  The record type.
     * @param refClass     The reference class used to resolve the resource.
     * @param resourceName The resource name.
     * @param charset      The character set used to read the resource.
     * @return A corresponding {@link Input}.
     */
    public static <T extends Record> Input<T> by(final Class<T> recordClass,
                                                 final Class<?> refClass,
                                                 final String resourceName,
                                                 final Charset charset) {
        return Reading.by(refClass, resourceName)
                      .input(charset, reader -> readRecord(recordClass, reader));
    }

    /**
     * Creates an {@link Input} that reads JSON encoded record instances from the specified classpath resource
     * using UTF-8.
     *
     * @param recordClass  The record type.
     * @param refClass     The reference class used to resolve the resource.
     * @param resourceName The resource name.
     * @return A corresponding {@link Input}.
     */
    public static <T extends Record> Input<T> by(final Class<T> recordClass,
                                                 final Class<?> refClass,
                                                 final String resourceName) {
        return by(recordClass, refClass, resourceName, StandardCharsets.UTF_8);
    }

    /**
     * Convenience method to read the full content of a JSON classpath resource specified by the given
     * <em>refClass</em> and <em>resourceName</em> as a {@code record} of type <em>recordClass</em>,
     * using UTF-8 decoding.
     *
     * @throws UncheckedIOException if an I/O error occurs while reading
     * @see #by(Class, Class, String)
     * @see #readUnchecked()
     */
    public static <T extends Record> T read(final Class<T> recordClass,
                                            final Class<?> refClass,
                                            final String resourceName) {
        return by(recordClass, refClass, resourceName).readUnchecked();
    }

    /**
     * Convenience method to read the full content of a JSON file specified by the given <em>path</em>
     * as a {@code record} of type <em>recordClass</em>, using UTF-8 decoding.
     *
     * @throws UncheckedIOException if an I/O error occurs while reading
     * @see #by(Class, Path)
     * @see #readUnchecked()
     */
    public static <T extends Record> T read(final Class<T> recordClass, final Path path) {
        return by(recordClass, path).readUnchecked();
    }

    /**
     * Convenience method to write the given <em>record</em> to a JSON file specified by the given <em>path</em>,
     * using UTF-8 encoding.
     *
     * @throws UncheckedIOException if an I/O error occurs while writing
     * @see #by(Class, Path)
     * @see #writeUnchecked(Object)
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <T extends Record> void write(final T record, final Path path) {
        by((Class) record.getClass(), path).writeUnchecked(record);
    }
}