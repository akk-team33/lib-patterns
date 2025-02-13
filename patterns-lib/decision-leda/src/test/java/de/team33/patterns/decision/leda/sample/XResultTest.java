package de.team33.patterns.decision.leda.sample;

import de.team33.patterns.decision.leda.sample.complex.XResult;
import de.team33.patterns.decision.leda.sample.straight.SResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class XResultTest {

    @Test
    final void test() {
        for (int i = -2; i <= 2; ++i) {
            for (int k = -2; k <= 2; ++k) {
                for (int n = -2; n <= 2; ++n) {
                    final Input input = new InputImpl(i, k, n);
                    final String expected = SResult.map(input).name();

                    final String result = XResult.map(input).name();

                    assertEquals(expected, result);
                }
            }
        }
    }
}
