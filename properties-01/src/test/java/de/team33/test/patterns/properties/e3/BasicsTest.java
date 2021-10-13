package de.team33.test.patterns.properties.e3;

import de.team33.patterns.pooling.e1.Provider;
import de.team33.patterns.properties.e3.Basics;
import de.team33.test.patterns.properties.shared.AnyClass;
import de.team33.test.patterns.properties.shared.MapMode;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BasicsTest {

    private static final Provider<Random> RANDOM = new Provider<>(Random::new);
    private static final Basics<AnyClass> BASICS = new Basics<>(BasicsTest::map);

    private final AnyClass origin = RANDOM.get(AnyClass::new);
    private final AnyClass other = RANDOM.get(AnyClass::new);
    private final AnyClass copy = new AnyClass(origin, true);

    private static Map<String, Object> map(final AnyClass any) {
        return any.toMap(MapMode.DEEP);
    }

    @Test
    final void testHashCode() {
        assertEquals(map(origin).hashCode(), BASICS.hashCode(origin));
    }

    @Test
    final void testEquals() {
        assertFalse(BASICS.equals(origin, other),
                    "It is extremely unlikely, but theoretically possible, that the two values are actually the " +
                            "same. If in doubt, please repeat this test.");
        assertTrue(BASICS.equals(origin, copy));
    }

    @Test
    final void testToString() {
        assertEquals(map(origin).toString(), BASICS.toString(origin));
    }

    @Test
    final void testToMap() {
        assertEquals(map(origin), BASICS.toMap(origin));
    }
}