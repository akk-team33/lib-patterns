package de.team33.patterns.json.charon.publics;

import de.team33.patterns.collection.mneme.FinalEntry;
import de.team33.patterns.collection.mneme.FinalMap;
import de.team33.patterns.json.charon.*;
import de.team33.patterns.streamable.galatea.Streamable;
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
                         new JsonBoolean(true),
                         new JsonBoolean(false),
                         new JsonNumber(BigDecimal.ONE),
                         new JsonNumber(BigDecimal.valueOf(278)),
                         new JsonNumber(new BigDecimal("123456789.123456789")),
                         new JsonNumber(new BigDecimal("0.123E1234")),
                         new JsonNumber(BigDecimal.valueOf(-1)),
                         new JsonNumber(BigDecimal.valueOf(0)),
                         new JsonNumber(BigDecimal.valueOf(-123.45)),
                         new JsonNumber(new BigDecimal("1E5")),
                         new JsonNumber(new BigDecimal("-1.2e-5")),
                         new JsonString(" abc "),
                         new JsonString("\\\"\b\f\n\r\t"),
                         new JsonObject(Streamable.empty()),
                         new JsonArray(List.of()),
                         new JsonArray(List.of(
                                 new JsonString("a"),
                                 new JsonBoolean(true),
                                 Json.NULL,
                                 new JsonNumber(new BigDecimal("1.23")),
                                 new JsonObject(FinalMap.of("name", new JsonString("value"))))),
                         new JsonObject(new LinkedHashMap<>() {{
                             put("name1", Json.NULL);
                             put("name2", Json.NULL);
                             put("name3", new JsonArray(List.of(
                                     Json.NULL,
                                     Json.NULL,
                                     new JsonNumber(BigDecimal.valueOf(3)))));
                         }}),
                         new JsonObject(new LinkedHashMap<>() {{
                             put("name1", new JsonString("value1"));
                             put("name2", new JsonString("value2"));
                             put("name3", new JsonArray(List.of(
                                     new JsonNumber(BigDecimal.valueOf(1)),
                                     new JsonNumber(BigDecimal.valueOf(2)),
                                     new JsonNumber(BigDecimal.valueOf(3)))));
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
            return new JsonArray(jsonArray.value().stream()
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