package de.team33.patterns.escaping.namaka;

import de.team33.patterns.proving.kerberos.Guard;

import java.util.regex.Matcher;

public final class EscCodec {

    public static final EscCodec DOUBLE_QUOTES = new EscCodec("\"");
    public static final EscCodec SINGLE_QUOTES = new EscCodec("'");

    private static final String EXPECTED_SINGLE_CHAR =
            "<encoded> is expected to represent a single character - but was <%s>";

    private final EscEncoder encoder;

    private EscCodec(final String quotes) {
        encoder = EscEncoder.using(quotes);
    }

    public final String encode(final String uncoded) {
        return encoder.encode(uncoded);
    }

    public final String encodeChar(final char uncoded) {
        return encode(String.valueOf(uncoded));
    }

    @SuppressWarnings("MethodMayBeStatic")
    public final String decode(final CharSequence encoded) {
        final StringBuilder builder = new StringBuilder(encoded.length());
        final Matcher matcher = EscChar.PATTERN.matcher(encoded);
        while (matcher.find()) {
            final String replacement = String.valueOf(EscChar.toChar(matcher));
            matcher.appendReplacement(builder, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(builder);
        return builder.toString();
    }

    public final char decodeChar(final CharSequence encoded) {
        return Guard.proved(decode(encoded), Condition::isCharLength, any -> EXPECTED_SINGLE_CHAR.formatted(encoded))
                    .charAt(0);
    }
}
