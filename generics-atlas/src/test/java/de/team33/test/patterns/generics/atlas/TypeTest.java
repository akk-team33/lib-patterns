package de.team33.test.patterns.generics.atlas;

import de.team33.patterns.generics.atlas.Type;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TypeTest {

    private static final Type<String> STRING_TYPE = Type.of(String.class);
    private static final Type<Map<String, List<String>>> MAP_TYPE = new Type<Map<String, List<String>>>() {};

    @Test
    final void asClass() {
        assertSame(String.class, STRING_TYPE.asClass());
        assertSame(Map.class, MAP_TYPE.asClass());
    }

    @Test
    final void getFormalParameters() {
        assertEquals(Collections.emptyList(), STRING_TYPE.getFormalParameters());
        assertEquals(Arrays.asList("K", "V"), MAP_TYPE.getFormalParameters());
    }

    @Test
    final void getSuperType() {
        assertEquals(Optional.of(Type.of(Object.class)), STRING_TYPE.getSuperType());
        assertEquals(Optional.empty(), MAP_TYPE.getSuperType());
    }

    @SuppressWarnings("AnonymousInnerClassMayBeStatic")
    @Test
    final void testEquals() {
        assertEquals(STRING_TYPE, new Type<String>() {});
        assertEquals(MAP_TYPE, new Type<Map<String, List<String>>>() {});
    }

    @SuppressWarnings("AnonymousInnerClassMayBeStatic")
    @Test
    final void testHashCode() {
        assertEquals(STRING_TYPE.hashCode(), new Type<String>() {}.hashCode());
        assertEquals(MAP_TYPE.hashCode(), new Type<Map<String, List<String>>>() {}.hashCode());
    }

    @Test
    final void testToString() {
        assertEquals("String", STRING_TYPE.toString());
        assertEquals("Map<String, List<String>>", MAP_TYPE.toString());
    }
}
