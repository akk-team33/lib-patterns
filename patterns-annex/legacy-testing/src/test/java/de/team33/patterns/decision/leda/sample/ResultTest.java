package de.team33.patterns.decision.leda.sample;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResultTest {

    @Test
    final void test() {
        for (int i = -2; i <= 2; ++i) {
            for (int k = -2; k <= 2; ++k) {
                for (int n = -2; n <= 2; ++n) {
                    final Input input = new InputImpl(i, k, n);
                    final String expected =
                            de.team33.patterns.decision.leda.sample.straight.Result.map(input).name();

                    final String result =
                            de.team33.patterns.decision.leda.sample.recent.Result.map(input).name();

                    assertEquals(expected, result);
                }
            }
        }
    }
}
