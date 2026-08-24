package de.team33.patterns.records.rho;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonObjectTest {

    @Test
    final void builder() {
        final JsonObject object = JsonObject.builder()
                                            .put("name1", new JsonBoolean(false))
                                            .put("name2", new JsonBoolean(true))
                                            .put("name1", JsonValue.NULL)
                                            .put("name2", JsonValue.NULL)
                                            .build();
        assertEquals("name1", object.get(0).name());
        assertEquals(JsonValue.NULL, object.get(0).value());
        assertEquals("name2", object.get(1).name());
        assertEquals(JsonValue.NULL, object.get(1).value());
    }
}