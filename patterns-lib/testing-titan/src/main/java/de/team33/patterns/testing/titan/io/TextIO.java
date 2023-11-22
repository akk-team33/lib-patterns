package de.team33.patterns.testing.titan.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

/**
 * @deprecated use class <em>TextIO</em> from module <em>io-deimos</em> instead.
 * This class may be removed in a future release.
 */
@Deprecated
public final class TextIO {

    private static final String CANNOT_READ_RESOURCE = "cannot read resource%n" +
                                                       "    resource name   : %s%n" +
                                                       "    referring class : %s%n";
    private static final String CANNOT_READ_FILE = "cannot read file%n" +
                                                   "    path : %s%n";
    private static final String NEW_LINE = String.format("%n");

    private TextIO() {
    }

    public static String read(final Class<?> refClass, final String rsrcName) {
        try (final InputStream in = refClass.getResourceAsStream(rsrcName)) {
            return read(in);
        } catch (final NullPointerException | IOException e) {
            throw new IllegalArgumentException(String.format(CANNOT_READ_RESOURCE, rsrcName, refClass), e);
        }
    }

    public static String read(final Path path) {
        try (final InputStream in = Files.newInputStream(path)) {
            return read(in);
        } catch (final IOException e) {
            throw new IllegalArgumentException(String.format(CANNOT_READ_FILE, path), e);
        }
    }

    public static String read(final InputStream in) throws IOException {
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining(NEW_LINE));
        }
    }
}
