package de.team33.patterns.decision.leda.publics;

import de.team33.patterns.decision.leda.PreDecision;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class PreDecisionTest {

    private final PreDecision.Cascade<Input> cascade = PreDecision.basedOn(Input::isA, Input::isB, Input::isC);
    private final PreDecision<Input, Result> decision = cascade.replying(Result.values());

    @Test
    final void basedOn_replying_less() {
        try {
            final PreDecision<Input, String> decision = cascade.replying("A", "B", "C", "D", "E");
            fail("expected to fail - but was " + decision);
        } catch (final IllegalArgumentException e) {
            // as expected
            // e.printStackTrace();
            assertTrue(e.getMessage().contains("3"));
            assertTrue(e.getMessage().contains("8"));
            assertTrue(e.getMessage().contains("5"));
        }
    }

    @Test
    final void basedOn_replying_more() {
        try {
            final PreDecision<Input, String> decision = cascade.replying("A", "A", "A", "B", "C", "C", "D", "E", "E", "E", "E");
            fail("expected to fail - but was " + decision);
        } catch (final IllegalArgumentException e) {
            // as expected
            // e.printStackTrace();
            assertTrue(e.getMessage().contains("3"));
            assertTrue(e.getMessage().contains("8"));
            assertTrue(e.getMessage().contains("11"));
        }
    }

    @ParameterizedTest
    @EnumSource
    void apply(final Result given) {
        final Result result = decision.apply(new Input(given.ordinal()));
        assertEquals(given, result);
    }

    enum Result {
        CASE_000,
        CASE_001,
        CASE_010,
        CASE_011,
        CASE_100,
        CASE_101,
        CASE_110,
        CASE_111
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