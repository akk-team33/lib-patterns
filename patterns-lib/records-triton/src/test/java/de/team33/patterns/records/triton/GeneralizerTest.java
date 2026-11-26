package de.team33.patterns.records.triton;

import de.team33.patterns.json.charon.Json;
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
        return Stream.of(new Case<>(true, Json.value(true)),
                         new Case<>(false, Json.value(false)),
                         new Case<>(Boolean.TRUE, Json.value(true)),
                         new Case<>(Boolean.FALSE, Json.value(false)),
                         new Case<>((byte) 16, Json.value(BigDecimal.valueOf(16))),
                         new Case<>((short) 17, Json.value(BigDecimal.valueOf(17))),
                         new Case<>(18, Json.value(BigDecimal.valueOf(18))),
                         new Case<>(19L, Json.value(BigDecimal.valueOf(19))),
                         new Case<>(1.414f, Json.value(new BigDecimal("1.414"))),
                         new Case<>(new BigInteger("97531086429630741852"),
                                    Json.value(new BigDecimal("97531086429630741852"))),
                         new Case<>(3.141592654, Json.value(new BigDecimal("3.141592654"))),
                         new Case<>(new BigDecimal("3.141592654"), Json.value(new BigDecimal("3.141592654"))),
                         new Case<>(EnumSample.V3, Json.value("V3")),
                         new Case<>('c', Json.value("c")),
                         new Case<>("any string", Json.value("any string")),
                         new Case<>(new int[]{1, 2, 3}, Stream.of(Json.value(new BigDecimal(1)),
                                                                  Json.value(new BigDecimal(2)),
                                                                  Json.value(new BigDecimal(3)))
                                                              .collect(Json.arrayCollector())),
                         new Case<>(new SampleRecord(null, null, null), Json.value(new LinkedHashMap<>() {{
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