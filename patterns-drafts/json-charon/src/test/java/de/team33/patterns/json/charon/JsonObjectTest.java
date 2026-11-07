package de.team33.patterns.json.charon;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonObjectTest {

    @Test
    final void builder() {
        final JsonObject object = JsonObject.builder()
                                            .put("name1", Json.valueOf(false))
                                            .put("name2", Json.valueOf(true))
                                            .put("name1", Json.NULL)
                                            .put("name2", Json.NULL)
                                            .build();
        assertEquals("name1", object.get(0).name());
        assertEquals(Json.NULL, object.get(0).value());
        assertEquals("name2", object.get(1).name());
        assertEquals(Json.NULL, object.get(1).value());
    }
}