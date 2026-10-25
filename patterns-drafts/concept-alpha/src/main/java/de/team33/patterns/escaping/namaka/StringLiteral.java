package de.team33.patterns.escaping.namaka;

import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Represents a Java String Literal.
 */
public final class StringLiteral {

    private static final char BACKSLASH = SpecialChar.BACKSLASH.core();
    private static final String QUOTE = String.valueOf(SpecialChar.DOUBLE_QUOTE.core());
    private static final EscCodec CODEC = EscCodec.DOUBLE_QUOTES;
    private static final Pattern BLANK = Pattern.compile("\\s");
    private static final String INVALID = "not a valid string literal: »%s«%n" +
                                          "    A valid string literal ...%n" +
                                          "    - does not contain straight control characters%n" +
                                          "    - does not contain straight QUOTEs or BACKSLASHes%n" +
                                          "      other than preceded by a BACKSLASH%n" +
                                          "    - is delimited by QUOTEs unless it also%n" +
                                          "      does not contain any WHITE_SPACE";

    private final String raw;
    private final String quoted;

    private StringLiteral(final String raw, final String quoted) {
        this.raw = raw;
        this.quoted = quoted;
    }

    /**
     * Retrieves a {@link StringLiteral} of a given {@link String} <em>input</em>.
     *
     * @throws NullPointerException if <em>input</em> is {@code null}.
     */
    public static StringLiteral of(final String input) {
        return new StringLiteral(input, String.join("", QUOTE, CODEC.encode(input), QUOTE));
    }

    /**
     * Retrieves a {@link StringLiteral} by parsing a given {@link String} <em>literal</em>.
     *
     * @throws NullPointerException     if <em>literal</em> is {@code null}.
     * @throws IllegalArgumentException if <em>literal</em> cannot be parsed.
     */
    public static StringLiteral parse(final String literal) {
        if (isQuoted(literal)) {
            return of(CODEC.decode(body(literal)));
        } else {
            return Optional.of(of(literal))
                           .filter(StringLiteral::isCompact)
                           .orElseThrow(() -> new IllegalArgumentException(INVALID.formatted(literal)));
        }
    }

    private static boolean isQuoted(final String literal) {
        return literal.startsWith(QUOTE) && literal.endsWith(QUOTE);
    }

    private static String body(final String literal) {
        final int delta = QUOTE.length();
        return literal.substring(delta, literal.length() - delta);
    }

    private boolean isCompact() {
        return !(raw.isEmpty() || BLANK.matcher(raw).find() || (0 <= quoted.indexOf(BACKSLASH)));
    }

    public final String asString() {
        return raw;
    }

    @Override
    public final boolean equals(final Object obj) {
        return (this == obj) || ((obj instanceof final StringLiteral literal) && quoted.equals(literal.quoted));
    }

    @Override
    public final int hashCode() {
        return quoted.hashCode();
    }

    @Override
    public final String toString() {
        return quoted;
    }

    public final String toCompactString() {
        return isCompact() ? raw : quoted;
    }
}
