package de.team33.patterns.arbitrary.mimas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class Util {

    @SuppressWarnings("SpellCheckingInspection")
    static final String STD_CHARACTERS = "0123456789_abcdefghijklmnopqrstuvwxyz-ABCDEFGHIJKLMNOPQRSTUVWXYZ " +
                                         "!#$§%&*+,.?@äöüÄÖÜß";
    static final long MAX_RETRY = 16;
    static final int DOUBLE_RESOLUTION = Double.SIZE - 11;
    static final int FLOAT_RESOLUTION = Float.SIZE - 8;
    static final BigInteger MIN_LONG = BigInteger.valueOf(Long.MIN_VALUE);
    static final BigInteger MAX_LONG = BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE);

    private static final String NEWLINE = String.format("%n");
    private static final String NO_RESOURCE = "Should not happen:" +
            " <%s> is not a valid resource or is not accessible in the context <%s>";

    private Util() {
    }

    static BigInteger anyBigInteger(final Generator generator, final BigInteger bound, final int bitLength) {
        if (BigInteger.ZERO.compareTo(bound) < 0) {
            return Stream.generate(() -> generator.anyBits(bitLength))
                         .limit(MAX_RETRY)
                         .filter(result -> result.compareTo(bound) < 0)
                         .findAny()
                         .orElseGet(() -> generator.anyBits(bitLength - 1));
        }
        throw new IllegalArgumentException("<bound> must be greater than ZERO but was " + bound);
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
