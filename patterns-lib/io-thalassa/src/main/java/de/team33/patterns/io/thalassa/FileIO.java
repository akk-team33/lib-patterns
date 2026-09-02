package de.team33.patterns.io.thalassa;

import de.team33.patterns.exceptional.dione.XBiConsumer;
import de.team33.patterns.exceptional.dione.XFunction;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Path;

/**
 * Combines {@link Input} and {@link Output} for reading from and writing to the same file.
 * <p>
 * The conversion between the file contents and values of type {@code <T>} is supplied by the caller.
 * Each invocation of {@link #read()} or {@link #write(Object)} opens a new stream, processes the file,
 * and closes the stream afterward.
 * <p>
 * Implementation sample:
 * <pre>
 * public class SampleIO extends FileIO&lt;Sample&gt; {
 *
 *     public SampleIO(final Path path, final Charset charset) {
 *         super(path, charset, SampleIO::readSample, SampleIO::writeSample);
 *     }
 *
 *     private static Sample readSample(final BufferedReader reader) throws IOException {
 *         try (final StringWriter writer = new StringWriter()) {
 *             reader.transferTo(writer);
 *             return Sample.parse(writer.toString());
 *         }
 *     }
 *
 *     private static void writeSample(final Writer writer, final Sample sample) throws IOException {
 *         writer.write(sample.toString());
 *     }
 * }
 * </pre>
 *
 * @param <T> the type of values to read and write
 */
public class FileIO<T> implements IO<T> {

    private final Input<T> input;
    private final Output<T> output;

    /**
     * Creates a new {@code FileIO} using binary streams.
     *
     * @param path        the file to read from and write to
     * @param readMethod  the function used to read a value from an input stream
     * @param writeMethod the function used to write a value to an output stream
     */
    public FileIO(final Path path,
                  final XFunction<? super InputStream, ? extends T, ? extends IOException> readMethod,
                  final XBiConsumer<? super OutputStream, ? super T, ? extends IOException> writeMethod) {
        this.input = Reading.by(path).input(readMethod);
        this.output = Writing.by(path).output(writeMethod);
    }

    /**
     * Creates a new {@code FileIO} using buffered character streams with the
     * specified character set.
     *
     * @param path        the file to read from and write to
     * @param charset     the character set used to decode and encode the file
     * @param readMethod  the function used to read a value from a buffered reader
     * @param writeMethod the function used to write a value to a buffered writer
     */
    public FileIO(final Path path, final Charset charset,
                  final XFunction<? super BufferedReader, ? extends T, ? extends IOException> readMethod,
                  final XBiConsumer<? super BufferedWriter, ? super T, ? extends IOException> writeMethod) {
        this.input = Reading.by(path).input(charset, readMethod);
        this.output = Writing.by(path).output(charset, writeMethod);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final T read() throws IOException {
        return input.read();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void write(final T value) throws IOException {
        output.write(value);
    }
}
