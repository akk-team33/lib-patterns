package de.team33.patterns.normal.iocaste.test;

import de.team33.patterns.normal.iocaste.Normal;
import de.team33.patterns.normal.iocaste.testing.Supply;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class NormalTest extends Supply {

    @Test
    final void of_String() {
        final String origin = nextString();
        final Normal result = Normal.of(origin);

        assertEquals(Normal.Type.SIMPLE, result.type());

        assertTrue(result.isSimple());
        assertFalse(result.isAggregate());
        assertFalse(result.isComposite());

        assertEquals(origin, result.asSimple());
        assertThrows(UnsupportedOperationException.class, result::asAggregate);
        assertThrows(UnsupportedOperationException.class, result::asComposite);
    }

    @Test
    final void of_Set() {
        final Set<Normal> origin = nextNormalSet();
        final List<Normal> expected = origin.stream()
                                            .sorted(Normal.ORDER)
                                            .collect(Collectors.toList());
        final Normal result = Normal.of(origin);

        assertEquals(Normal.Type.AGGREGATE, result.type());

        assertTrue(result.isAggregate());
        assertFalse(result.isSimple());
        assertFalse(result.isComposite());

        assertEquals(expected, result.asAggregate());
        assertThrows(UnsupportedOperationException.class, result::asSimple);
        assertThrows(UnsupportedOperationException.class, result::asComposite);
    }

    @Test
    final void of_List() {
        final List<Normal> origin = nextNormalList();
        final Normal result = Normal.of(origin);

        assertEquals(Normal.Type.AGGREGATE, result.type());

        assertTrue(result.isAggregate());
        assertFalse(result.isSimple());
        assertFalse(result.isComposite());

        assertEquals(origin, result.asAggregate());
        assertThrows(UnsupportedOperationException.class, result::asSimple);
        assertThrows(UnsupportedOperationException.class, result::asComposite);
    }

    @Test
    final void of_Map() {
        final Map<Normal, Normal> origin = nextNormalMap();
        final Normal result = Normal.of(origin);

        assertEquals(Normal.Type.COMPOSITE, result.type());

        assertTrue(result.isComposite());
        assertFalse(result.isSimple());
        assertFalse(result.isAggregate());

        assertEquals(origin, result.asComposite());
        assertThrows(UnsupportedOperationException.class, result::asSimple);
        assertThrows(UnsupportedOperationException.class, result::asAggregate);
    }

    @Test
    void type() {
    }

    @Test
    void isSimple() {
    }

    @Test
    void isAggregate() {
    }

    @Test
    void isComposite() {
    }

    @Test
    void asSimple() {
    }

    @Test
    void asAggregate() {
    }

    @Test
    void asComposite() {
    }

    @Test
    void testEquals() {
    }

    @Test
    void testHashCode() {
    }

    @Test
    void testToString() {
    }
}