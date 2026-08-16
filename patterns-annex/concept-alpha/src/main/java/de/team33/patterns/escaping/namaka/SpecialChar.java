package de.team33.patterns.escaping.namaka;

import de.team33.patterns.enums.pan.Values;

import java.util.Optional;
import java.util.stream.Collectors;

import static de.team33.patterns.escaping.namaka.Condition.isCharLength;
import static de.team33.patterns.proving.kerberos.Guard.prove;

@SuppressWarnings({"unused", "HardcodedLineSeparator"})
enum SpecialChar {

    BACKSPACE('\b', 'b'),
    FORMFEED('\f', 'f'),
    NEWLINE('\n', 'n'),
    RETURN('\r', 'r'),
    SPACE(' ', 's'),
    TABULATOR('\t', 't'),
    SINGLE_QUOTE('\'', '\''),
    DOUBLE_QUOTE('"', '"'),
    BACKSLASH('\\', '\\', true);

    private static final Values<SpecialChar> VALUES = Values.of(SpecialChar.class);
    static final String REGEX = VALUES.mapAll(value -> value.regex)
                                      .collect(Collectors.joining("", "[", "]"));
    private static final String EXPECTED_CHAR_LENGTH = "length of <group> is expected to be 1 - but was %d";
    private final char core;
    private final char symbol;
    private final String regex;

    SpecialChar(final char core, final char symbol) {
        this(core, symbol, false);
    }

    SpecialChar(final char core, final char symbol, final boolean escInRegex) {
        this.core = core;
        this.symbol = symbol;
        this.regex = (escInRegex ? "\\%c" : "%c").formatted(symbol);
    }

    static Optional<String> encode(final char core) {
        return VALUES.findAny(value -> value.core == core)
                     .map(value -> "%c%c".formatted(BACKSLASH.core, value.symbol));
    }

    static char toChar(final String group) {
        prove(isCharLength(group), () -> EXPECTED_CHAR_LENGTH.formatted(group.length()));
        final char symbol = group.charAt(0);
        return VALUES.findAny(value -> value.symbol == symbol)
                     .map(value -> value.core)
                     .orElseThrow(() -> new IllegalArgumentException("unknown symbol: '%c'".formatted(symbol)));
    }

    final char core() {
        return core;
    }
}
