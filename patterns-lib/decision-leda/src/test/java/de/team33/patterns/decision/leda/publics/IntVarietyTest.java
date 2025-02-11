package de.team33.patterns.decision.leda.publics;

import de.team33.patterns.decision.leda.BitOrder;
import de.team33.patterns.decision.leda.IntVariety;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IntVarietyTest {

    private final IntVariety<Input> variety = IntVariety.joined(Input::isA, Input::isB, Input::isC);

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7})
    void apply(final int given) {
        final int result = variety.apply(new Input(4 == (given & 4), 2 == (given & 2), 1 == (given & 1)));
        assertEquals(given, result);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7})
    void apply_LSB_FIRST(final int given) {
        final int result = variety.with(BitOrder.LSB_FIRST)
                                  .apply(new Input(1 == (given & 1), 2 == (given & 2), 4 == (given & 4)));
        assertEquals(given, result);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7})
    void apply_MSB_FIRST(final int given) {
        final int result = variety.with(BitOrder.MSB_FIRST)
                                  .apply(new Input(4 == (given & 4), 2 == (given & 2), 1 == (given & 1)));
        assertEquals(given, result);
    }

    static class Input {

        private final boolean a;
        private final boolean b;
        private final boolean c;

        Input(final boolean a, final boolean b, final boolean c) {
            this.a = a;
            this.b = b;
            this.c = c;
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