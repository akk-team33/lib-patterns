package de.team33.test.patterns.properties.e1a;

import de.team33.patterns.pooling.e1.Provider;
import de.team33.patterns.properties.e1a.Properties;
import de.team33.test.patterns.properties.e1.AnyClass;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class PropertiesTest {

    private static final Logger LOG = Logger.getLogger(PropertiesTest.class.getCanonicalName());
    private static final Provider<Random> RANDOM = new Provider<>(Random::new);

    @SuppressWarnings({"SwitchStatement", "fallthrough"})
    private static Map<String, Object> expectedMap(final AnyClass sample, final Properties.Mode mode) {
        final Map<String, Object> result = new TreeMap<>();
        switch (mode) {
        case BY_PUBLIC_GETTERS:
        case BY_PUBLIC_ACCESSORS:
            result.put("aLong", sample.getALong());
            result.put("aBigDecimal", sample.getABigDecimal());
            result.put("aList", sample.getAList());
            break;
        case BY_FIELDS_DEEP:
            result.put(".aLong", sample.getALong());
            result.put(".aBigDecimal", sample.getABigDecimal());
            result.put(".aList", sample.getAList());
            break;
        }
        result.put("anInt", sample.getAnInt());
        result.put("aDouble", sample.getADouble());
        result.put("aString", sample.getAString());
        result.put("aDate", sample.getADate());
        return result;
    }

    @Test
    final void toMap() {
        for (final Properties.Mode mode : Properties.Mode.values()) {
            LOG.info(() -> "Properties.Mode: " + mode);
            final Properties<AnyClass> properties = Properties.of(AnyClass.class, mode);
            final AnyClass sample = RANDOM.get(AnyClass::new);
            final Map<String, Object> expected = expectedMap(sample, mode);
            final Map<String, Object> result = properties.toMap(sample);
            assertEquals(expected, result);
        }
    }
}
