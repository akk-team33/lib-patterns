package de.team33.patterns.records.triton;

import de.team33.patterns.enums.pan.Values;

import java.util.Optional;

final class StringLiteral {

    private StringLiteral() {
    }

    static String render(final String source) {
        final StringBuilder result = new StringBuilder().append('"');
        for (int index = 0; index < source.length(); ++index) {
            final char next = source.charAt(index);
            final String quoted = mappingPlain(next).map(EscapeMapping::escaped)
                                                    .orElseGet(() -> Character.toString(next));
            result.append(quoted);
        }
        return result.append('"').toString();
    }

    static String parse(final Source source) {
        final StringBuilder body = new StringBuilder();
        char next = source.expect('"')
                          .failIfEOT()
                          .peek();
        while (next != '"') {
            source.skip();
            if ('\\' == next) {
                final char symbol = source.failIfEOT().peek();
                final char plain = mappingSymbol(symbol).map(EscapeMapping::plain)
                                                        .orElseThrow(() -> parseException(source.index() - 1, symbol));
                source.skip();
                body.append(plain);
            } else {
                body.append(next);
            }
            next = source.failIfEOT().peek();
        }
        source.skip();
        return body.toString();
    }

    private static IllegalArgumentException parseException(final int index, final char symbol) {
        return new IllegalArgumentException(
                "unexpected escape sequence: '\\%c' at index %d".formatted(symbol, index));
    }

    private static Optional<EscapeMapping> mappingSymbol(final char symbol) {
        return EscapeMapping.VALUES.findAny(value -> symbol == value.symbol);
    }

    private static Optional<EscapeMapping> mappingPlain(final char plain) {
        return EscapeMapping.VALUES.findAny(value -> plain == value.plain);
    }

    private enum EscapeMapping {

        BACKSLASH('\\', '\\'),
        DOUBLE_QUOTE('"', '"'),
        BACKSPACE('\b', 'b'),
        FORM_FEED('\f', 'f'),
        NEW_LINE('\n', 'n'),
        CARRIAGE_RETURN('\r', 'r'),
        TABULATOR('\t', 't');

        private static final Values<EscapeMapping> VALUES = Values.of(EscapeMapping.class);

        private final char plain;
        private final char symbol;

        EscapeMapping(final char plain, final char symbol) {
            this.plain = plain;
            this.symbol = symbol;
        }

        final char plain() {
            return plain;
        }

        final String escaped() {
            return "\\" + symbol;
        }
    }
}
