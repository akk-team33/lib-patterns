package de.team33.test.patterns.properties.e1;

import de.team33.patterns.pooling.e1.Provider;
import de.team33.patterns.properties.e1.Properties;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.team33.patterns.properties.e1.Properties.Strategy.*;
import static org.junit.jupiter.api.Assertions.*;

class PropertiesTest {

    private static final Provider<Random> RANDOM = new Provider<>(Random::new);

    private final List<AnyClass> samples = RANDOM.get(PropertiesTest::newSamples);

    private final Properties<AnyClass> propertiesByFieldsFlat = Properties.of(AnyClass.class).by(FIELDS_FLAT);
    private final Properties<AnyClass> propertiesByFieldsDeep = Properties.of(AnyClass.class).by(FIELDS_DEEP);
    private final Properties<AnyClass> propertiesByGetters = null; // Properties.of(AnyClass.class).by(PUBLIC_GETTERS);
    private final Properties<AnyClass> propertiesByAccessors = null; // Properties.of(AnyClass.class).by(PUBLIC_GETTERS_AND_SETTERS);
    private final List<Properties<AnyClass>> allProperties = Arrays.asList(
            propertiesByFieldsFlat, propertiesByFieldsDeep/*, propertiesByGetters, propertiesByAccessors*/);


    private static List<AnyClass> newSamples(final Random random) {
        return Stream.generate(() -> new AnyClass(random))
                     .limit(100)
                     .collect(Collectors.toList());
    }

    private static AnyClass cloneOf(final AnyClass origin) {
        return new AnyClass(origin);
    }

    @Test
    void testEquals() {
        allProperties.forEach(properties -> samples.forEach(sample -> {
            assertTrue(properties.equals(sample, new AnyClass(sample)));
            samples.stream().map(AnyClass::new).forEach(other -> {
                if (properties.equals(sample, other)) {
                    assertEquals(properties.hashCode(sample), properties.hashCode(other));
                    assertEquals(properties.toString(sample), properties.toString(other));
                } else {
                    assertNotEquals(properties.hashCode(sample), properties.hashCode(other),
                                    "It is extremely unlikely, but theoretically it can happen that the hash code" +
                                            " of two different samples is the same." +
                                            " If in doubt, please repeat the test.");
                    assertNotEquals(properties.toString(sample), properties.toString(other));
                }
            });
        }));
    }
}