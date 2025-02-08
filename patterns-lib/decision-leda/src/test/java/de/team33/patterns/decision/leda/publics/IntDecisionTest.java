package de.team33.patterns.decision.leda.publics;

import de.team33.patterns.decision.leda.IntDecision;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IntDecisionTest {

    private final IntDecision<Input> decision = IntDecision.basedOn(Input::isA, Input::isB, Input::isC);

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7})
    void apply(final int given) {
        final int result = decision.apply(new Input(given));
        assertEquals(given, result);
    }

    static class Input {

        private final boolean a;
        private final boolean b;
        private final boolean c;

        Input(final int expectation) {
            this.a = (1 == (expectation & 1));
            this.b = (2 == (expectation & 2));
            this.c = (4 == (expectation & 4));
        }

        boolean isA() {
            return a;
        }

        boolean isB() {
            return b;
        }

        boolean isC() {
            return c;
        }
    }
}