package de.team33.patterns.json.charon.publics;

import de.team33.patterns.json.charon.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class JsonTest {

    @Test
    final void valueOf_null() {
        assertThrows(NullPointerException.class, () -> new JsonBoolean(null)); //.printStackTrace();
        assertThrows(NullPointerException.class, () -> new JsonNumber(null)); //.printStackTrace();
        assertThrows(NullPointerException.class, () -> new JsonString(null)); //.printStackTrace();
        assertThrows(NullPointerException.class, () -> new JsonArray(null)); //.printStackTrace();
        assertThrows(NullPointerException.class, () -> new JsonObject(null)); //.printStackTrace();
    }
}