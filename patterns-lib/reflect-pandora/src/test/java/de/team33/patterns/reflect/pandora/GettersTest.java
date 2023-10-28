package de.team33.patterns.reflect.pandora;

import de.team33.patterns.reflect.pandora.testing.BeanInterface;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("SerializableInnerClassWithNonSerializableOuterClass")
class GettersTest {

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
}