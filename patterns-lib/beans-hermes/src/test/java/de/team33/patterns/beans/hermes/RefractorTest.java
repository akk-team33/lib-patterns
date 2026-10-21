package de.team33.patterns.beans.hermes;

import de.team33.patterns.arbitrary.mimas.Generator;
import de.team33.patterns.typing.proteus.Type;
import de.team33.patterns.value.sinope.Equation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class RefractorTest {

    private static final Generator GENERATOR = new Generator.Basic();

    private final Refractor<Sample<String, Instant>> refractor = Refractor.of(new Type<>() {});

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

    @ParameterizedTest
    @MethodSource("cases")
    final <T, U> void toMap(final Case<T, U> given) {
        final Map<String, Object> result = given.refractor().toMap(given.sample);
        assertEquals(given.map, result);
    }

    @ParameterizedTest
    @MethodSource("cases")
    final <T, U> void toBean(final Case<T, U> given) {
        final Sample<T, U> result = given.refractor().toBean(given.map);
        assertEquals(given.sample, result);
    }

    @ParameterizedTest
    @MethodSource("cases")
    final <T, U> void toBean_mismatch(final Case<T, U> given) {
        final Map<String, Object> map = new LinkedHashMap<>() {{
            put("index", given.sample.index);
            put("name", given.sample.timestamp);
            put("timestamp", given.sample.name);
        }};
        try {
            final Sample<T, U> result = given.refractor().toBean(map);
            fail("expected to fail - but was " + result);
        } catch (final IllegalArgumentException e) {
            // e.printStackTrace();
            // as expected
        }
    }

    private record Case<T, U>(Type<Sample<T, U>> type, Sample<T, U> sample,
                              Map<String, Object> map, Refractor<Sample<T, U>> refractor) {}

    @SuppressWarnings("unused")
    static final class Sample<T, U> {

        @SuppressWarnings("rawtypes")
        private static final Equation<Sample> EQUATION = Equation.of(Sample.class, Sample::toList);

        private int index;
        private T name;
        private U timestamp;

        Sample() {
        }

        Sample(final int index, final T name, final U timestamp) {
            this.index = index;
            this.name = name;
            this.timestamp = timestamp;
        }

        public int getIndex() {
            return index;
        }

        public Sample<T, U> setIndex(final int index) {
            this.index = index;
            return this;
        }

        public T getName() {
            return name;
        }

        public Sample<T, U> setName(final T name) {
            this.name = name;
            return this;
        }

        public U getTimestamp() {
            return timestamp;
        }

        public Sample<T, U> setTimestamp(final U timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        private List<?> toList() {
            return List.of(index, name, timestamp);
        }

        @Override
        public final boolean equals(final Object obj) {
            return EQUATION.equals(this, obj);
        }

        @Override
        public final int hashCode() {
            return EQUATION.hashCode(this);
        }

        @Override
        public final String toString() {
            return EQUATION.toString(this);
        }
    }
}