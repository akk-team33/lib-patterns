package de.team33.patterns.json.charon;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
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
                         JsonObject.builder().build(),
                         Json.valueOf(List.of()),
                         Json.valueOf(List.of(
                                 Json.valueOf("a"),
                                 Json.valueOf(true),
                                 Json.NULL,
                                 Json.valueOf(new BigDecimal("1.23")),
                                 JsonObject.builder()
                                           .put("name", Json.valueOf("value"))
                                           .build())),
                         JsonObject.builder()
                                   .put("name1", Json.NULL)
                                   .put("name2", Json.NULL)
                                   .put("name3", Json.valueOf(List.of(
                                           Json.NULL,
                                           Json.NULL,
                                           Json.valueOf(BigDecimal.valueOf(3)))))
                                   .build(),
                         JsonObject.builder()
                                   .put("name1", Json.valueOf("value1"))
                                   .put("name2", Json.valueOf("value2"))
                                   .put("name3", Json.valueOf(List.of(
                                           Json.valueOf(BigDecimal.valueOf(1)),
                                           Json.valueOf(BigDecimal.valueOf(2)),
                                           Json.valueOf(BigDecimal.valueOf(3)))))
                                   .build());
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
        if (value instanceof final JsonObject object && options.contains(RenderOption.SKIP_NULL)) {
            final JsonObject.Builder builder = JsonObject.builder();
            object.stream()
                  .filter(entry -> Json.NULL != entry.value())
                  .forEach(entry -> builder.put(entry.name(), entry.value()));
            return builder.build();
        }
        return value;
    }

    @ParameterizedTest
    @MethodSource("renderCases")
    final void render(final RenderCase given) {
        final String result = Renderer.render(given.source, given.options);
        // System.out.println(result);
        assertEquals(given.expected, Parser.parse(result));
    }

    @SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
    record RenderCase(JsonValue source, Set<RenderOption> options, JsonValue expected) {
    }
}