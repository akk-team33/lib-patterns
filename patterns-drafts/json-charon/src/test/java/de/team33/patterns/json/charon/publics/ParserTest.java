package de.team33.patterns.json.charon.publics;

import de.team33.patterns.collection.mneme.FinalEntry;
import de.team33.patterns.collection.mneme.FinalMap;
import de.team33.patterns.json.charon.Json;
import de.team33.patterns.json.charon.JsonValue;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class ParserTest {

    static Stream<ParseCase> parseCases() {
        return Stream.of(new ParseCase("null", Json.NULL),
                         new ParseCase(" \n\t null \r\f", Json.NULL),
                         new ParseCase(" true", Json.valueOf(true)),
                         new ParseCase("false ", Json.valueOf(false)),
                         new ParseCase("1", Json.valueOf(BigDecimal.ONE)),
                         new ParseCase("278", Json.valueOf(BigDecimal.valueOf(278))),
                         new ParseCase(" 123456789.123456789",
                                       Json.valueOf(new BigDecimal("123456789.123456789"))),
                         new ParseCase(" 0.123E1234  ", Json.valueOf(new BigDecimal("0.123E1234"))),
                         new ParseCase("-1", Json.valueOf(BigDecimal.valueOf(-1))),
                         new ParseCase("0", Json.valueOf(BigDecimal.valueOf(0))),
                         new ParseCase("-123.45", Json.valueOf(BigDecimal.valueOf(-123.45))),
                         new ParseCase("1E5", Json.valueOf(new BigDecimal("1E5"))),
                         new ParseCase("-1.2e-5", Json.valueOf(new BigDecimal("-1.2e-5"))),
                         new ParseCase(" \" abc \" ", Json.valueOf(" abc ")),
                         new ParseCase("\t \"\\\\\\\"\\b\\f\\n\\r\\t\" \n",
                                       Json.valueOf("\\\"\b\f\n\r\t")),
                         new ParseCase("{}", Json.valueOf(Map.of())),
                         new ParseCase("[]", Json.valueOf(List.of())),
                         new ParseCase("[\"a\", true, null, 1.23, {\"name\" : \"value\"}]",
                                       Json.valueOf(List.of(Json.valueOf("a"),
                                                            Json.valueOf(true),
                                                            Json.NULL,
                                                            Json.valueOf(new BigDecimal("1.23")),
                                                            Json.valueOf(FinalMap.of("name", Json.valueOf("value")))))),
                         new ParseCase("""
                                               {
                                                   "name1" : "value1" ,
                                                   "name2" : "value2"
                                               ,
                                                   "name3" : [1, 2, 3]
                                               }""",
                                       Json.valueOf(Stream.of(FinalEntry.of("name1", Json.valueOf("value1")),
                                                              FinalEntry.of("name2", Json.valueOf("value2")),
                                                              FinalEntry.of("name3",
                                                                            Json.valueOf(List.of(
                                                                                    Json.valueOf(BigDecimal.valueOf(1)),
                                                                                    Json.valueOf(BigDecimal.valueOf(2)),
                                                                                    Json.valueOf(BigDecimal.valueOf(3))))))
                                                          .collect(FinalMap.collector()))));
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
        final JsonValue result = Json.parse(given.source);
        assertEquals(given.expected, result);
    }

    @ParameterizedTest
    @MethodSource("failCases")
    final void parse_fail(final String given) {
        try {
            final JsonValue result = Json.parse(given);
            fail("expected to fail - but was %s".formatted(result));
        } catch (final IllegalArgumentException e) {
            // as expected
        }
    }

    record ParseCase(String source, JsonValue expected) {
    }
}