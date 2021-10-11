package de.team33.test.patterns.properties.e2;

import de.team33.patterns.pooling.e1.Provider;
import de.team33.patterns.properties.e2.Analyzer;
import de.team33.patterns.properties.e2.Fields;
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

class AnalyzerTest {

    private static final Provider<Random> RANDOM = new Provider<>(Random::new);

    @ParameterizedTest
    @EnumSource(Case.class)
    final void analyse(final Case testCase) {
        final Analyzer<AnyClass> analyzer = testCase.analyzer.get();
        final AnyClass origin = RANDOM.get(AnyClass::new);
        final Map<String, Object> expected = testCase.expected.apply(origin);
        final Map<String, Object> result = analyzer.analyse(origin, new TreeMap<>());
        assertEquals(expected, result);
    }

    enum Case {

        FIELDS_STRAIGHT(() -> Fields.of(AnyClass.class, Fields.Mode.STRAIGHT),
                        template -> template.toMap(MapMode.STRAIGHT)),

        FIELDS_DEEP(() -> Fields.of(AnyClass.class, Fields.Mode.DEEP),
                    template -> template.toMap(MapMode.PREFIXED));

        final Supplier<Analyzer<AnyClass>> analyzer;
        final Function<AnyClass, Map<String, Object>> expected;

        Case(final Supplier<Analyzer<AnyClass>> analyzer, final Function<AnyClass, Map<String, Object>> expected) {
            this.analyzer = analyzer;
            this.expected = expected;
        }
    }
}
