package de.team33.patterns.records.metis;

import de.team33.patterns.arbitrary.mimas.Generator;
import de.team33.patterns.typing.proteus.Type;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class RefractorTest {

    private static final Generator GENERATOR = Generator.of(new SecureRandom());

    private final Refractor<Sample<String, Instant>> refractor = Refractor.of(new Type<>() {});

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Test
    final void of_fail() {
        final Type wrongType = Type.of(HashMap.class);
        assertThrows(IllegalArgumentException.class, () -> Refractor.of(wrongType)); //.printStackTrace();
    }

    @Test
    final void description() {
        final Map<String, Type<?>> expected = new LinkedHashMap<>() {{
            put("index", Type.of(int.class));
            put("name", Type.of(String.class));
            put("timestamp", Type.of(Instant.class));
        }};
        final Map<String, Type<?>> description = refractor.description();
        assertEquals(expected, description);
    }

    static Stream<Case<?, ?>> cases() {
        return Stream.of(newCase(new Type<>() {}, new Sample<>(GENERATOR.anyInt(),
                                                               GENERATOR.anyString(),
                                                               anyInstant())),
                         newCase(new Type<>() {}, new Sample<>(GENERATOR.anyInt(),
                                                               anyInstant(),
                                                               GENERATOR.anyBigInteger())),
                         newCase(new Type<>() {}, new Sample<>(GENERATOR.anyInt(),
                                                               GENERATOR.anyBigInteger(),
                                                               GENERATOR.anyString())));
    }

    private static Instant anyInstant() {
        return Instant.now()
                      .plusMillis(GENERATOR.anyShort());
    }

    private static <T, U> Case<T, U> newCase(final Type<Sample<T, U>> type, final Sample<T, U> sample) {
        return new Case<>(type, sample, new LinkedHashMap<>() {{
            put("index", sample.index);
            put("name", sample.name);
            put("timestamp", sample.timestamp);
        }}, Refractor.of(type));
    }

    @ParameterizedTest
    @MethodSource("cases")
    final <T, U> void toMap(final Case<T, U> given) {
        final Map<String, Object> result = given.refractor().toMap(given.sample);
        assertEquals(given.map, result);
    }

    @ParameterizedTest
    @MethodSource("cases")
    final <T, U> void toRecord(final Case<T, U> given) {
        final Sample<T, U> result = given.refractor().toRecord(given.map);
        assertEquals(given.sample, result);
    }

    @ParameterizedTest
    @MethodSource("cases")
    final <T, U> void toRecord_mismatch(final Case<T, U> given) {
        final Map<String, Object> map = new LinkedHashMap<>() {{
            put("index", given.sample.index);
            put("name", given.sample.timestamp);
            put("timestamp", given.sample.name);
        }};
        try {
            final Sample<T, U> result = given.refractor().toRecord(map);
            fail("expected to fail - but was " + result);
        } catch (final IllegalArgumentException e) {
            // e.printStackTrace();
            // as expected
        }
    }

    private record Case<T, U>(Type<Sample<T, U>> type, Sample<T, U> sample,
                              Map<String, Object> map, Refractor<Sample<T, U>> refractor) {}

    private record Sample<T, U>(int index, T name, U timestamp) {}
}