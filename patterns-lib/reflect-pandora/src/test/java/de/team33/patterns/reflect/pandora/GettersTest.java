package de.team33.patterns.reflect.pandora;

import de.team33.patterns.reflect.pandora.testing.BeanClass;
import de.team33.patterns.reflect.pandora.testing.BeanInterface;
import de.team33.patterns.reflect.pandora.testing.Supply;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static javax.swing.UIManager.put;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GettersTest {

    private static final Supply SUPPLY = new Supply();

    private final Getters<BeanClass> getters = Getters.of(BeanClass.class);

    @Test
    final void map() {
        final BeanClass origin = SUPPLY.nextBean();
        final Map<String, Object> expected = new TreeMap<String, Object>() {{
            put("intValue", origin.getIntValue());
            put("longValue", origin.getLongValue());
            put("stringValue", origin.getStringValue());
            put("instantValue", origin.getInstantValue());
        }};

        final Map<String, Object> result = getters.map(origin);

        assertEquals(expected, result);
    }
}
