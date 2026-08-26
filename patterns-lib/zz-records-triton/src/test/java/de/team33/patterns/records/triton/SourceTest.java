package de.team33.patterns.records.triton;

import de.team33.patterns.records.triton.testing.Supply;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class SourceTest {

    private static final Supply SUPPLY = new Supply();

    static Stream<AnyMoreCase> anyMoreCases() {
        return Stream.of(new AnyMoreCase("", 0, false),
                         new AnyMoreCase(SUPPLY.anyString(), Integer.MAX_VALUE, false),
                         new AnyMoreCase(SUPPLY.anyString(), 0, true));
    }

    static Stream<PeekCase> peekCases() {
        return Stream.of(new PeekCase("", 0),
                         new PeekCase(SUPPLY.anyString(), Integer.MAX_VALUE),
                         new PeekCase(SUPPLY.anyString(), 0),
                         new PeekCase("more than 10 characters", 10));
    }

    @Test
    final void new_Source_Null() {
        assertThrows(NullPointerException.class, () -> new Source(null));
    }

    @ParameterizedTest
    @MethodSource("anyMoreCases")
    final void hasMore(final AnyMoreCase given) {
        final boolean result = given.source().hasMore();
        assertEquals(given.expected, result);
    }

    @ParameterizedTest
    @MethodSource("anyMoreCases")
    final void failIfMore(final AnyMoreCase given) {
        try {
            given.source().failIfMore();
            assertFalse(given.expected);
        } catch (final IllegalArgumentException e) {
            assertTrue(given.expected);
        }
    }

    @ParameterizedTest
    @MethodSource("anyMoreCases")
    final void failIfEOT(final AnyMoreCase given) {
        try {
            given.source().failIfEOT();
            assertTrue(given.expected);
        } catch (final IllegalArgumentException e) {
            assertFalse(given.expected);
        }
    }

    @ParameterizedTest
    @MethodSource("peekCases")
    final void peek(final PeekCase given) {
        final Source source = given.source();
        try {
            final char result = source.peek();
            assertEquals(given.expected(), result);
        } catch (final IllegalArgumentException e) {
            assertNull(given.expected());
        }
        assertEquals(given.index, source.index());
    }

    @ParameterizedTest
    @MethodSource("peekCases")
    final void skip(final PeekCase given) {
        final Source source = given.source();
        final int expected = given.index + 1;
        source.skip();
        assertEquals(expected, source.index());
    }

    @Test
    final void expect_EOT() {
        final String text = SUPPLY.anyString();
        final int index = text.length();
        final Source source = new Source(text).skip(index);
        final char sample = SUPPLY.anyChar();

        try {
            source.expect(sample);
            fail("expected to fail - but index was %d -> %d".formatted(index, source.index()));
        } catch (final IllegalArgumentException e) {
            // as expected
        }
    }

    @Test
    final void expect_positive() {
        final String text = SUPPLY.anyString();
        final int index = SUPPLY.anyInt(text.length());
        final Source source = new Source(text).skip(index);
        final char sample = text.charAt(index);

        source.expect(sample);
        assertEquals(index + 1, source.index());
    }

    @Test
    final void expect_negative() {
        final String text = SUPPLY.anyString();
        final int index = SUPPLY.anyInt(text.length());
        final Source source = new Source(text).skip(index);
        final char exclude = text.charAt(index);
        char sample = SUPPLY.anyChar();
        while (exclude == sample) {
            sample = SUPPLY.anyChar();
        }

        try {
            source.expect(sample);
            fail("expected to fail - but index was %d -> %d".formatted(index, source.index()));
        } catch (final IllegalArgumentException e) {
            // as expected
        }
    }

    @Test
    final void skipWhitespace_positive() {
        final String head = SUPPLY.anyString();
        final int index = head.length();
        final String text = head + SUPPLY.anyString(10, " \n\r\t");
        final Source source = new Source(text).skip(index);

        source.skipWhitespace();
        assertEquals(index + 10, source.index());
    }

    @Test
    final void skipWhitespace_negative() {
        final String text = SUPPLY.anyString()
                                  .replace(' ', '_');
        final int index = SUPPLY.anyInt(text.length());
        final Source source = new Source(text).skip(index);

        source.skipWhitespace();
        assertEquals(index, source.index());
    }

    @Test
    final void peakUntil() {
        final String head = SUPPLY.anyString(SUPPLY.anyInt(8), "abcdefg");
        final String tail = SUPPLY.anyString(SUPPLY.anyInt(8), "abcdefg");
        final String text = head + ' ' + tail;
        final Source source = new Source(text);

        assertEquals(head, source.peekUntil(c -> c == ' '));
        source.skip(head.length())
              .expect(' ');
        assertEquals(tail, source.peekUntil(c -> c == ' '));
        source.skip(tail.length());
        assertFalse(source.hasMore());
    }

    @FunctionalInterface
    interface SourceCase {

        String text();

        @SuppressWarnings("SameReturnValue")
        default int index() {
            return 0;
        }

        default Source source() {
            return new Source(text()).skip(index());
        }
    }

    record PeekCase(String text, int index) implements SourceCase {

        PeekCase {
            index = Integer.min(index, text.length());
        }

        Character expected() {
            return (index < text.length()) ? text.charAt(index) : null;
        }
    }

    record AnyMoreCase(String text, int index, boolean expected) implements SourceCase {

        AnyMoreCase {
            index = Integer.min(index, text.length());
        }
    }
}