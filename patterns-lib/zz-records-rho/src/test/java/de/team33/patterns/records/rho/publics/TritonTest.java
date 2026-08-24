package de.team33.patterns.records.rho.publics;

import de.team33.patterns.records.rho.RenderOption;
import de.team33.patterns.records.rho.Triton;
import de.team33.patterns.records.rho.TritonTestBase;
import de.team33.patterns.records.rho.testing.Supply;
import de.team33.patterns.typing.proteus.Type;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import static de.team33.patterns.records.rho.RenderOption.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TritonTest extends TritonTestBase {

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
                     () -> Triton.setup(FailingA.class, null)); //.printStackTrace();
    }

    @Test
    final void setup_fail_B() {
        assertThrows(NullPointerException.class,
                     () -> Triton.setup(FailingB.class, mapping -> null)); //.printStackTrace();
    }

    @Test
    final void setup_fail_C() {
        Triton.setup(FailingC.class, mapping -> mapping);
        assertThrows(IllegalStateException.class,
                     () -> Triton.setup(FailingC.class, mapping -> mapping)); //.printStackTrace();
    }

    @Test
    final void setup_fail_D() {
        Triton.toJson(anySample());
        assertThrows(IllegalStateException.class,
                     () -> Triton.setup(Instant.class, mapping -> mapping)); //.printStackTrace();
    }

    @Test
    final void jsonRoundTrip() {
        final Sample<Sample<Class<?>>> origin = anySample();
        final String stage = Triton.toJson(origin);
        final Sample<Sample<Class<?>>> result = Triton.toRecord(new Type<>() {}, stage);
        assertEquals(origin, result);
    }

    @ParameterizedTest
    @MethodSource("options")
    final void jsonRoundTrip_withOptions(final List<RenderOption> options) {
        final Sample<Sample<Class<?>>> origin = anySample();
        final String stage = Triton.toJson(origin, options.toArray(RenderOption[]::new));
        final Sample<Sample<Class<?>>> result = Triton.toRecord(new Type<>() {}, stage);
        assertEquals(origin, result);
    }

    @Test
    final void mapRoundTrip() {
        final Sample<Sample<Class<?>>> origin = anySample();
        final Map<String, Object> stage = Triton.toMap(origin);
        final Sample<Sample<Class<?>>> result = Triton.toRecord(new Type<>() {}, stage);
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