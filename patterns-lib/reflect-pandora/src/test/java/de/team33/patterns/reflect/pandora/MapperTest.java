package de.team33.patterns.reflect.pandora;

import de.team33.patterns.reflect.pandora.testing.BeanClass;
import de.team33.patterns.reflect.pandora.testing.BeanInterface;
import de.team33.patterns.reflect.pandora.testing.Supply;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

class MapperTest extends Supply {

    private final Mapper<BeanInterface, BeanClass> mapper = Mapper.mapping(BeanInterface.class, BeanClass.class);

    @Test
    final void map() {
        final BeanClass origin = nextBean();
        final BeanClass target = nextBean();
        assertNotEquals(origin, target);

        mapper.map(origin, target);

        assertEquals(origin, target);
    }

    @Test
    final void toMap() {
        final BeanClass origin = nextBean();
        final Map<String, Object> expected = new TreeMap<String, Object>() {{
            put("intValue", origin.getIntValue());
            put("longValue", origin.getLongValue());
            put("stringValue", origin.getStringValue());
            put("instantValue", origin.getInstantValue());
        }};

        final Map<String, Object> result = mapper.toMap(origin);

        assertEquals(expected, result);
    }
}
