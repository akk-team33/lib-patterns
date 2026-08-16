package de.team33.patterns.records.triton.publics;

import de.team33.patterns.records.triton.RenderOption;
import de.team33.patterns.records.triton.Triton;
import de.team33.patterns.records.triton.TritonTestBase;
import de.team33.patterns.records.triton.testing.Supply;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import static de.team33.patterns.records.triton.RenderOption.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TritonTest extends TritonTestBase {

    private static final Supply SUPPLY = new Supply();

    private static Sample anySample() {
        return new Sample(SUPPLY.anyString(),
                          Instant.now().plusMillis(SUPPLY.anyShort()),
                          UUID.randomUUID(),
                          Sample.class);
    }

    static Stream<List<RenderOption>> options() {
        return Stream.of(List.of(),
                         List.of(FORMAT_ARRAY),
                         List.of(INLINE_OBJECT),
                         List.of(SKIP_NULL),
                         List.of(FORMAT_ARRAY, INLINE_OBJECT),
                         List.of(FORMAT_ARRAY, SKIP_NULL),
                         List.of(INLINE_OBJECT, SKIP_NULL),
                         List.of(FORMAT_ARRAY, INLINE_OBJECT, SKIP_NULL));
    }

    @Test
    final void setup_fail_null_A() {
        assertThrows(NullPointerException.class,
                     () -> Triton.setup(Integer.class, null)).printStackTrace();
    }

    @Test
    final void setup_fail_null_B() {
        assertThrows(NullPointerException.class,
                     () -> Triton.setup(Double.class, mapping -> null)).printStackTrace();
    }

    @Test
    final void setup_fail_A() {
        assertThrows(IllegalStateException.class,
                     () -> Triton.setup(Class.class, mapping -> mapping)); //.printStackTrace();
    }

    @Test
    final void setup_fail_B() {
        jsonRoundTrip();
        assertThrows(IllegalStateException.class,
                     () -> Triton.setup(Instant.class, mapping -> mapping)); //.printStackTrace();
    }

    @Test
    final void jsonRoundTrip() {
        final Sample origin = anySample();
        final String stage = Triton.toJson(origin);
        final Sample result = Triton.toRecord(Sample.class, stage);
        assertEquals(origin, result);
    }

    @ParameterizedTest
    @MethodSource("options")
    final void jsonRoundTrip_withOptions(final List<RenderOption> options) {
        final Sample origin = anySample();
        final String stage = Triton.toJson(origin, options.toArray(RenderOption[]::new));
        final Sample result = Triton.toRecord(Sample.class, stage);
        assertEquals(origin, result);
    }

    @Test
    final void mapRoundTrip() {
        final Sample origin = anySample();
        final Map<String, Object> stage = Triton.toMap(origin);
        final Sample result = Triton.toRecord(Sample.class, stage);
        assertEquals(origin, result);
    }

    private record Sample(String name, Instant create, UUID uuid, Class<?> refClass) {
    }
}