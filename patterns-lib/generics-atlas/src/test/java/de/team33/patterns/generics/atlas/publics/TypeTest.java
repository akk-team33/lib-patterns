package de.team33.patterns.generics.atlas.publics;

import de.team33.patterns.generics.atlas.Type;
import de.team33.patterns.generics.atlas.testing.ListType;
import de.team33.patterns.generics.atlas.testing.StringListType;
import de.team33.patterns.generics.atlas.testing.MapType;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.emptySet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class TypeTest {

    private static final Type<String> STRING_TYPE = Type.of(String.class);
    private static final Type<List<String>> LIST_TYPE = new Type<List<String>>() {};
    private static final Type<Map<String, List<String>>> MAP_TYPE = new Type<Map<String, List<String>>>() {};

    @Test
    void genericDerivative() {
        try {
            final Type<Map<String, List<String>>> type = new MapType<>();
            fail("expected to fail - but was " + type);
        } catch (final IllegalStateException e) {
            e.printStackTrace();
            assertTrue(e.getMessage().contains(MapType.class.getSimpleName()));
        }
    }

    @Test
    void indirectAnonymousDerivative() {
        final Type<List<String>> type = new ListType<String>(){};
        assertEquals(LIST_TYPE, type);
    }

    @Test
    void indirectDerivative() {
        final Type<List<String>> type = new StringListType(){};
        assertEquals(LIST_TYPE, type);
    }

    @Test
    final void multipleDerivation() {
        //noinspection EmptyClass
        final Type<Map<String, List<String>>> mapType = new MapType<String, List<String>>() {};
        assertEquals(MAP_TYPE, mapType);
    }

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

    @Test
    final void getSuperTypes() {
        assertEquals(new HashSet<>(Arrays.asList(
                Type.of(Object.class),
                Type.of(Serializable.class),
                new Type<Comparable<String>>() {},
                Type.of(CharSequence.class)
        )), new HashSet<>(STRING_TYPE.getSuperTypes()));
        assertEquals(emptySet(), new HashSet<>(MAP_TYPE.getSuperTypes()));
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
        assertEquals("java.lang.String", STRING_TYPE.toString());
        assertEquals("java.util.Map<java.lang.String, java.util.List<java.lang.String>>", MAP_TYPE.toString());
    }
}