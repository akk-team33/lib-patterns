package de.team33.patterns.decision.carpo.publics;

import de.team33.patterns.decision.carpo.BitOrder;
import de.team33.patterns.decision.carpo.Variety;
import de.team33.patterns.decision.carpo.testing.Input;
import de.team33.patterns.decision.carpo.testing.InputImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class VarietyReplyingTest {

    private final Variety<Input> variety = Variety.joined(Input::isC, Input::isB, Input::isA);
    private final Function<Input, Result> function = variety.replying(Result.values());

    @Test
    final void joined_replying_less() {
        try {
            final Function<Input, String> decision = Variety.joined(BitOrder.LSB_FIRST,
                                                                    Input::isA, Input::isB, Input::isC)
                                                            .replying("A", "B", "C", "D", "E");
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
    final void joined_replying_more() {
        try {
            final Function<Input, String> decision =
                    variety.replying("A", "A", "A", "B", "C", "C", "D", "E", "E", "E", "E");
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
    final void apply(final Result given) {
        final Result result = function.apply(new InputImpl(given.ordinal()));
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
}