package de.team33.patterns.test.reflect.pandora;

import de.team33.patterns.reflect.pandora.Getters;
import de.team33.patterns.reflect.pandora.Setters;
import de.team33.patterns.testing.reflect.pandora.BeanClass;
import de.team33.patterns.testing.reflect.pandora.Supply;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class SettersTest {

    private static final Supply SUPPLY = new Supply();

    @Test
    void names() {
        final Setters<BeanClass> setters = Setters.of(BeanClass.class);
        final Set<String> expected = new TreeSet<String>() {{
            add("intValue");
            add("longValue");
            add("stringValue");
            add("instantValue");
        }};

        final Set<String> result = setters.names();

        assertEquals(expected, result);
    }

    @Test
    final void setter() {
        final Getters<BeanClass> getters = Getters.of(BeanClass.class);
        final Setters<BeanClass> setters = Setters.of(BeanClass.class);
        final BeanClass origin = SUPPLY.nextBean();
        final BeanClass target = SUPPLY.nextBean();
        assertNotEquals(origin, target);

        setters.names()
               .forEach(name -> setters.setter(name, getters.type(name))
                                       .accept(target, getters.getter(name)
                                                              .apply(origin)));

        assertEquals(origin, target);
    }

    @Test
    final void setter_unknown_name() {
        final Setters<BeanClass> setters = Setters.of(BeanClass.class);
        final String unknownName = SUPPLY.nextString();
        try {
            final BiConsumer<BeanClass, Object> setter = setters.setter(unknownName, Object.class);
            fail("expected to fail - but was " + setter);
        } catch (final NoSuchElementException e) {
            // as expected!
            assertTrue(e.getMessage().contains(unknownName));
        }
    }

    @Test
    final void setter_unknown_type() {
        final Setters<BeanClass> setters = Setters.of(BeanClass.class);
        try {
            final BiConsumer<BeanClass, Object> setter = setters.setter("intValue", String.class);
            fail("expected to fail - but was " + setter);
        } catch (final NoSuchElementException e) {
            // as expected!
            assertTrue(e.getMessage().contains(String.class.getCanonicalName()));
        }
    }
}
