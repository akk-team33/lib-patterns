package de.team33.patterns.decision.carpo.publics;

import de.team33.patterns.decision.carpo.sample.recent.FResult;
import de.team33.patterns.decision.carpo.sample.recent.NResult;
import de.team33.patterns.decision.carpo.sample.recent.XResult;
import de.team33.patterns.decision.carpo.sample.straight.SResult;
import de.team33.patterns.decision.carpo.testing.Input;
import de.team33.patterns.decision.carpo.testing.InputImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@SuppressWarnings("MethodWithMultipleLoops")
class SampleTest {

    @Test
    final void fResult() {
        for (int i = -2; i <= 2; ++i) {
            for (int k = -2; k <= 2; ++k) {
                for (int n = -2; n <= 2; ++n) {
                    final Input input = new InputImpl(i == 0, k == 0, n == 0);
                    final String expected = SResult.map(input).name();

                    final String result = FResult.map(input).name();

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
                    final Input input = new InputImpl(i == 0, k == 0, n == 0);
                    final String expected = SResult.map(input).name();

                    final String result = XResult.map(input).name();

                    assertEquals(expected, result);
                }
            }
        }
    }

    @Test
    final void nResult() {
        final Input input = new InputImpl(true, true, false);
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
