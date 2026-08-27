package de.team33.patterns.records.rho;

import de.team33.patterns.arbitrary.mimas.Generator;
import de.team33.patterns.typing.proteus.Type;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RefractorTest {

    private static final Generator GENERATOR = Generator.of(new SecureRandom());

    private final Refractor<Sample<Instant>> refractor = Refractor.of(new Type<>() {});

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

    @Test
    final void toMap() {
        final Sample<Instant> sample = new Sample<>(GENERATOR.anyInt(),
                                                    GENERATOR.anyString(),
                                                    Instant.now().plusMillis(GENERATOR.anyShort()));
        final Map<String, Object> expected = new LinkedHashMap<>() {{
            put("index", sample.index);
            put("name", sample.name);
            put("timestamp", sample.timestamp);
        }};
        final Map<String, Object> result = refractor.toMap(sample);
        assertEquals(expected, result);
    }

    @Test
    final void toRecord() {
        final Sample<Instant> sample = new Sample<>(GENERATOR.anyInt(),
                                                    GENERATOR.anyString(),
                                                    Instant.now().plusMillis(GENERATOR.anyShort()));
        final Map<String, Object> map = new LinkedHashMap<>() {{
            put("index", sample.index);
            put("name", sample.name);
            put("timestamp", sample.timestamp);
        }};
        final Sample<Instant> result = refractor.toRecord(map);
        assertEquals(sample, result);
    }

    @Test
    final void toRecord_fail() {
        final Sample<Instant> sample = new Sample<>(GENERATOR.anyInt(),
                                                    GENERATOR.anyString(),
                                                    Instant.now().plusMillis(GENERATOR.anyShort()));
        final Map<String, Object> map = new LinkedHashMap<>() {{
            put("index", sample.index);
            put("name", sample.timestamp);
            put("timestamp", sample.name);
        }};
        assertThrows(IllegalArgumentException.class, () -> refractor.toRecord(map));
    }

    private record Sample<T>(int index, String name, T timestamp) {}
}