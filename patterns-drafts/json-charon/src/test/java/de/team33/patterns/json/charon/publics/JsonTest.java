package de.team33.patterns.json.charon.publics;

import de.team33.patterns.json.charon.Json;
import de.team33.patterns.json.charon.JsonValue;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;

class JsonTest {

    @Test
    final void valueOf_null() {
        assertThrows(NullPointerException.class,
                     () -> Json.valueOf((Boolean) null)); //.printStackTrace();
        assertThrows(NullPointerException.class,
                     () -> Json.valueOf((BigDecimal) null)); //.printStackTrace();
        assertThrows(NullPointerException.class,
                     () -> Json.valueOf((CharSequence) null)); //.printStackTrace();
        assertThrows(NullPointerException.class,
                     () -> Json.valueOf((Collection<? extends JsonValue>) null)); //.printStackTrace();
        assertThrows(NullPointerException.class,
                     () -> Json.valueOf((Map<String, ? extends JsonValue>) null)); //.printStackTrace();
    }
}