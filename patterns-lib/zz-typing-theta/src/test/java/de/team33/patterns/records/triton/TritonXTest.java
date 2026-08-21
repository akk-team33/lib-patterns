package de.team33.patterns.records.triton;

import de.team33.patterns.records.triton.testing.Supply;
import de.team33.patterns.typing.theta.Type;
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

class TritonXTest extends TritonTestBase {

    private static final Supply SUPPLY = new Supply();

    private static Sample<Sample<Class<?>>> anySample() {
        return anySample(anySample(Sample.class));
    }

    private static <X> Sample<X> anySample(final X extra) {
        return new Sample<>(SUPPLY.anyString(),
                            Instant.now().plusMillis(SUPPLY.anyShort()),
                            UUID.randomUUID(),
                            extra);
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
    final void setup_fail_A() {
        assertThrows(NullPointerException.class,
                     () -> TritonX.setup(FailingA.class, null)); //.printStackTrace();
    }

    @Test
    final void setup_fail_B() {
        assertThrows(NullPointerException.class,
                     () -> TritonX.setup(FailingB.class, mapping -> null)); //.printStackTrace();
    }

    @Test
    final void setup_fail_C() {
        TritonX.setup(FailingC.class, mapping -> mapping);
        assertThrows(IllegalStateException.class,
                     () -> TritonX.setup(FailingC.class, mapping -> mapping)); //.printStackTrace();
    }

    @Test
    final void setup_fail_D() {
        TritonX.toJson(anySample());
        assertThrows(IllegalStateException.class,
                     () -> TritonX.setup(Instant.class, mapping -> mapping)); //.printStackTrace();
    }

    @Test
    final void jsonRoundTrip() {
        final Sample<Sample<Class<?>>> origin = anySample();
        final String stage = TritonX.toJson(origin);
        final Sample<Sample<Class<?>>> result = TritonX.toRecord(new Type<>() {}, stage);
        assertEquals(origin, result);
    }

    @ParameterizedTest
    @MethodSource("options")
    final void jsonRoundTrip_withOptions(final List<RenderOption> options) {
        final Sample<Sample<Class<?>>> origin = anySample();
        final String stage = TritonX.toJson(origin, options.toArray(RenderOption[]::new));
        final Sample<Sample<Class<?>>> result = TritonX.toRecord(new Type<>() {}, stage);
        assertEquals(origin, result);
    }

    @Test
    final void mapRoundTrip() {
        final Sample<Sample<Class<?>>> origin = anySample();
        final Map<String, Object> stage = TritonX.toMap(origin);
        final Sample<Sample<Class<?>>> result = TritonX.toRecord(new Type<>() {}, stage);
        assertEquals(origin, result);
    }

    @SuppressWarnings({"EmptyClass", "WeakerAccess"})
    static class FailingA {
    }

    @SuppressWarnings({"EmptyClass", "WeakerAccess"})
    static class FailingB {
    }

    @SuppressWarnings({"EmptyClass", "WeakerAccess"})
    static class FailingC {
    }

    private record Sample<X>(String name, Instant create, UUID uuid, X extra) {
    }
}