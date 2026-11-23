package de.team33.patterns.json.charon.publics;

import de.team33.patterns.collection.mneme.FinalEntry;
import de.team33.patterns.collection.mneme.FinalMap;
import de.team33.patterns.json.charon.Json;
import de.team33.patterns.json.charon.JsonArray;
import de.team33.patterns.json.charon.JsonObject;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RendererTest {

    private static Stream<Json.Value> jsonValues() {
        return Stream.of(Json.NULL,
                         Json.value(true),
                         Json.value(false),
                         Json.value(BigDecimal.ONE),
                         Json.value(BigDecimal.valueOf(278)),
                         Json.value(new BigDecimal("123456789.123456789")),
                         Json.value(new BigDecimal("0.123E1234")),
                         Json.value(BigDecimal.valueOf(-1)),
                         Json.value(BigDecimal.valueOf(0)),
                         Json.value(BigDecimal.valueOf(-123.45)),
                         Json.value(new BigDecimal("1E5")),
                         Json.value(new BigDecimal("-1.2e-5")),
                         Json.value(" abc "),
                         Json.value("\\\"\b\f\n\r\t"),
                         Json.value(FinalMap.empty()),
                         Json.value(List.of()),
                         Json.value(List.of(
                                 Json.value("a"),
                                 Json.value(true),
                                 Json.NULL,
                                 Json.value(new BigDecimal("1.23")),
                                 Json.value(FinalMap.of("name", Json.value("value"))))),
                         Json.value(new LinkedHashMap<>() {{
                             put("name1", Json.NULL);
                             put("name2", Json.NULL);
                             put("name3", Json.value(List.of(
                                     Json.NULL,
                                     Json.NULL,
                                     Json.value(BigDecimal.valueOf(3)))));
                         }}),
                         Json.value(new LinkedHashMap<>() {{
                             put("name1", Json.value("value1"));
                             put("name2", Json.value("value2"));
                             put("name3", Json.value(List.of(
                                     Json.value(BigDecimal.valueOf(1)),
                                     Json.value(BigDecimal.valueOf(2)),
                                     Json.value(BigDecimal.valueOf(3)))));
                         }}));
    }

    static Stream<RenderCase> renderCases() {
        return Stream.of(EnumSet.noneOf(Json.Option.class),
                         EnumSet.of(Json.Option.SKIP_NULL),
                         EnumSet.of(Json.Option.INLINE_OBJECT),
                         EnumSet.of(Json.Option.FORMAT_ARRAY),
                         EnumSet.of(Json.Option.SKIP_NULL, Json.Option.INLINE_OBJECT),
                         EnumSet.of(Json.Option.SKIP_NULL, Json.Option.FORMAT_ARRAY),
                         EnumSet.of(Json.Option.INLINE_OBJECT, Json.Option.FORMAT_ARRAY),
                         EnumSet.of(Json.Option.SKIP_NULL, Json.Option.INLINE_OBJECT, Json.Option.FORMAT_ARRAY))
                     .flatMap(RendererTest::renderCases);
    }

    private static Stream<RenderCase> renderCases(final EnumSet<Json.Option> options) {
        return jsonValues().map(value -> renderCase(options, value));
    }

    private static RenderCase renderCase(final EnumSet<Json.Option> options, final Json.Value value) {
        return new RenderCase(value, options, expected(value, options));
    }

    @SuppressWarnings("ChainOfInstanceofChecks")
    private static Json.Value expected(final Json.Value value, final Set<Json.Option> options) {
        if (value instanceof final JsonObject jsonObject && options.contains(Json.Option.SKIP_NULL)) {
            return jsonObject.value().entrySet().stream()
                             .filter(entry -> Json.NULL != entry.getValue())
                             .map(entry -> FinalEntry.of(entry.getKey(),
                                                         expected(entry.getValue(), options)))
                             .collect(JsonObject.collector());
        } else if (value instanceof final JsonArray jsonArray && options.contains(Json.Option.SKIP_NULL)) {
            return Json.value(jsonArray.value().stream()
                                          .map(elem -> expected(elem, options))
                                          .toList());
        }
        return value;
    }

    @ParameterizedTest
    @MethodSource("renderCases")
    final void render(final RenderCase given) {
        final String result = Json.render(given.source, given.options);
        // System.out.println(result);
        assertEquals(given.expected, Json.parse(result));
    }

    @SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
    record RenderCase(Json.Value source, Set<Json.Option> options, Json.Value expected) {
    }
}