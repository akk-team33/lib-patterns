package de.team33.patterns.arbitrary.mimas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Random;
import java.util.random.RandomGenerator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class Util {

    @SuppressWarnings("SpellCheckingInspection")
    static final String STD_CHARACTERS = "0123456789_abcdefghijklmnopqrstuvwxyz-ABCDEFGHIJKLMNOPQRSTUVWXYZ " +
                                         "!#$§%&*+,.?@äöüÄÖÜß";
    static final long MAX_RETRY = 16;

    private static final String NEWLINE = String.format("%n");
    private static final String NO_RESOURCE = "Should not happen:" +
            " <%s> is not a valid resource or is not accessible in the context <%s>";

    private Util() {
    }

    static String normalName(final String name) {
        final Prefix prefix = Stream.of(Prefix.values())
                                    .filter(pfx -> name.startsWith(pfx.value))
                                    .findAny()
                                    .orElse(Prefix.NONE);
        final int headIndex = prefix.value.length();
        final int tailIndex = (headIndex < name.length()) ? (headIndex + 1) : headIndex;
        final String head = name.substring(headIndex, tailIndex).toUpperCase(Locale.ROOT);
        return head + name.substring(tailIndex);
    }

    static Random legacy(final RandomGenerator generator) {
        return (generator instanceof Random random) ? random : new Proxy(generator);
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
