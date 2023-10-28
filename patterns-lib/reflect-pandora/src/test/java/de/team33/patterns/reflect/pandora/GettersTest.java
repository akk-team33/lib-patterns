package de.team33.patterns.reflect.pandora;

import de.team33.patterns.reflect.pandora.testing.BeanClass;
import de.team33.patterns.reflect.pandora.testing.BeanInterface;
import de.team33.patterns.reflect.pandora.testing.Supply;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("SerializableInnerClassWithNonSerializableOuterClass")
class GettersTest {

    private static final Supply SUPPLY = new Supply();

    @Test
    final void names() {
        final Getters<BeanInterface> getters = Getters.of(BeanInterface.class);
        final Set<String> expected = new TreeSet<String>(){{
            add("intValue");
            add("longValue");
            add("stringValue");
            add("instantValue");
        }};

        final Set<String> result = getters.names();

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
    final void getter_failing() {
        final BeanInterface origin = new BeanMock();
        final Getters<BeanInterface> getters = Getters.of(BeanInterface.class);

        try {
            final Object intValue = getters.getter("intValue")
                                           .apply(origin);
            fail("expected to fail - but was <" + intValue + ">");
        } catch (final IllegalStateException e) {
            // as expected!
            e.printStackTrace();
            assertTrue(e.getMessage().contains("getIntValue"));
        }
    }

    @Test
    final void getter() {
        final BeanClass origin = SUPPLY.nextBean();
        final Getters<BeanInterface> getters = Getters.of(BeanInterface.class);
        final Map<String, Object> expected = new TreeMap<String, Object>(){{
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

    static class BeanMock implements BeanInterface {

        @Override
        public final int getIntValue() {
            throw new UnsupportedOperationException("not yet implemented");
        }

        @Override
        public final Long getLongValue() {
            throw new UnsupportedOperationException("not yet implemented");
        }

        @Override
        public final String getStringValue() {
            throw new UnsupportedOperationException("not yet implemented");
        }

        @Override
        public final Instant getInstantValue() {
            throw new UnsupportedOperationException("not yet implemented");
        }
    }
}