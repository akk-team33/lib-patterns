package de.team33.patterns.decision.carpo.publics;

import de.team33.patterns.decision.carpo.sample.complex.XResult;
import de.team33.patterns.decision.carpo.sample.recent.NResult;
import de.team33.patterns.decision.carpo.sample.recent.RResult;
import de.team33.patterns.decision.carpo.sample.straight.SResult;
import de.team33.patterns.decision.carpo.testing.Input;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class SampleTest {

    @Test
    final void rResult() {
        for (int i = -2; i <= 2; ++i) {
            for (int k = -2; k <= 2; ++k) {
                for (int n = -2; n <= 2; ++n) {
                    final Input input = new Input(i == 0, k == 0, n == 0);
                    final String expected = SResult.map(input).name();

                    final String result = RResult.map(input).name();

                    assertEquals(expected, result);
                }
            }
        }
    }

    @Test
    final void xResult() {
        for (int i = -2; i <= 2; ++i) {
            for (int k = -2; k <= 2; ++k) {
                for (int n = -2; n <= 2; ++n) {
                    final Input input = new Input(i == 0, k == 0, n == 0);
                    final String expected = SResult.map(input).name();

                    final String result = XResult.map(input).name();

                    assertEquals(expected, result);
                }
            }
        }
    }

    @Test
    final void nResult() {
        final Input input = new Input(true, true, false);
        try {
            final NResult result = NResult.map(input);
            fail("expected to fail - but was " + result);
        } catch (final UnsupportedOperationException e) {
            // as expected ...
            // e.printStackTrace();
            assertEquals("Case 11 is not supported", e.getMessage());
        }
    }
}
