package de.team33.patterns.json.charon.publics;

import de.team33.patterns.collection.mneme.FinalEntry;
import de.team33.patterns.collection.mneme.FinalMap;
import de.team33.patterns.json.charon.Json;
import de.team33.patterns.json.charon.JsonType;
import de.team33.patterns.json.charon.JsonValue;
import de.team33.patterns.json.charon.RenderOption;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RendererTest {

    private static Stream<JsonValue> jsonValues() {
        return Stream.of(Json.NULL,
                         Json.valueOf(true),
                         Json.valueOf(false),
                         Json.valueOf(BigDecimal.ONE),
                         Json.valueOf(BigDecimal.valueOf(278)),
                         Json.valueOf(new BigDecimal("123456789.123456789")),
                         Json.valueOf(new BigDecimal("0.123E1234")),
                         Json.valueOf(BigDecimal.valueOf(-1)),
                         Json.valueOf(BigDecimal.valueOf(0)),
                         Json.valueOf(BigDecimal.valueOf(-123.45)),
                         Json.valueOf(new BigDecimal("1E5")),
                         Json.valueOf(new BigDecimal("-1.2e-5")),
                         Json.valueOf(" abc "),
                         Json.valueOf("\\\"\b\f\n\r\t"),
                         Json.valueOf(Map.of()),
                         Json.valueOf(List.of()),
                         Json.valueOf(List.of(
                                 Json.valueOf("a"),
                                 Json.valueOf(true),
                                 Json.NULL,
                                 Json.valueOf(new BigDecimal("1.23")),
                                 Json.valueOf(Map.of("name", Json.valueOf("value"))),
                                 Json.valueOf(new LinkedHashMap<>() {{
                                     put("name1", Json.NULL);
                                     put("name2", Json.NULL);
                                     put("name3", Json.valueOf(List.of(
                                             Json.NULL,
                                             Json.NULL,
                                             Json.valueOf(BigDecimal.valueOf(3)))));
                                 }}),
                                 Json.valueOf(new LinkedHashMap<>() {{
                                     put("name1", Json.valueOf("value1"));
                                     put("name2", Json.valueOf("value2"));
                                     put("name3", Json.valueOf(List.of(
                                             Json.valueOf(BigDecimal.valueOf(1)),
                                             Json.valueOf(BigDecimal.valueOf(1)),
                                             Json.valueOf(BigDecimal.valueOf(3)))));
                                 }}))));
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

    private static RenderCase renderCase(final EnumSet<RenderOption> options, final JsonValue value) {
        return new RenderCase(value, options, expected(value, options));
    }

    private static JsonValue expected(final JsonValue value, final Set<RenderOption> options) {
        if (JsonType.OBJECT == value.type() && options.contains(RenderOption.SKIP_NULL)) {
            return Json.valueOf(value.asObject().entrySet().stream()
                                     .filter(entry -> Json.NULL != entry.getValue())
                                     .map(entry -> FinalEntry.of(entry.getKey(), expected(entry.getValue(), options)))
                                     .collect(FinalMap.collector()));
        } else if (JsonType.ARRAY == value.type() && options.contains(RenderOption.SKIP_NULL)) {
            return Json.valueOf(value.asArray().stream()
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
    record RenderCase(JsonValue source, Set<RenderOption> options, JsonValue expected) {}
}