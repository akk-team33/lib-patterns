package de.team33.patterns.json.charon;

import de.team33.patterns.arbitrary.mimas.Generator;
import de.team33.patterns.json.charon.testing.Supply;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.EnumSet;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RendererTest {

    private static final Supply SUPPLY = new Supply();
    private static final EnumSet<RenderOption> OPTIONS = EnumSet.noneOf(RenderOption.class);

    static Stream<RenderCase> renderCases() {
        final boolean anyBoolean = SUPPLY.anyBoolean();
        final byte anyByte = SUPPLY.anyByte();
        final short anyShort = SUPPLY.anyShort();
        final int anyInt = SUPPLY.anyInt();
        final long anyLong = SUPPLY.anyLong();
        final float anyFloat = SUPPLY.anyFloat() / SUPPLY.anyFloat();
        final double anyDouble = SUPPLY.anyDouble() / SUPPLY.anyDouble();
        final BigInteger anyBigInteger = SUPPLY.anyBigInteger();
        final BigDecimal anyBigDecimal = SUPPLY.anyBigDecimal().divide(SUPPLY.anyBigDecimal(), RoundingMode.HALF_UP);
        final String anyString = SUPPLY.anyString(Generator.CHARACTERS + "\t\r\n\f\"\\");
        return Stream.of(new RenderCase(JsonValue.NULL, "null"),
                         new RenderCase(JsonBoolean.FALSE, "false"),
                         new RenderCase(JsonBoolean.TRUE, "true"),
                         new RenderCase(JsonBoolean.of(anyBoolean), Boolean.toString(anyBoolean)),
                         new RenderCase(JsonNumber.of(anyByte), Byte.toString(anyByte)),
                         new RenderCase(JsonNumber.of(anyShort), Short.toString(anyShort)),
                         new RenderCase(JsonNumber.of(anyInt), Integer.toString(anyInt)),
                         new RenderCase(JsonNumber.of(anyLong), Long.toString(anyLong)),
                         new RenderCase(JsonNumber.of(anyFloat), Float.toString(anyFloat)),
                         new RenderCase(JsonNumber.of(anyDouble), Double.toString(anyDouble)),
                         new RenderCase(JsonNumber.of(anyBigInteger), anyBigInteger.toString()),
                         new RenderCase(JsonNumber.of(anyBigDecimal), anyBigDecimal.toString()),
                         new RenderCase(JsonString.of(anyString), StringLiteral.render(anyString)),
                         new RenderCase(Stream.of(JsonBoolean.of(anyBoolean),
                                                  JsonNumber.of(anyByte),
                                                  JsonValue.NULL,
                                                  JsonNumber.of(anyLong),
                                                  JsonNumber.of(anyBigDecimal),
                                                  JsonString.of(anyString))
                                              .collect(JsonArray.COLLECTOR),
                                        "[%s, %s, %s, %s, %s, %s]".formatted(anyBoolean,
                                                                             anyByte,
                                                                             "null",
                                                                             anyLong,
                                                                             anyBigDecimal,
                                                                             StringLiteral.render(anyString))));
    }

    @ParameterizedTest
    @MethodSource("renderCases")
    final void render(final RenderCase given) {
        final var result = Renderer.render(given.value(), OPTIONS);
        assertEquals(given.expected, result);
    }

    record RenderCase(JsonValue value, String expected) {
    }
}
