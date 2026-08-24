package de.team33.patterns.records.rho;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class JsonArrayTest {

    @Test
    final void equalsAndHashCode() {
        final var left = JsonArray.builder().build();
        final var right = JsonArray.builder().build();
        assertEquals(left, right);
        assertEquals(left.hashCode(), right.hashCode());

        //noinspection EqualsWithItself
        assertEquals(left, left);
        //noinspection MisorderedAssertEqualsArguments
        assertNotEquals(left, JsonValue.NULL);
    }
}