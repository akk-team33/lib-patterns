package de.team33.test.patterns.properties.e1;

import de.team33.patterns.pooling.e1.Provider;
import de.team33.patterns.properties.e1.Properties;
import de.team33.test.patterns.properties.shared.AnyBaseClass;
import de.team33.test.patterns.properties.shared.AnyClass;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PropertiesTest {

    private static final Provider<Random> RANDOM = new Provider<>(Random::new);

    @SuppressWarnings("SwitchStatement")
    private static Map<String, Object> expectedMap(final AnyClass sample, final Properties.Mode mode) {
        final Map<String, Object> result = new TreeMap<>();
        switch (mode) {
//        case BY_PUBLIC_GETTERS:
//        case BY_PUBLIC_ACCESSORS:
//            result.put("aLong", sample.getALong());
//            result.put("aBigDecimal", sample.getABigDecimal());
//            result.put("aList", sample.getAList());
//            break;
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

    @ParameterizedTest
    @EnumSource(Case.class)
    final void toMap(final Case c) {
        final AnyClass sample = RANDOM.get(AnyClass::new);
        // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
        final Map<String, Object> result = c.properties.toMap(sample);
        // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
        assertEquals(c.expectedMap.apply(sample), result);
    }

    @ParameterizedTest
    @EnumSource(Case.class)
    final void map(final Case c) {
        final AnyClass sample = RANDOM.get(AnyClass::new);
        // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
        final Map<String, Object> stage = c.properties.map(sample, new TreeMap<>());
        final AnyClass result = c.properties.map(stage, new AnyClass());
        // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
        assertEquals(c.expectedMap.apply(sample), stage);
        assertEquals(c.expected.apply(sample), result);
    }

    @ParameterizedTest
    @EnumSource(Case.class)
    final void copy(final Case c) {
        final AnyClass sample = RANDOM.get(AnyClass::new);
        // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
        final AnyClass result = c.properties.copy(sample, new AnyClass());
        // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
        assertEquals(c.expected.apply(sample), result);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @ParameterizedTest
    @EnumSource(Case.class)
    final void failRead(final Case c) {
        final AnyBaseClass sample = RANDOM.get(AnyBaseClass::new);
        final Properties rawProperties = c.properties;
        //rawProperties.toMap(sample);
        assertThrows(IllegalArgumentException.class, () -> rawProperties.toMap(sample));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @ParameterizedTest
    @EnumSource(Case.class)
    final void failWrite(final Case c) {
        final AnyClass sample = RANDOM.get(AnyClass::new);
        final Properties rawProperties = c.properties;
        //rawProperties.copy(sample, new AnyBaseClass());
        assertThrows(IllegalArgumentException.class, () -> rawProperties.copy(sample, new AnyBaseClass()));
    }

    @SuppressWarnings("PackageVisibleField")
    enum Case {

        REFLECTIVE_BY_FIELDS_FLAT(Properties.of(AnyClass.class, Properties.Mode.BY_FIELDS_FLAT),
                                  sample -> new AnyClass(sample, false),
                                  sample -> expectedMap(sample, Properties.Mode.BY_FIELDS_FLAT),
                                  false),

        REFLECTIVE_BY_FIELDS_DEEP(Properties.of(AnyClass.class, Properties.Mode.BY_FIELDS_DEEP),
                                  sample -> new AnyClass(sample, true),
                                  sample -> expectedMap(sample, Properties.Mode.BY_FIELDS_DEEP),
                                  false);

        final Properties<AnyClass> properties;
        final Function<AnyClass, AnyClass> expected;
        final Function<AnyClass, Map<String, Object>> expectedMap;
        final boolean readOnly;

        Case(final Properties<AnyClass> properties,
             final Function<AnyClass, AnyClass> expected,
             final Function<AnyClass, Map<String, Object>> expectedMap,
             final boolean readOnly) {
            this.properties = properties;
            this.expected = expected;
            this.expectedMap = expectedMap;
            this.readOnly = readOnly;
        }
    }
}
