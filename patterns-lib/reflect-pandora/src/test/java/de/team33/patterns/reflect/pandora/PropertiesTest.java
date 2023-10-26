package de.team33.patterns.reflect.pandora;

import de.team33.patterns.reflect.pandora.testing.BeanClass;
import de.team33.patterns.reflect.pandora.testing.Supply;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class PropertiesTest {

    private static final Supply SUPPLY = new Supply();

    @Test
    final void toMap() {
        final Properties<BeanClass> properties = Properties.of(BeanClass.class);
        final BeanClass original = SUPPLY.nextBean();
        final Map<String, Object> expected = new HashMap<String, Object>() {{
            put("intValue", original.getIntValue());
            put("longValue", original.getLongValue());
            put("stringValue", original.getStringValue());
            put("instantValue", original.getInstantValue());
        }};

        final Map<String, Object> result = properties.toMap(original);

        assertEquals(expected, result);
    }
}
