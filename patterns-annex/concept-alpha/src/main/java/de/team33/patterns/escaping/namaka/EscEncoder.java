package de.team33.patterns.escaping.namaka;

final class EscEncoder {

    private static final int ASCII_LIMIT = 256;

    private final String additional;

    private EscEncoder(final String quotes) {
        this.additional = SpecialChar.BACKSLASH.core() + quotes;
    }

    static EscEncoder using(final String quotes) {
        return new EscEncoder(quotes);
    }

    private static String encodeChar(final char c) {
        return SpecialChar.encode(c)
                          .orElseGet(() -> ((c < ASCII_LIMIT) ? "\\%03o"
                                                              : "\\u%04X").formatted((int) c));
    }

    final String encode(final String uncoded) {
        final int capacity = Integer.max(6, uncoded.length() << 1);
        final StringBuilder sb = new StringBuilder(capacity).append(normal(uncoded, 0));

        int start = sb.length();
        while (start < uncoded.length()) {
            sb.append(encodeChar(uncoded.charAt(start)));
            start += 1;

            final String normal = normal(uncoded, start);
            sb.append(normal);
            start += normal.length();
        }

        return sb.toString();
    }

    private String normal(final String uncoded, final int start) {
        int limit = start;
        while (limit < uncoded.length() && isNormal(uncoded.charAt(limit))) {
            limit += 1;
        }
        return uncoded.substring(start, limit);
    }

    private boolean isNormal(final char c) {
        return Character.isDefined(c) && !isSpecial(c);
    }

    private boolean isSpecial(final char c) {
        return (0 <= additional.indexOf(c)) || Character.isISOControl(c);
    }
}
