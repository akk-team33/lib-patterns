package de.team33.patterns.escaping.namaka.publics;

import de.team33.patterns.escaping.namaka.EscCodec;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.security.SecureRandom;
import java.util.Random;
import java.util.stream.IntStream;

import static java.lang.System.Logger.Level.DEBUG;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("HardcodedLineSeparator")
class EscCodecTest {

    private static final System.Logger LOGGER = System.getLogger(EscCodecTest.class.getCanonicalName());
    private static final Random RANDOM = new SecureRandom();

    static String anyString(final int length) {
        //noinspection NumericCastThatLosesPrecision
        return IntStream.generate(() -> RANDOM.nextInt(0x10, RANDOM.nextInt(0x20, RANDOM.nextInt(0x30, 0x10000))))
                        .limit(length)
                        .collect(StringBuilder::new, (sb, i) -> sb.append((char) i), StringBuilder::append)
                        .toString();
    }

    @ParameterizedTest
    @EnumSource
    final void unescape_escape(final TestCase given) {
        final EscCodec codec = given.codec;
        final String unescaped = codec.decode(given.escaped);
        final String biEscaped = codec.encode(given.escaped);
        final String reEscaped = codec.encode(unescaped);
        final String reUnescaped = codec.decode(reEscaped);
        LOGGER.log(DEBUG, () -> ("unescape_escape(%s) ...%n" +
                                 "    escaped:   '%s'%n" +
                                 "    biEscaped: '%s'%n" +
                                 "    unescaped: '%s'%n" +
                                 "    reEscaped: '%s'").formatted(given, given.escaped, biEscaped, unescaped, reEscaped));
        assertEquals(given.unescaped, unescaped);
        assertEquals(given.reEscaped, reEscaped);
        assertEquals(unescaped, reUnescaped);
        assertEquals(given.escaped, codec.decode(biEscaped));
    }

    @ParameterizedTest
    @ValueSource(ints = {8, 13, 21, 34, 55, 89, 144, 233})
    final void randomRoundTrip(final int length) {
        final EscCodec codec = EscCodec.DOUBLE_QUOTES;
        final String unescaped = anyString(length);
        final String escaped = codec.encode(unescaped);
        final String biEscaped = codec.encode(escaped);
        LOGGER.log(DEBUG, () -> ("randomRoundTrip(%d) ...%n" +
                                 "    unescaped: '%s'%n" +
                                 "    escaped:   '%s'%n" +
                                 "    biEscaped: '%s'").formatted(length, unescaped, escaped, biEscaped));
        assertEquals(escaped, codec.decode(biEscaped));
        assertEquals(unescaped, codec.decode(escaped));
    }

    @ParameterizedTest
    @ValueSource(ints = {0x20, 0x40, 0x80, 0x100, 0x1000, 0x10000})
    @SuppressWarnings("NumericCastThatLosesPrecision")
    final void randomCharRoundTrip(final int bound) {
        final EscCodec codec = EscCodec.SINGLE_QUOTES;
        final int min = bound >> 8;
        final char unescaped = (char) RANDOM.nextInt(min, bound);
        final String escaped = codec.encodeChar(unescaped);
        LOGGER.log(DEBUG, () -> ("randomCharRoundTrip(%d, %d) ...%n" +
                                 "    unescaped: '%c'%n" +
                                 "    escaped:   '%s'").formatted(min, bound, unescaped, escaped));
        assertEquals(unescaped, codec.decodeChar(escaped));
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    final void unescape_escape_null() {
        assertThrows(NullPointerException.class, () -> EscCodec.DOUBLE_QUOTES.decode(null));
        assertThrows(NullPointerException.class, () -> EscCodec.DOUBLE_QUOTES.encode(null));
    }

    @Test
    final void unescape_partialUnicodeSequence() {
        try {
            final String unescaped = EscCodec.DOUBLE_QUOTES.decode("Test \\u12GZ");
            fail("Expected to fail - but was '%s'".formatted(unescaped));
        } catch (final IllegalArgumentException e) {
            LOGGER.log(DEBUG, e::getMessage, e);
            assertTrue(e.getMessage().contains("\\u12GZ"));
        }
    }

    @ParameterizedTest
    @EnumSource
    final void testCase(final TestCase given) {
        assertEquals(given.escaped.toCharArray().length, given.escaped.length());
        assertEquals(given.unescaped.toCharArray().length, given.unescaped.length());
        assertEquals(given.reEscaped.toCharArray().length, given.reEscaped.length());
    }

    @SuppressWarnings({"unused", "PackageVisibleField"})
    enum TestCase {

        EMPTY("", ""),
        NORMAL("Normal's String", "Normal's String"),
        UNUSUAL("Normal\\'s String", "Normal's String", "Normal's String"),
        NON_PRINTABLE(
                "\\u0001\\u000e\\u000f\\u007f\\u0081\\u0000",
                "\1\016\17\177\201\00",
                "\\001\\016\\017\\177\\201\\000"),
        @SuppressWarnings("EscapedSpace")
        SPACES(
                "\\s<-leading\\s<-inner->\\strailing->\\s",
                "\s<-leading\s<-inner->\strailing->\s",
                " <-leading <-inner-> trailing-> "),
        BASIC("Hello\\nWorld", "Hello\nWorld"),
        QUOTES_AND_BACKSLASHES("\\\"Quote\\\" \\\\Backslash\\\\", "\"Quote\" \\Backslash\\"),
        CONTROL_CHARACTERS("\\b\\t\\n\\f\\r", "\b\t\n\f\r"),
        UNICODE_CHARACTERS("Gr\\u00fc\\u00dfe \\u2615 \\ud83c\\udf40", "Grüße ☕ 🍀", "Grüße ☕ 🍀"),
        SURROGATE_PAIR("\\ud83c\\udf40\\ud83c\\udfb5", "🍀🎵", "🍀🎵"),
        OCTAL_EDGE_CASES("\\0\\7\\77\\377\\777", "\0\7?ÿ?7", "\\000\\007?ÿ?7");

        final EscCodec codec;
        final String escaped;
        final String unescaped;
        final String reEscaped;

        @SuppressWarnings("ParameterNameDiffersFromOverriddenParameter")
        TestCase(final String escaped, final String unEscaped) {
            this(EscCodec.DOUBLE_QUOTES, escaped, unEscaped, escaped);
        }

        TestCase(final String escaped, final String unEscaped, final String reEscaped) {
            this(EscCodec.DOUBLE_QUOTES, escaped, unEscaped, reEscaped);
        }

        TestCase(final EscCodec codec, final String escaped, final String unEscaped, final String reEscaped) {
            this.codec = codec;
            this.escaped = escaped;
            this.unescaped = unEscaped;
            this.reEscaped = reEscaped;
        }
    }
}
