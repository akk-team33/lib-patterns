package de.team33.patterns.records.triton.publics;

import de.team33.patterns.records.triton.Descriptor;
import de.team33.patterns.records.triton.RenderOption;
import de.team33.patterns.records.triton.Triton;
import de.team33.patterns.records.triton.TritonTestBase;
import de.team33.patterns.records.triton.testing.Supply;
import de.team33.patterns.typing.proteus.Type;
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
                          UUID.randomUUID());
    }

    private static GenericSample<GenericSample<Class<?>>> anyGenericSample() {
        return anyGenericSample(anyGenericSample(GenericSample.class));
    }

    private static <X> GenericSample<X> anyGenericSample(final X extra) {
        return new GenericSample<>(SUPPLY.anyString(),
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
        Triton.toJson(anyGenericSample());
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

    @Test
    final void jsonRoundTrip_Generic() {
        final GenericSample<GenericSample<Class<?>>> origin = anyGenericSample();
        final String stage = Triton.toJson(origin);
        final GenericSample<GenericSample<Class<?>>> result = Triton.toRecord(new Type<>() {}, stage);
        assertEquals(origin, result);
    }

    @Deprecated
    @Test
    final void mapRoundTrip() {
        final Sample origin = anySample();
        final Map<String, Object> stage = Triton.toMap(origin);
        final Sample result = Triton.toRecord(Sample.class, stage);
        assertEquals(origin, result);
    }

    @ParameterizedTest
    @MethodSource("options")
    final void jsonRoundTrip_withOptions(final List<RenderOption> options) {
        final GenericSample<GenericSample<Class<?>>> origin = anyGenericSample();
        final String stage = Triton.toJson(origin, options.toArray(RenderOption[]::new));
        final GenericSample<GenericSample<Class<?>>> result = Triton.toRecord(new Type<>() {}, stage);
        assertEquals(origin, result);
    }


    @Deprecated
    @Test
    final void descriptor() {
        final Descriptor<Sample> result = Triton.descriptor(Sample.class);
        assertEquals(Sample.class, result.recordType());
        assertEquals(List.of("name", "create", "uuid"), result.names());
        assertEquals(String.class, result.type("name"));
        assertEquals(Instant.class, result.type("create"));
        assertEquals(UUID.class, result.type("uuid"));
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

    private record Sample(String name, Instant create, UUID uuid) {
    }

    private record GenericSample<X>(String name, Instant create, UUID uuid, X extra) {
    }
}