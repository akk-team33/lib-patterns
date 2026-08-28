package de.team33.patterns.records.metis.publics;

import de.team33.patterns.arbitrary.mimas.Generator;
import de.team33.patterns.records.metis.Records;
import de.team33.patterns.typing.proteus.Type;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RecordsTest {

    private static final Generator GENERATOR = Generator.of(new SecureRandom());

    private static Sample anySample() {
        return new Sample(GENERATOR.anyInt(),
                          GENERATOR.anyString(),
                          Instant.now().plusMillis(GENERATOR.anyShort()));
    }

    @Test
    final void description_byClass() {
        final Map<String, Class<?>> expected = new LinkedHashMap<>() {{
            put("index", int.class);
            put("name", String.class);
            put("timestamp", Instant.class);
        }};
        final Map<String, Class<?>> description = Records.description(Sample.class);
        assertEquals(expected, description);
    }

    @Test
    final void description_byType() {
        final Map<String, Type<?>> expected = new LinkedHashMap<>() {{
            put("index", Type.of(int.class));
            put("name", Type.of(String.class));
            put("timestamp", Type.of(Instant.class));
        }};
        final Map<String, Type<?>> description = Records.description(Type.of(Sample.class));
        assertEquals(expected, description);
    }

    @Test
    final void toMap() {
        final Sample sample = anySample();
        final Map<String, Object> expected = new LinkedHashMap<>() {{
            put("index", sample.index);
            put("name", sample.name);
            put("timestamp", sample.timestamp);
        }};
        final Map<String, Object> result = Records.toMap(sample);
        assertEquals(expected, result);
    }

    @Test
    final void toRecord_byClass() {
        final Sample sample = anySample();
        final Map<String, Object> map = new LinkedHashMap<>() {{
            put("index", sample.index);
            put("name", sample.name);
            put("timestamp", sample.timestamp);
        }};
        final Sample result = Records.toRecord(Sample.class, map);
        assertEquals(sample, result);
    }

    @Test
    final void toRecord_byType() {
        final Sample sample = anySample();
        final Map<String, Object> map = new LinkedHashMap<>() {{
            put("index", sample.index);
            put("name", sample.name);
            put("timestamp", sample.timestamp);
        }};
        final Sample result = Records.toRecord(Type.of(Sample.class), map);
        assertEquals(sample, result);
    }

    @Test
    final void toRecord_mismatch() {
        final Sample sample = anySample();
        final Map<String, Object> source = new LinkedHashMap<>() {{
            put("index", sample.index);
            put("name", sample.timestamp);
            put("timestamp", sample.name);
        }};
        assertThrows(IllegalArgumentException.class, () -> Records.toRecord(Sample.class, source));
    }

    private record Sample(int index, String name, Instant timestamp) {}
}