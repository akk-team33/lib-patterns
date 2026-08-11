package de.team33.patterns.io.deimos;

import de.team33.patterns.exceptional.dione.XFunction;
import de.team33.patterns.exceptional.dione.XSupplier;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @deprecated consider module
 * <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/io-thalassa/apidocs/">io-thalassa</a>
 * as a replacement.
 *
 * @see <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/io-thalassa/">io-thalassa</a>
 * @see <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/io-thalassa/apidocs/">io-thalassa/apidocs</a>
 */
@Deprecated
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

    private final Charset charset;
    private final XSupplier<InputStream, IOException> newInputStream;
    private final Function<Exception, String> newExceptionMessage;

    @SuppressWarnings("WeakerAccess")
    protected Resource(final Charset charset,
                       final XSupplier<InputStream, IOException> newInputStream,
                       final Function<Exception, String> newExceptionMessage) {
        this.charset = charset;
        this.newInputStream = newInputStream;
        this.newExceptionMessage = newExceptionMessage;
    }

    /**
     * @deprecated see {@link Resource}
     */
    @Deprecated
    public static Resource by(final Class<?> referringClass, final String resourceName) {
        return new Resource(StandardCharsets.UTF_8,
                            () -> referringClass.getResourceAsStream(resourceName),
                            caught -> String.format(CANNOT_READ_RESOURCE, resourceName, referringClass,
                                                    caught.getClass().getCanonicalName(), caught.getMessage()));
    }

    /**
     * @deprecated see {@link Resource}
     */
    @Deprecated
    public static Resource by(final Path path) {
        return new Resource(StandardCharsets.UTF_8,
                            () -> Files.newInputStream(path),
                            caught -> String.format(CANNOT_READ_FILE, path,
                                                    caught.getClass().getCanonicalName(), caught.getMessage()));
    }

    /**
     * @deprecated see {@link Resource}
     */
    @Deprecated
    public final Resource using(final Charset charset) {
        return new Resource(charset, newInputStream, newExceptionMessage);
    }

    private static String readText(final BufferedReader reader) {
        return reader.lines().collect(Collectors.joining(NEW_LINE));
    }

    private static Properties readProperties(final Reader in) throws IOException {
        final Properties result = new Properties();
        result.load(in);
        return result;
    }

    /**
     * @deprecated see {@link Resource}
     */
    @Deprecated
    public final <R> R readByteStream(final XFunction<? super InputStream, R, ? extends IOException> function) {
        try (final InputStream in = newInputStream.get()) {
            return function.apply(in);
        } catch (final RuntimeException | IOException e) {
            throw new IllegalArgumentException(newExceptionMessage.apply(e), e);
        }
    }

    private <R>
    R readCharStream(final InputStream stream,
                     final XFunction<? super BufferedReader, R, ? extends IOException> function) throws IOException {
        try (final Reader reader = new InputStreamReader(stream, charset);
             final BufferedReader bufferedReader = new BufferedReader(reader)) {
            return function.apply(bufferedReader);
        }
    }

    /**
     * @deprecated see {@link Resource}
     */
    @Deprecated
    public final <R> R readCharStream(final XFunction<? super BufferedReader, R, ? extends IOException> function) {
        return readByteStream(in -> readCharStream(in, function));
    }

    /**
     * @deprecated see {@link Resource}
     */
    @Deprecated
    public final String readText() {
        return readCharStream(Resource::readText);
    }

    /**
     * @deprecated see {@link Resource}
     */
    @Deprecated
    public final Properties readProperties() {
        return readCharStream(Resource::readProperties);
    }
}
