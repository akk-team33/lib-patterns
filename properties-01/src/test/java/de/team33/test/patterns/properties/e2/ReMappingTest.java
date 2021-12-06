package de.team33.test.patterns.properties.e2;

import de.team33.patterns.pooling.e1.Provider;
import de.team33.patterns.properties.e1.BiMapping;
import de.team33.patterns.properties.e1.Fields;
import de.team33.patterns.properties.e1.ReMapping;
import de.team33.test.patterns.properties.shared.AnyClass;
import de.team33.test.patterns.properties.shared.MapMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ReMappingTest {

    private static final Provider<Random> RANDOM = new Provider<>(Random::new);

    @ParameterizedTest
    @EnumSource(Case.class)
    final void map(final Case testCase) {
        // prepare ...
        final ReMapping<AnyClass> mapping = testCase.mapping.get();
        final AnyClass expected = RANDOM.get(AnyClass::new);
        final Map<String, Object> origin = testCase.toMap.apply(expected);

        // charge ...
        final AnyClass result = mapping.remap(origin).to(new AnyClass());

        // verify ...
        assertEquals(expected, result);
    }

    enum Case {

        RE_MAPPING_DECLARATIVE(() -> ReMapping.add("aLong", AnyClass::setALong)
                                              .add("aBigDecimal", AnyClass::setABigDecimal)
                                              .add("aList", AnyClass::setAList)
                                              .add("anInt", AnyClass::setAnInt)
                                              .add("aDouble", AnyClass::setADouble)
                                              .add("aString", AnyClass::setAString)
                                              .add("aDate", AnyClass::setADate)
                                              .build(),
                               any -> any.toMap(MapMode.DEEP)),

        BI_MAPPING_DECLARATIVE(() -> BiMapping.add("aLong", AnyClass::getALong, AnyClass::setALong)
                                              .add("aBigDecimal", AnyClass::getABigDecimal, AnyClass::setABigDecimal)
                                              .add("aList", AnyClass::getAList, AnyClass::setAList)
                                              .add("anInt", AnyClass::getAnInt, AnyClass::setAnInt)
                                              .add("aDouble", AnyClass::getADouble, AnyClass::setADouble)
                                              .add("aString", AnyClass::getAString, AnyClass::setAString)
                                              .add("aDate", AnyClass::getADate, AnyClass::setADate)
                                              .build(),
                               any -> any.toMap(MapMode.DEEP)),

        BI_MAPPING_BY_FIELDS_DEEP(() -> Fields.mapping(AnyClass.class, Fields.Mode.DEEP),
                                  any -> any.toMap(MapMode.PREFIXED));

        final Supplier<ReMapping<AnyClass>> mapping;
        final Function<AnyClass, Map<String, Object>> toMap;

        Case(final Supplier<ReMapping<AnyClass>> mapping, final Function<AnyClass, Map<String, Object>> toMap) {
            this.mapping = mapping;
            this.toMap = toMap;
        }
    }
}
