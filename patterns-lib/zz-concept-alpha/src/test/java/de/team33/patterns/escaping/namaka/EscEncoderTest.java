package de.team33.patterns.escaping.namaka;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EscEncoderTest {

    @ParameterizedTest
    @EnumSource
    final void test(final TestCase given) {
        final EscEncoder encoder = given.encoder;
        final String result = encoder.encode(given.uncoded);
        assertEquals(given.expected, result);
    }

    @SuppressWarnings({"unused", "PackageVisibleField"})
    enum TestCase {

        MINIMUM_NORMAL(EscEncoder.using(""), "normal text", "normal text"),
        MINIMUM_WITH_QUOTES(EscEncoder.using(""), "'normal' \"text\"", "'normal' \"text\""),
        MINIMUM_WITH_BACKSLASH(EscEncoder.using(""), "normal\\text", "normal\\\\text"),
        @SuppressWarnings({"HardcodedLineSeparator", "EscapedSpace", "UnnecessaryStringEscape"})
        MINIMUM_SPECIAL(EscEncoder.using(""), "\b\f\n\r\s\t\'\"\\", "\\b\\f\\n\\r \\t'\"\\\\"),
        MINIMUM_NON_PRINTABLE(EscEncoder.using(""), "\1\16\17\177\201\00", "\\001\\016\\017\\177\\201\\000"),
        @SuppressWarnings({"HardcodedLineSeparator", "EscapedSpace", "UnnecessaryStringEscape"})
        SINGLE_QUOTE(EscEncoder.using("'"), "\b\f\n\r\s\t\'\"\\", "\\b\\f\\n\\r \\t\\'\"\\\\"),
        @SuppressWarnings({"HardcodedLineSeparator", "EscapedSpace", "UnnecessaryStringEscape"})
        DOUBLE_QUOTE(EscEncoder.using("\""), "\b\f\n\r\s\t\'\"\\", "\\b\\f\\n\\r \\t'\\\"\\\\"),
        USE_SPACE_NORMAL(EscEncoder.using(" "), "normal text", "normal\\stext"),
        @SuppressWarnings({"HardcodedLineSeparator", "EscapedSpace", "UnnecessaryStringEscape"})
        USE_SPACE_SPECIAL(EscEncoder.using(" "), "\b\f\n\r\s\t\'\"\\", "\\b\\f\\n\\r\\s\\t'\"\\\\"),
        @SuppressWarnings({"HardcodedLineSeparator", "EscapedSpace", "UnnecessaryStringEscape"})
        USE_SOME_SPECIAL(EscEncoder.using("'\" "), "\b\f\n\r\s\t\'\"\\", "\\b\\f\\n\\r\\s\\t\\'\\\"\\\\"),
        @SuppressWarnings({"HardcodedLineSeparator", "EscapedSpace", "UnnecessaryStringEscape"})
        USE_MORE_SPECIAL(EscEncoder.using("\b\f\n\r\s\t\'\"\\"),
                         "\b\f\n\r\s\t\'\"\\", "\\b\\f\\n\\r\\s\\t\\'\\\"\\\\"),
        UNUSUAL(EscEncoder.using("omx"), "normal text", "n\\157r\\155al te\\170t");

        final EscEncoder encoder;
        final String uncoded;
        final String expected;

        TestCase(final EscEncoder encoder, final String uncoded, final String expected) {
            this.encoder = encoder;
            this.uncoded = uncoded;
            this.expected = expected;
        }
    }
}