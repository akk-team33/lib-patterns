package de.team33.patterns.arbitrary.mimas.publics;

import de.team33.patterns.arbitrary.mimas.sample.FixedGenerator;
import de.team33.patterns.typing.proteus.Type;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FixedGeneratorTest extends GeneratorTestBase<FixedGenerator> {

    FixedGeneratorTest() {
        super(new FixedGenerator());
    }

    @Test
    final void anyBoolean() {
        final boolean expected = generator().anyBoolean();
        final boolean result = generator().anyBoolean();
        assertEquals(expected, result);
    }

    @Test
    final void anyByte() {
        final byte expected = generator().anyByte();
        final byte result = generator().anyByte();
        assertEquals(expected, result);
    }

    @Test
    final void anyShort() {
        final short expected = generator().anyShort();
        final short result = generator().anyShort();
        assertEquals(expected, result);
    }

    @Test
    final void anyInt() {
        final int expected = generator().anyInt();
        final int result = generator().anyInt();
        assertEquals(expected, result);
    }

    @Test
    final void anyLong() {
        final long expected = generator().anyLong();
        final long result = generator().anyLong();
        assertEquals(expected, result);
    }

    @Test
    final void anyFloat() {
        final float result = generator().anyFloat();
        assertFalse(0.0 > result);
        assertTrue(1.0 > result);
    }

    @Test
    final void anyDouble() {
        final double result = generator().anyDouble();
        assertFalse(0.0 > result);
        assertTrue(1.0 > result);
    }

    @Test
    final void anyDataSet_predictable() {
        final Map<String, Type<?>> description = new HashMap<>() {{
            put("index", Type.of(int.class));
            put("longIndex", Type.of(Long.class));
            put("name", Type.of(String.class));
            put("title", Type.of(String.class));
            put("subTitle", Type.of(CharSequence.class));
            put("missing", Type.of(Instant.class));
        }};

        final Map<String, Object> result = generator().anyDataSet(description);
        assertEquals(description.keySet(), result.keySet());

        assertEquals(generator().anyInt(), result.get("index"));
        assertEquals(generator().anyLong(), result.get("longIndex"));
        assertEquals(generator().anyName(), result.get("name"));
        assertEquals(generator().anyTitle(), result.get("title"));
        assertEquals(generator().anyCharSequence(), result.get("subTitle"));
        assertNull(result.get("missing"));
    }
}