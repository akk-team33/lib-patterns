package de.team33.test.patterns.properties.e1;

import de.team33.patterns.pooling.e1.Provider;
import de.team33.patterns.properties.e1.BiMapping;
import de.team33.patterns.properties.e1.Fields;
import de.team33.patterns.properties.e1.Mapping;
import de.team33.test.patterns.properties.shared.AnyClass;
import de.team33.test.patterns.properties.shared.MapMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;


class MappingTest {

    private static final Provider<Random> RANDOM = new Provider<>(Random::new);

    @ParameterizedTest
    @EnumSource(Case.class)
    final void map(final Case testCase) {
        // prepare ...
        final Mapping<AnyClass> mapping = testCase.mapping.get();
        final AnyClass origin = RANDOM.get(AnyClass::new);
        final Map<String, Object> expected = testCase.expected.apply(origin);

        // charge ...
        final Map<String, Object> result = mapping.map(origin).to(new TreeMap<>());

        // verify ...
        assertEquals(expected, result);
    }

    enum Case {

        MAPPING_DECLARATIVE(() -> Mapping.add("aLong", AnyClass::getALong)
                                         .add("aBigDecimal", AnyClass::getABigDecimal)
                                         .add("aList", AnyClass::getAList)
                                         .add("anInt", AnyClass::getAnInt)
                                         .add("aDouble", AnyClass::getADouble)
                                         .add("aString", AnyClass::getAString)
                                         .add("aDate", AnyClass::getADate)
                                         .build(), //
                            template -> template.toMap(MapMode.DEEP)),

        BI_MAPPING_DECLARATIVE(() -> BiMapping.add("aLong", AnyClass::getALong, AnyClass::setALong)
                                              .add("aBigDecimal",
                                                   AnyClass::getABigDecimal,
                                                   AnyClass::setABigDecimal)
                                              .add("aList", AnyClass::getAList, AnyClass::setAList)
                                              .add("anInt", AnyClass::getAnInt, AnyClass::setAnInt)
                                              .add("aDouble", AnyClass::getADouble, AnyClass::setADouble)
                                              .add("aString", AnyClass::getAString, AnyClass::setAString)
                                              .add("aDate", AnyClass::getADate, AnyClass::setADate)
                                              .build(), //
                               template -> template.toMap(MapMode.DEEP)),

        BI_MAPPING_BY_FIELDS(() -> Fields.mapping(AnyClass.class, Fields.Mode.DEEP),
                             template -> template.toMap(MapMode.DEEP));

        final Supplier<Mapping<AnyClass>> mapping;

        final Function<AnyClass, Map<String, Object>> expected;

        Case(final Supplier<Mapping<AnyClass>> mapping, final Function<AnyClass, Map<String, Object>> expected) {
            this.mapping = mapping;
            this.expected = expected;
        }
    }
}
