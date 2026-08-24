package de.team33.patterns.records.rho;

import de.team33.patterns.records.rho.testing.Supply;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class StringLiteralTest {

    private static final Supply SUPPLY = new Supply();

    static Stream<ParseCase> parseCases() {
        return Stream.of(new ParseCase("", null),
                         new ParseCase(SUPPLY.anyString(), null),
                         new ParseCase("\"\"", ""),
                         new ParseCase("\"\\\"\\\"\"", "\"\""),
                         new ParseCase("\"\\\"\\\\\\\"\\\\\\\"\\\"\"", "\"\\\"\\\"\""),
                         new ParseCase("\"this\\tcontains\\nseveral\\rescape\\fsequences\"",
                                       "this\tcontains\nseveral\rescape\fsequences"),
                         parseCase(SUPPLY.anyString()));
    }

    private static ParseCase parseCase(final String expected) {
        return new ParseCase("\"%s\"".formatted(expected), expected);
    }

    static Stream<RenderCase> renderCases() {
        return parseCases().filter(parseCase -> null != parseCase.expected)
                           .map(parseCase -> new RenderCase(parseCase.expected, parseCase.text));
    }

    @Test
    final void render_null() {
        //noinspection DataFlowIssue
        assertThrows(NullPointerException.class, () -> StringLiteral.render(null));
    }

    @ParameterizedTest
    @MethodSource("renderCases")
    final void render(final RenderCase given) {
        final var result = StringLiteral.render(given.text);
        assertEquals(given.expected, result);
    }

    @ParameterizedTest
    @MethodSource("parseCases")
    final void parse(final ParseCase given) {
        try {
            final String result = StringLiteral.parse(given.source());
            assertNotNull(given.expected);
            assertEquals(given.expected, result);
        } catch (final IllegalArgumentException e) {
            assertNull(given.expected);
            // -> as expected
        }
    }

    record RenderCase(String text, String expected) {
    }

    record ParseCase(String text, String expected) {

        final Source source() {
            return new Source(text);
        }
    }
}