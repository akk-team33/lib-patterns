package de.team33.patterns.json.charon.publics;

import de.team33.patterns.collection.mneme.FinalList;
import de.team33.patterns.collection.mneme.FinalMap;
import de.team33.patterns.json.charon.Json;
import de.team33.patterns.json.charon.JsonObject;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class ParserTest {

    static Stream<ParseCase> parseCases() {
        return Stream.of(new ParseCase("null", Json.NULL),
                         new ParseCase(" \n\t null \r\f", Json.NULL),
                         new ParseCase(" true", Json.value(true)),
                         new ParseCase("false ", Json.value(false)),
                         new ParseCase("1", Json.value(BigDecimal.ONE)),
                         new ParseCase("278", Json.value(BigDecimal.valueOf(278))),
                         new ParseCase(" 123456789.123456789",
                                       Json.value(new BigDecimal("123456789.123456789"))),
                         new ParseCase(" 0.123E1234  ", Json.value(new BigDecimal("0.123E1234"))),
                         new ParseCase("-1", Json.value(BigDecimal.valueOf(-1))),
                         new ParseCase("0", Json.value(BigDecimal.valueOf(0))),
                         new ParseCase("-123.45", Json.value(BigDecimal.valueOf(-123.45))),
                         new ParseCase("1E5", Json.value(new BigDecimal("1E5"))),
                         new ParseCase("-1.2e-5", Json.value(new BigDecimal("-1.2e-5"))),
                         new ParseCase(" \" abc \" ", Json.value(" abc ")),
                         new ParseCase("\t \"\\\\\\\"\\b\\f\\n\\r\\t\" \n",
                                       Json.value("\\\"\b\f\n\r\t")),
                         new ParseCase("{}", Json.value(FinalMap.empty())),
                         new ParseCase("[]", Json.value(FinalList.empty())),
                         new ParseCase("[\"a\", true, null, 1.23, {\"name\" : \"value\"}]",
                                       Json.value(List.of(
                                               Json.value("a"),
                                               Json.value(true),
                                               Json.NULL,
                                               Json.value(new BigDecimal("1.23")),
                                               Json.value(FinalMap.of("name", Json.value("value")))))),
                         new ParseCase("""
                                               {
                                                   "name1" : "value1" ,
                                                   "name2" : "value2"
                                                   ,
                                                   "name3" : [1, 2, 3]
                                               }""",
                                       new LinkedHashMap<String, Json.Value>() {{
                                           put("name1", Json.value("value1"));
                                           put("name2", Json.value("value2"));
                                           put("name3", Json.value(List.of(
                                                   Json.value(BigDecimal.valueOf(1)),
                                                   Json.value(BigDecimal.valueOf(2)),
                                                   Json.value(BigDecimal.valueOf(3)))));
                                       }}.entrySet().stream().collect(JsonObject.collector())));
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
        final Json.Value result = Json.parse(given.source);
        assertEquals(given.expected, result);
    }

    @ParameterizedTest
    @MethodSource("failCases")
    final void parse_fail(final String given) {
        try {
            final Json.Value result = Json.parse(given);
            fail("expected to fail - but was %s".formatted(result));
        } catch (final IllegalArgumentException e) {
            // as expected
        }
    }

    record ParseCase(String source, Json.Value expected) {
    }
}