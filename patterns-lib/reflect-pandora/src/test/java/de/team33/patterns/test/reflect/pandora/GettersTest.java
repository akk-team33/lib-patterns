package de.team33.patterns.test.reflect.pandora;

import de.team33.patterns.reflect.pandora.Getters;
import de.team33.patterns.testing.reflect.pandora.BeanClass;
import de.team33.patterns.testing.reflect.pandora.BeanInterface;
import de.team33.patterns.testing.reflect.pandora.Supply;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@SuppressWarnings({"SerializableInnerClassWithNonSerializableOuterClass", "SerializableStoresNonSerializable"})
class GettersTest {

    private static final Supply SUPPLY = new Supply();

    @Test
    final void names() {
        final Getters<BeanInterface> getters = Getters.of(BeanInterface.class);
        final Set<String> expected = new TreeSet<String>() {{
            add("intValue");
            add("longValue");
            add("stringValue");
            add("instantValue");
        }};

        final Set<String> result = getters.names();

        assertEquals(expected, result);
    }

    @Test
    final void type() {
        final Getters<BeanInterface> getters = Getters.of(BeanInterface.class);
        final Map<String, Class<?>> expected = new TreeMap<String, Class<?>>() {{
            put("intValue", int.class);
            put("longValue", Long.class);
            put("stringValue", String.class);
            put("instantValue", Instant.class);
        }};

        final Map<String, Class<?>> result = getters.names()
                                                    .stream()
                                                    .collect(HashMap::new,
                                                             (map, name) -> map.put(name, getters.type(name)),
                                                             Map::putAll);

        assertEquals(expected, result);
    }

    @Test
    final void getter() {
        final BeanClass origin = SUPPLY.nextBean();
        final Getters<BeanInterface> getters = Getters.of(BeanInterface.class);
        final Map<String, Object> expected = new TreeMap<String, Object>() {{
            put("intValue", origin.getIntValue());
            put("longValue", origin.getLongValue());
            put("stringValue", origin.getStringValue());
            put("instantValue", origin.getInstantValue());
        }};

        final Map<String, Object> result = getters.names()
                                                  .stream()
                                                  .collect(HashMap::new,
                                                           (map, name) -> map.put(name, getters.getter(name)
                                                                                               .apply(origin)),
                                                           Map::putAll);

        assertEquals(expected, result);
    }

    @Test
    final void getter_unknown() {
        final Getters<BeanInterface> getters = Getters.of(BeanInterface.class);
        final String unknownName = SUPPLY.nextString();
        try {
            final Function<BeanInterface, Object> getter = getters.getter(unknownName);
            fail("expected to fail - but was " + getter);
        } catch (final NoSuchElementException e) {
            // as expected!
            assertTrue(e.getMessage().contains(unknownName));
        }
    }

    @Test
    final void toMap() {
        final BeanClass origin = SUPPLY.nextBean();
        final Getters<BeanInterface> getters = Getters.of(BeanInterface.class);
        final Map<String, Object> expected = new TreeMap<String, Object>() {{
            put("intValue", origin.getIntValue());
            put("longValue", origin.getLongValue());
            put("stringValue", origin.getStringValue());
            put("instantValue", origin.getInstantValue());
        }};

        final Map<String, Object> result = getters.toMap(origin);

        assertEquals(expected, result);
    }
}
