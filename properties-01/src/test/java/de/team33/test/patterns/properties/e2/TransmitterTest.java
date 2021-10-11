package de.team33.test.patterns.properties.e2;

import de.team33.patterns.pooling.e1.Provider;
import de.team33.patterns.properties.e2.Fields;
import de.team33.patterns.properties.e2.Transmitter;
import de.team33.test.patterns.properties.shared.AnyClass;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransmitterTest {

    private static final Provider<Random> RANDOM = new Provider<>(Random::new);

    @ParameterizedTest
    @EnumSource(Case.class)
    final void transmit(final Case testCase) {
        final Transmitter<AnyClass> analyzer = testCase.transmitter.get();
        final AnyClass origin = RANDOM.get(AnyClass::new);
        final AnyClass expected = testCase.expected.apply(origin);
        final AnyClass result = analyzer.transmit(origin, new AnyClass());
        assertEquals(expected, result);
    }

    enum Case {

        FIELDS_STRAIGHT(() -> Fields.of(AnyClass.class, Fields.Mode.STRAIGHT),
                        template -> new AnyClass(template, false)),

        FIELDS_DEEP(() -> Fields.of(AnyClass.class, Fields.Mode.DEEP),
                    template -> new AnyClass(template, true));

        final Supplier<Transmitter<AnyClass>> transmitter;
        final Function<AnyClass, AnyClass> expected;

        Case(final Supplier<Transmitter<AnyClass>> transmitter, final Function<AnyClass, AnyClass> expected) {
            this.transmitter = transmitter;
            this.expected = expected;
        }
    }
}
