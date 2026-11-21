package de.team33.patterns.records.triton;

import de.team33.patterns.json.charon.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GeneralizerTest {

    static Stream<Case<?>> cases() {
        return Stream.of(new Case<>(true, Json.valueOf(true)),
                         new Case<>(false, Json.valueOf(false)),
                         new Case<>(Boolean.TRUE, Json.valueOf(true)),
                         new Case<>(Boolean.FALSE, Json.valueOf(false)),
                         new Case<>((byte) 16, new JsonNumber(BigDecimal.valueOf(16))),
                         new Case<>((short) 17, new JsonNumber(BigDecimal.valueOf(17))),
                         new Case<>(18, new JsonNumber(BigDecimal.valueOf(18))),
                         new Case<>(19L, new JsonNumber(BigDecimal.valueOf(19))),
                         new Case<>(1.414f, new JsonNumber(new BigDecimal("1.414"))),
                         new Case<>(new BigInteger("97531086429630741852"),
                                    new JsonNumber(new BigDecimal("97531086429630741852"))),
                         new Case<>(3.141592654, new JsonNumber(new BigDecimal("3.141592654"))),
                         new Case<>(new BigDecimal("3.141592654"), new JsonNumber(new BigDecimal("3.141592654"))),
                         new Case<>(EnumSample.V3, new JsonString("V3")),
                         new Case<>('c', new JsonString("c")),
                         new Case<>("any string", new JsonString("any string")),
                         new Case<>(new int[]{1, 2, 3}, Stream.of(new JsonNumber(new BigDecimal(1)),
                                                                  new JsonNumber(new BigDecimal(2)),
                                                                  new JsonNumber(new BigDecimal(3)))
                                                              .collect(JsonArray.collector())),
                         new Case<>(new SampleRecord(null, null, null), new JsonObject(new LinkedHashMap<>() {{
                             put("name", Json.NULL);
                             put("lValue", Json.NULL);
                             put("eValue", Json.NULL);
                         }})));
    }

    @Test
    final void map_fail() {
        assertThrows(IllegalArgumentException.class, () -> Generalizer.map(List.of()));
    }

    @ParameterizedTest
    @MethodSource("cases")
    final <T> void map(final Case<T> given) {
        final Json.Value result = Generalizer.map(given.source);
        assertEquals(given.expected, result);
    }

    record Case<T>(T source, Json.Value expected) {
    }
}