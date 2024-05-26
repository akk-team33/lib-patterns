package de.team33.patterns.arbitrary.mimas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

final class Util {

    static final int DOUBLE_RESOLUTION = Double.SIZE - 11;
    static final int FLOAT_RESOLUTION = Float.SIZE - 8;

    private static final String NEWLINE = String.format("%n");
    private static final String NO_RESOURCE = "Should not happen:" +
            " <%s> is not a valid resource or is not accessible in the context <%s>";

    private Util() {
    }

    static String load(final Class<?> context, final String resource) {
        try (final InputStream in = context.getResourceAsStream(resource)) {
            return load(in);
        } catch (final IOException | RuntimeException e) {
            throw new UnfitConditionException(String.format(NO_RESOURCE, resource, context), e);
        }
    }

    private static String load(final InputStream stream) throws IOException {
        try (final Reader in = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
            return load(in);
        }
    }

    private static String load(final Reader reader) throws IOException {
        try (final BufferedReader in = new BufferedReader(reader)) {
            return in.lines()
                     .collect(Collectors.joining(NEWLINE));
        }
    }
}
