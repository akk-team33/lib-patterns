package de.team33.patterns.records.triton;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RendererTest {

    private static Stream<JsonValue> jsonValues() {
        return Stream.of(JsonValue.NULL,
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
                         JsonObject.builder().build(),
                         JsonArray.builder().build(),
                         JsonArray.builder()
                                  .add(new JsonString("a"))
                                  .add(new JsonBoolean(true))
                                  .add(JsonValue.NULL)
                                  .add(new JsonNumber(new BigDecimal("1.23")))
                                  .add(JsonObject.builder()
                                                 .put("name", new JsonString("value"))
                                                 .build())
                                  .build(),
                         JsonObject.builder()
                                   .put("name1", JsonValue.NULL)
                                   .put("name2", JsonValue.NULL)
                                   .put("name3",
                                        JsonArray.builder()
                                                 .add(JsonValue.NULL)
                                                 .add(JsonValue.NULL)
                                                 .add(new JsonNumber(BigDecimal.valueOf(3)))
                                                 .build())
                                   .build(),
                         JsonObject.builder()
                                   .put("name1", new JsonString("value1"))
                                   .put("name2", new JsonString("value2"))
                                   .put("name3",
                                        JsonArray.builder()
                                                 .add(new JsonNumber(BigDecimal.valueOf(1)))
                                                 .add(new JsonNumber(BigDecimal.valueOf(2)))
                                                 .add(new JsonNumber(BigDecimal.valueOf(3)))
                                                 .build())
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
                  .filter(entry -> JsonValue.NULL != entry.value())
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