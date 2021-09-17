package de.team33.test.patterns.properties.e1;

import de.team33.patterns.pooling.e1.Provider;
import de.team33.patterns.properties.e1.Properties;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static de.team33.patterns.properties.e1.Properties.Strategy.*;
import static org.junit.jupiter.api.Assertions.*;

class PropertiesTest {

    private static final Provider<Random> RANDOM = new Provider<>(Random::new);
    private Properties<AnyClass> propertiesByFieldsFlat = Properties.of(AnyClass.class).by(FIELDS_FLAT);
    private Properties<AnyClass> propertiesByFieldsDeep = Properties.of(AnyClass.class).by(FIELDS_DEEP);
    private Properties<AnyClass> propertiesByGetters = null; // Properties.of(AnyClass.class).by(PUBLIC_GETTERS);
    private Properties<AnyClass> propertiesByAccessors = null; // Properties.of(AnyClass.class).by(PUBLIC_GETTERS_AND_SETTERS);
    private List<Properties<AnyClass>> allProperties = Arrays.asList(
            propertiesByFieldsFlat, propertiesByFieldsDeep/*, propertiesByGetters, propertiesByAccessors*/);

    @Test
    void testEquals() {
        for (final Properties<AnyClass> properties : allProperties) {
            final AnyClass origin = RANDOM.get(AnyClass::new);
            assertTrue(properties.equals(origin, cloneOf(origin)));
            assertFalse(properties.equals(origin, cloneOf(origin).setAnInt(origin.getAnInt() + 1)));
            assertFalse(properties.equals(origin, cloneOf(origin).setADouble(origin.getADouble() + 1)));
            assertFalse(properties.equals(origin, cloneOf(origin).setAString(origin.getAString() + "1")));
            assertFalse(properties.equals(origin, cloneOf(origin).setADate(new Date(origin.getADate().getTime() + 1))));
        }
    }

    private static AnyClass cloneOf(final AnyClass origin) {
        return new AnyClass(origin);
    }
}