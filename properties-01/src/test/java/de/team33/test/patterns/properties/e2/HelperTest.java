package de.team33.test.patterns.properties.e2;

import de.team33.patterns.pooling.e1.Provider;
import de.team33.patterns.properties.e2.Fields;
import de.team33.patterns.properties.e2.Helper;
import de.team33.test.patterns.properties.shared.AnyClass;
import de.team33.test.patterns.properties.shared.MapMode;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HelperTest {

    private static final Provider<Random> RANDOM = new Provider<>(Random::new);
    private static final Fields<AnyClass> ANY_FIELDS = Fields.of(AnyClass.class, Fields.Mode.DEEP);

    private final Helper<AnyClass> helper = new Helper<>(subject -> ANY_FIELDS.analyse(subject, new TreeMap<>()));
    private final AnyClass sample = RANDOM.get(AnyClass::new);
    private final AnyClass other = new AnyClass(sample, true);

    @Test
    final void testEquals() {
        assertTrue(helper.equals(sample, other));
    }

    @Test
    final void testHashCode() {
        assertEquals(sample.toMap(MapMode.PREFIXED).hashCode(), helper.hashCode(sample));
    }

    @Test
    final void testToString() {
        assertEquals(sample.toMap(MapMode.PREFIXED).toString(), helper.toString(sample));
    }

    @Test
    final void toMap() {
        assertEquals(sample.toMap(MapMode.PREFIXED), helper.toMap(sample));
    }
}
