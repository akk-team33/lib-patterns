package de.team33.patterns.reflect.pandora.publics;

import de.team33.patterns.reflect.pandora.Getters;
import de.team33.patterns.reflect.pandora.Getters.Policy;
import de.team33.patterns.reflect.pandora.testing.BeanClass;
import de.team33.patterns.reflect.pandora.testing.BeanInterface;
import de.team33.patterns.reflect.pandora.testing.MixedClass;
import de.team33.patterns.reflect.pandora.testing.RecordClass;
import de.team33.patterns.reflect.pandora.testing.Supply;
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
    final void getter_RecordClass_RECORD() {
        final RecordClass origin = SUPPLY.nextRecord();
        final Getters<RecordClass> getters = Getters.of(RecordClass.class, Policy.RECORD);
        final Map<String, Object> expected = new TreeMap<String, Object>() {{
            put("intValue", origin.intValue());
            put("longValue", origin.longValue());
            put("stringValue", origin.stringValue());
            put("instantValue", origin.instantValue());
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
    final void getter_MixedClass_BROAD() {
        final MixedClass origin = SUPPLY.nextMixed();
        final Getters<MixedClass> getters = Getters.of(MixedClass.class, Policy.BROAD);
        final Map<String, Object> expected = new TreeMap<String, Object>() {{
            put("intValue", origin.intValue());
            put("longValue", origin.longValue());
            put("stringValue", origin.stringValue());
            put("instantValue", origin.instantValue());
            put("getIntValue", origin.getIntValue());
            put("getLongValue", origin.getLongValue());
            put("getStringValue", origin.getStringValue());
            put("getInstantValue", origin.getInstantValue());
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
            // e.printStackTrace();
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
