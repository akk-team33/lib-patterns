package de.team33.test.patterns.properties.e2;

import de.team33.patterns.pooling.e1.Provider;
import de.team33.patterns.properties.e2.Fields;
import de.team33.patterns.properties.e2.Synthesizer;
import de.team33.test.patterns.properties.shared.AnyClass;
import de.team33.test.patterns.properties.shared.MapMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SynthesizerTest {

    private static final Provider<Random> RANDOM = new Provider<>(Random::new);

    @ParameterizedTest
    @EnumSource(Case.class)
    final void synthesize(final Case testCase) {
        final Synthesizer<AnyClass> subject = testCase.synthesizer.get();
        final AnyClass template = RANDOM.get(AnyClass::new);
        final Map<String, Object> origin = testCase.origin.apply(template);
        final AnyClass expected = testCase.expected.apply(template);
        // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
        final AnyClass result = subject.synthesize(origin, new AnyClass());
        // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
        assertEquals(expected, result);
    }

    enum Case {

        FIELDS_STRAIGHT(() -> Fields.of(AnyClass.class, Fields.Mode.STRAIGHT),
                        template -> template.toMap(MapMode.PREFIXED),
                        template -> new AnyClass(template, false)),

        FIELDS_DEEP(() -> Fields.of(AnyClass.class, Fields.Mode.DEEP),
                    template -> template.toMap(MapMode.PREFIXED),
                    template -> new AnyClass(template, true));

        private final Supplier<Synthesizer<AnyClass>> synthesizer;
        private final Function<AnyClass, Map<String, Object>> origin;
        private final Function<AnyClass, AnyClass> expected;

        Case(final Supplier<Synthesizer<AnyClass>> synthesizer,
             final Function<AnyClass, Map<String, Object>> origin,
             final Function<AnyClass, AnyClass> expected) {
            this.synthesizer = synthesizer;
            this.origin = origin;
            this.expected = expected;
        }
    }
}
