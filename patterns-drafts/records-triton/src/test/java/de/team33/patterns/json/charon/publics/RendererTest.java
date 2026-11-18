package de.team33.patterns.json.charon.publics;

import de.team33.patterns.collection.mneme.FinalEntry;
import de.team33.patterns.collection.mneme.FinalMap;
import de.team33.patterns.json.charon.*;
import de.team33.patterns.records.triton.RenderOption;
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

    private static Stream<JsonItem> jsonValues() {
        return Stream.of(JsonItem.NULL,
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
                                 JsonItem.NULL,
                                 new JsonNumber(new BigDecimal("1.23")),
                                 new JsonObject(FinalMap.of("name", new JsonString("value"))))),
                         new JsonObject(new LinkedHashMap<>() {{
                             put("name1", JsonItem.NULL);
                             put("name2", JsonItem.NULL);
                             put("name3", new JsonArray(List.of(
                                     JsonItem.NULL,
                                     JsonItem.NULL,
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
        return Stream.of(EnumSet.noneOf(RenderOption.class),
                         EnumSet.of(RenderOption.SKIP_NULL),
                         EnumSet.of(RenderOption.INLINE_OBJECT),
                         EnumSet.of(RenderOption.FORMAT_ARRAY),
                         EnumSet.of(RenderOption.SKIP_NULL, RenderOption.INLINE_OBJECT),
                         EnumSet.of(RenderOption.SKIP_NULL, RenderOption.FORMAT_ARRAY),
                         EnumSet.of(RenderOption.INLINE_OBJECT, RenderOption.FORMAT_ARRAY),
                         EnumSet.of(RenderOption.SKIP_NULL, RenderOption.INLINE_OBJECT, RenderOption.FORMAT_ARRAY))
                     .flatMap(RendererTest::renderCases);
    }

    private static Stream<RenderCase> renderCases(final EnumSet<RenderOption> options) {
        return jsonValues().map(value -> renderCase(options, value));
    }

    private static RenderCase renderCase(final EnumSet<RenderOption> options, final JsonItem value) {
        return new RenderCase(value, options, expected(value, options));
    }

    @SuppressWarnings("ChainOfInstanceofChecks")
    private static JsonItem expected(final JsonItem value, final Set<RenderOption> options) {
        if (value instanceof final JsonObject jsonObject && options.contains(RenderOption.SKIP_NULL)) {
            return jsonObject.value().entrySet().stream()
                             .filter(entry -> JsonItem.NULL != entry.getValue())
                             .map(entry -> FinalEntry.of(entry.getKey(),
                                                         expected(entry.getValue(), options)))
                             .collect(JsonObject.collector());
        } else if (value instanceof final JsonArray jsonArray && options.contains(RenderOption.SKIP_NULL)) {
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
    record RenderCase(JsonItem source, Set<RenderOption> options, JsonItem expected) {
    }
}