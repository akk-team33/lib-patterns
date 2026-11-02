package de.team33.patterns.json.sinope;

import de.team33.patterns.typing.proteus.Type;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class NumerableTest {

    static Stream<Case<?>> cases() {
        return Stream.of(newCase(byte.class, true),
                         newCase(short.class, true),
                         newCase(int.class, true),
                         newCase(long.class, true),
                         newCase(float.class, true),
                         newCase(double.class, true),
                         newCase(Byte.class, true),
                         newCase(Short.class, true),
                         newCase(Integer.class, true),
                         newCase(Long.class, true),
                         newCase(Float.class, true),
                         newCase(Double.class, true),
                         newCase(BigInteger.class, true),
                         newCase(BigDecimal.class, true),
                         newCase(String.class, false));
    }

    private static <T> Case<T> newCase(final Class<T> tClass, final boolean supports) {
        return new Case<>(Type.of(tClass), supports);
    }

    @Test
    void decode() {
    }

    @Test
    void encode() {
    }

    @ParameterizedTest
    @MethodSource("cases")
    final <T> void supports(final Case<T> given) {
        assertEquals(given.supports, Numerable.supports(given.type));
    }

    record Case<T>(Type<T> type, boolean supports) {}
}