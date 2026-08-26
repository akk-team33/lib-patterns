package de.team33.patterns.escaping.namaka.publics;

import de.team33.patterns.escaping.namaka.StringLiteral;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static java.lang.System.Logger.Level.INFO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StringLiteralTest {

    private static final System.Logger LOGGER = System.getLogger(StringLiteralTest.class.getCanonicalName());

    private static void log(final Case given) {
        LOGGER.log(INFO, () -> ("%s: %n" +
                                "    raw:     %s%n" +
                                "    quoted:  %s%n" +
                                "    compact: %s%n").formatted(given, given.raw, given.quoted, given.compact));
    }

    @Test
    final void of_null() {
        assertThrows(NullPointerException.class, () -> StringLiteral.of(null));
    }

    @Test
    final void parse_null() {
        assertThrows(NullPointerException.class, () -> StringLiteral.parse(null));
    }

    @ParameterizedTest
    @EnumSource
    final void of(final Case given) {
        log(given);
        final StringLiteral literal = StringLiteral.of(given.raw);
        assertEquals(given.raw, literal.asString());
        assertEquals(given.quoted, literal.toString());
        assertEquals(given.compact, literal.toCompactString());
    }

    @ParameterizedTest
    @EnumSource
    final void parse(final Case given) {
        log(given);
        final StringLiteral literal = StringLiteral.parse(given.quoted);
        assertEquals(given.raw, literal.asString());
        assertEquals(given.quoted, literal.toString());
        assertEquals(given.compact, literal.toCompactString());
    }

    @ParameterizedTest
    @EnumSource
    final void parse_raw(final Case given) {
        log(given);
        try {
            final StringLiteral literal = StringLiteral.parse(given.raw);
            assertEquals(given.raw, given.compact,
                         () -> "expected to fail - but was %s".formatted(literal));
        } catch (final IllegalArgumentException e) {
            LOGGER.log(INFO, e::getMessage, e);
            assertEquals(given.quoted, given.compact,
                         () -> "expected to work - but threw %s".formatted(e));
        }
    }

    @SuppressWarnings({"unused", "PackageVisibleField"})
    enum Case {

        EMPTY("", "\"\""),
        BLANK("     ", "\"     \""),
        SINGLE_WORD("simple", "\"simple\"", "simple"),
        INTEGER("1234", "\"1234\"", "1234"),
        FLOAT("1.234E-7", "\"1.234E-7\"", "1.234E-7"),
        MORE_WORDS("a simple string", "\"a simple string\""),
        TABBED("a\tsimple\tstring", "\"a\\tsimple\\tstring\""),
        DISTURBED_WORD("sim\0ple", "\"sim\\000ple\""),
        // TODO:
        // LITERAL("\"a\\tsimple\\nstring\"", "\"\\\"a\\\\tsimple\\\\nstring\\\"\""),
        @SuppressWarnings("HardcodedLineSeparator")
        CONTROLS("\0\b\t\n\f\r\17\"\\\177", "\"\\000\\b\\t\\n\\f\\r\\017\\\"\\\\\\177\"");

        final String raw;
        final String quoted;
        final String compact;

        @SuppressWarnings("ParameterNameDiffersFromOverriddenParameter")
        Case(final String raw, final String quoted) {
            this(raw, quoted, quoted);
        }

        Case(final String raw, final String quoted, final String compact) {
            this.raw = raw;
            this.quoted = quoted;
            this.compact = compact;
        }
    }
}