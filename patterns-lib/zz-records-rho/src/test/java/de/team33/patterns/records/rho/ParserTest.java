package de.team33.patterns.records.rho;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class ParserTest {

    static Stream<ParseCase> parseCases() {
        return Stream.of(new ParseCase("null", JsonValue.NULL),
                         new ParseCase(" \n\t null \r\f", JsonValue.NULL),
                         new ParseCase(" true", new JsonBoolean(true)),
                         new ParseCase("false ", new JsonBoolean(false)),
                         new ParseCase("1", new JsonNumber(BigDecimal.ONE)),
                         new ParseCase("278", new JsonNumber(BigDecimal.valueOf(278))),
                         new ParseCase(" 123456789.123456789",
                                       new JsonNumber(new BigDecimal("123456789.123456789"))),
                         new ParseCase(" 0.123E1234  ", new JsonNumber(new BigDecimal("0.123E1234"))),
                         new ParseCase("-1", new JsonNumber(BigDecimal.valueOf(-1))),
                         new ParseCase("0", new JsonNumber(BigDecimal.valueOf(0))),
                         new ParseCase("-123.45", new JsonNumber(BigDecimal.valueOf(-123.45))),
                         new ParseCase("1E5", new JsonNumber(new BigDecimal("1E5"))),
                         new ParseCase("-1.2e-5", new JsonNumber(new BigDecimal("-1.2e-5"))),
                         new ParseCase(" \" abc \" ", new JsonString(" abc ")),
                         new ParseCase("\t \"\\\\\\\"\\b\\f\\n\\r\\t\" \n",
                                       new JsonString("\\\"\b\f\n\r\t")),
                         new ParseCase("{}", JsonObject.builder().build()),
                         new ParseCase("[]", JsonArray.builder().build()),
                         new ParseCase("[\"a\", true, null, 1.23, {\"name\" : \"value\"}]",
                                       JsonArray.builder()
                                                .add(new JsonString("a"))
                                                .add(new JsonBoolean(true))
                                                .add(JsonValue.NULL)
                                                .add(new JsonNumber(new BigDecimal("1.23")))
                                                .add(JsonObject.builder()
                                                               .put("name", new JsonString("value"))
                                                               .build())
                                                .build()),
                         new ParseCase("{\n" +
                                       "        \"name1\" : \"value1\" ,\n" +
                                       "        \"name2\" : \"value2\"\n,\n" +
                                       "        \"name3\" : [1, 2, 3]\n" +
                                       "    }",
                                       JsonObject.builder()
                                                 .put("name1", new JsonString("value1"))
                                                 .put("name2", new JsonString("value2"))
                                                 .put("name3",
                                                      JsonArray.builder()
                                                               .add(new JsonNumber(BigDecimal.valueOf(1)))
                                                               .add(new JsonNumber(BigDecimal.valueOf(2)))
                                                               .add(new JsonNumber(BigDecimal.valueOf(3)))
                                                               .build())
                                                 .build()));
    }

    static Stream<String> failCases() {
        return Stream.of("",
                         "{null}",
                         "{",
                         "{\"name\":\"value\"",
                         "}",
                         "[",
                         "[1,2",
                         "]",
                         "tru",
                         "nul",
                         "fal",
                         "01",
                         "1.",
                         ".5",
                         "{\"a\"}",
                         "{:\"b\"}",
                         "{\"a\":}",
                         "{\"a\",1}",
                         "{,\"a\":1}",
                         "{\"a\":1,}",
                         "\"abc",
                         "\"\\q\"");
    }

    @ParameterizedTest
    @MethodSource("parseCases")
    final void parse(final ParseCase given) {
        final JsonValue result = Parser.parse(given.source);
        assertEquals(given.expected, result);
    }

    @ParameterizedTest
    @MethodSource("failCases")
    final void parse_fail(final String given) {
        try {
            final JsonValue result = Parser.parse(given);
            fail("expected to fail - but was %s".formatted(result));
        } catch (final IllegalArgumentException e) {
            // as expected
        }
    }

    record ParseCase(String source, JsonValue expected) {
    }
}