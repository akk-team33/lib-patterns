package de.team33.patterns.records.triton;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GeneralizerTest {

    static Stream<Case<?>> cases() {
        return Stream.of(new Case<>(true, new JsonBoolean(true)),
                         new Case<>(false, new JsonBoolean(false)),
                         new Case<>(Boolean.TRUE, new JsonBoolean(true)),
                         new Case<>(Boolean.FALSE, new JsonBoolean(false)),
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
                         new Case<>("any string", new JsonString("any string")),
                         new Case<>(new int[]{1, 2, 3}, JsonArray.builder()
                                                                 .add(new JsonNumber(new BigDecimal(1)))
                                                                 .add(new JsonNumber(new BigDecimal(2)))
                                                                 .add(new JsonNumber(new BigDecimal(3)))
                                                                 .build()),
                         new Case<>(new SampleRecord(null, null, null),
                                    JsonObject.builder()
                                              .put("name", JsonValue.NULL)
                                              .put("lValue", JsonValue.NULL)
                                              .put("eValue", JsonValue.NULL)
                                              .build()));
    }

    @Test
    final void map_fail() {
        assertThrows(IllegalArgumentException.class, () -> Generalizer.map(List.of()));
    }

    @ParameterizedTest
    @MethodSource("cases")
    final <T> void map(final Case<T> given) {
        final JsonValue result = Generalizer.map(given.source);
        assertEquals(given.expected, result);
    }

    record Case<T>(T source, JsonValue expected) {
    }
}