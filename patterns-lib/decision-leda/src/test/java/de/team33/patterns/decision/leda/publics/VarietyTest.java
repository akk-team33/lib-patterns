package de.team33.patterns.decision.leda.publics;

import de.team33.patterns.decision.leda.BitOrder;
import de.team33.patterns.decision.leda.Variety;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class VarietyTest {

    private final Variety.Stage<Input> stage = Variety.joined(Input::isC, Input::isB, Input::isA);
    private final Variety<Input, Result> variety = stage.replying(Result.values());

    @Test
    final void basedOn_replying_less() {
        try {
            final Variety<Input, String> decision = stage.replying("A", "B", "C", "D", "E");
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
            final Variety<Input, String> decision = stage.replying("A", "A", "A", "B", "C", "C", "D", "E", "E", "E", "E");
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
        final Result result = variety.apply(new Input(given.ordinal()));
        assertEquals(given, result);
    }

    @ParameterizedTest
    @EnumSource
    void apply_LSB_FIRST(final Result given) {
        final Result result = variety.with(BitOrder.LSB_FIRST)
                                     .apply(new Input(invert(given.ordinal())));
        assertEquals(given, result);
    }

    private int invert(final int ordinal) {
        final int[] stage = {(ordinal & 4) >> 2, (ordinal & 2), (ordinal & 1) << 2};
        return IntStream.of(stage).reduce(0, Integer::sum);
    }

    @ParameterizedTest
    @EnumSource
    void apply_MSB_FIRST(final Result given) {
        final Result result = variety.with(BitOrder.MSB_FIRST)
                                     .apply(new Input(given.ordinal()));
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