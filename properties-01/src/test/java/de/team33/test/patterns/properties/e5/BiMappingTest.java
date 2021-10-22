package de.team33.test.patterns.properties.e5;

import de.team33.patterns.pooling.e1.Provider;
import de.team33.patterns.properties.e5.BiMapping;
import de.team33.patterns.properties.e5.Fields;
import de.team33.test.patterns.properties.shared.AnyClass;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Random;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;


class BiMappingTest
{

    private static final Provider<Random> RANDOM = new Provider<>(Random::new);

    @ParameterizedTest
    @EnumSource(Case.class)
    final void map(final Case testCase)
    {
        // prepare ...
        final BiMapping<AnyClass> mapping = testCase.mapping.get();
        final AnyClass expected = RANDOM.get(AnyClass::new);

        // charge ...
        final AnyClass result = mapping.copy(expected).to(new AnyClass());

        // verify ...
        assertEquals(expected, result);
    }

    enum Case
    {

        BI_MAPPING_DECLARATIVE(() -> BiMapping.add("aLong", AnyClass::getALong, AnyClass::setALong)
                                              .add("aBigDecimal",
                                                   AnyClass::getABigDecimal,
                                                   AnyClass::setABigDecimal)
                                              .add("aList", AnyClass::getAList, AnyClass::setAList)
                                              .add("anInt", AnyClass::getAnInt, AnyClass::setAnInt)
                                              .add("aDouble", AnyClass::getADouble, AnyClass::setADouble)
                                              .add("aString", AnyClass::getAString, AnyClass::setAString)
                                              .add("aDate", AnyClass::getADate, AnyClass::setADate)
                                              .build()),

        BI_MAPPING_BY_FIELDS(() -> Fields.mapping(AnyClass.class, Fields.Mode.DEEP));

        final Supplier<BiMapping<AnyClass>> mapping;

        Case(final Supplier<BiMapping<AnyClass>> mapping)
        {
            this.mapping = mapping;
        }
    }
}
