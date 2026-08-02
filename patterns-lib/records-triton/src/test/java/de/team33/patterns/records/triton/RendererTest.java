package de.team33.patterns.records.triton;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RendererTest {

    static Stream<JsonValue> renderCases() {
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

    @ParameterizedTest
    @MethodSource("renderCases")
    final void render(final JsonValue expected) {
        final String result = Renderer.render(expected);
        assertEquals(expected, Parser.parse(result));
    }
}