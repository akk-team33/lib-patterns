package de.team33.patterns.records.rho;

import de.team33.patterns.arbitrary.mimas.Generator;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReflectorTest {

    private static final Generator GENERATOR = Generator.of(new SecureRandom());

    private final Reflector<Sample> reflector = Reflector.of(Sample.class);

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Test
    final void of_fail() {
        final Class wrongClass = HashMap.class;
        assertThrows(IllegalArgumentException.class, () -> Reflector.of(wrongClass)); //.printStackTrace();
    }

    @Test
    final void description() {
        final Map<String, Class<?>> expected = new LinkedHashMap<>() {{
            put("index", int.class);
            put("name", String.class);
            put("timestamp", Instant.class);
        }};
        final Map<String, Class<?>> description = reflector.description();
        assertEquals(expected, description);
    }

    @Test
    final void toMap() {
        final Sample sample = new Sample(GENERATOR.anyInt(),
                                         GENERATOR.anyString(),
                                         Instant.now().plusMillis(GENERATOR.anyShort()));
        final Map<String, Object> expected = new LinkedHashMap<>() {{
            put("index", sample.index);
            put("name", sample.name);
            put("timestamp", sample.timestamp);
        }};
        final Map<String, Object> result = reflector.toMap(sample);
        assertEquals(expected, result);
    }

    @Test
    final void toRecord() {
        final Sample sample = new Sample(GENERATOR.anyInt(),
                                         GENERATOR.anyString(),
                                         Instant.now().plusMillis(GENERATOR.anyShort()));
        final Map<String, Object> map = new LinkedHashMap<>() {{
            put("index", sample.index);
            put("name", sample.name);
            put("timestamp", sample.timestamp);
        }};
        final Sample result = reflector.toRecord(map);
        assertEquals(sample, result);
    }

    @Test
    final void toRecord_fail() {
        final Sample sample = new Sample(GENERATOR.anyInt(),
                                         GENERATOR.anyString(),
                                         Instant.now().plusMillis(GENERATOR.anyShort()));
        final Map<String, Object> map = new LinkedHashMap<>() {{
            put("index", sample.index);
            put("name", sample.timestamp);
            put("timestamp", sample.name);
        }};
        assertThrows(IllegalArgumentException.class, () -> reflector.toRecord(map));
    }

    private record Sample(int index, String name, Instant timestamp) {}
}