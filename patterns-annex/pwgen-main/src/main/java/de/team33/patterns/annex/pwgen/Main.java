package de.team33.patterns.annex.pwgen;

import de.team33.patterns.arbitrary.mimas.Generator;

import java.security.SecureRandom;
import java.util.stream.Stream;

public final class Main {

    private static final String CHARS = "abcdefghijkmnopqrstuvwxyz-ABCDEFGHJKLMNPQRSTUVWXYZ_0123456789.&%$";

    private Main() {
    }

    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public static void main(final String[] args) {
        final Generator pwGen = Generator.of(new SecureRandom());
        Stream.generate(() -> pwGen.anyString(20, CHARS)).limit(20).forEach(System.out::println);
    }
}
