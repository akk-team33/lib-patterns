package de.team33.patterns.io.deimos;

import de.team33.patterns.exceptional.dione.XFunction;
import de.team33.patterns.exceptional.dione.XSupplier;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Abstracts a resource that can be read via a byte stream.
 */
public class Resource {

    private static final String CANNOT_READ_RESOURCE = "cannot read resource%n" +
                                                       "    resource name   : %s%n" +
                                                       "    referring class : %s%n" +
                                                       "    cause type      : %s%n" +
                                                       "    cause message   : %s%n";
    private static final String CANNOT_READ_FILE = "cannot read file%n" +
                                                   "    path          : %s%n" +
                                                   "    cause type    : %s%n" +
                                                   "    cause message : %s%n";
    private static final String NEW_LINE = String.format("%n");

    private final XSupplier<InputStream, IOException> newInputStream;
    private final Function<Exception, String> newExceptionMessage;

    protected Resource(final XSupplier<InputStream, IOException> newInputStream,
                       final Function<Exception, String> newExceptionMessage) {
        this.newInputStream = newInputStream;
        this.newExceptionMessage = newExceptionMessage;
    }

    /**
     * Retrieves a {@link Resource} to read a java resource.
     */
    public static Resource by(final Class<?> referringClass, final String resourceName) {
        return new Resource(() -> referringClass.getResourceAsStream(resourceName),
                            caught -> String.format(CANNOT_READ_RESOURCE, resourceName, referringClass,
                                                    caught.getClass().getCanonicalName(), caught.getMessage()));
    }

    /**
     * Retrieves a {@link Resource} to read a file.
     */
    public static Resource by(final Path path) {
        return new Resource(() -> Files.newInputStream(path),
                            caught -> String.format(CANNOT_READ_FILE, path,
                                                    caught.getClass().getCanonicalName(), caught.getMessage()));
    }

    private static <R> R readCharStream(final InputStream in,
                                        final XFunction<BufferedReader, R, IOException> function) throws IOException {
        try (final Reader streamReader = new InputStreamReader(in, StandardCharsets.UTF_8);
             final BufferedReader bufferedReader = new BufferedReader(streamReader)) {
            return function.apply(bufferedReader);
        }
    }

    private static String readText(BufferedReader reader) {
        return reader.lines().collect(Collectors.joining(NEW_LINE));
    }

    private static Properties readProperties(final Reader in) throws IOException {
        final Properties result = new Properties();
        result.load(in);
        return result;
    }

    public final <R> R readByteStream(final XFunction<InputStream, R, IOException> function) {
        try (final InputStream in = newInputStream.get()) {
            return function.apply(in);
        } catch (final RuntimeException | IOException e) {
            throw new IllegalArgumentException(newExceptionMessage.apply(e), e);
        }
    }

    public final <R> R readCharStream(final XFunction<BufferedReader, R, IOException> function) {
        return readByteStream(in -> readCharStream(in, function));
    }

    public final String readText() {
        return readCharStream(Resource::readText);
    }

    public final Properties readProperties() {
        return readCharStream(Resource::readProperties);
    }
}
