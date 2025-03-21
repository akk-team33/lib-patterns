package de.team33.patterns.decision.carpo.publics;

import de.team33.patterns.decision.carpo.BitOrder;
import de.team33.patterns.decision.carpo.Variety;
import de.team33.patterns.decision.carpo.testing.Input;
import de.team33.patterns.decision.carpo.testing.InputImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class VarietyTest {

    private static final Random RANDOM = new SecureRandom();
    private static final Predicate<Input> P = i -> true;
    private static final List<Predicate<Input>> P_EMPTY = Collections.emptyList();

    private final Variety<Input> variety = Variety.joined(Input::isA, Input::isB, Input::isC);

    @Test
    final void joined_max() {
        final int result = Variety.joined(Stream.generate(() -> P)
                                                .limit(Integer.SIZE)
                                                .toList())
                                  .apply(new InputImpl(RANDOM.nextInt(0, 7)));
        assertEquals(-1, result);
    }

    @Test
    final void joined_max_plus() {
        try {
            final int result = Variety.joined(Stream.generate(() -> P)
                                                    .limit(Integer.SIZE + 1)
                                                    .toList())
                                      .apply(new InputImpl(0));
            fail("expected to fail - but apply() returns " + result);
        } catch (final IllegalArgumentException e) {
            // as expected
            // e.printStackTrace();
            assertEquals("Max. 32 criteria can be handled - but 33 are given.", e.getMessage());
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7})
    final void joined_empty_apply(final int given) {
        final int result = Variety.joined(P_EMPTY)
                                  .apply(new InputImpl(4 == (given & 4), 2 == (given & 2), 1 == (given & 1)));
        assertEquals(0, result);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7})
    final void joined_one_apply(final int given) {
        final int result = Variety.joined(BitOrder.LSB_FIRST, Input::isA)
                                  .apply(new InputImpl(4 == (given & 4), 2 == (given & 2), 1 == (given & 1)));
        assertEquals((0 == (given & 4)) ? 0 : 1, result);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7})
    final void apply(final int given) {
        final int result = variety.apply(new InputImpl(4 == (given & 4), 2 == (given & 2), 1 == (given & 1)));
        assertEquals(given, result);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7})
    final void apply_LSB_FIRST(final int given) {
        final int result = variety.with(BitOrder.LSB_FIRST)
                                  .apply(new InputImpl(1 == (given & 1), 2 == (given & 2), 4 == (given & 4)));
        assertEquals(given, result);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7})
    final void apply_MSB_FIRST(final int given) {
        final int result = variety.with(BitOrder.MSB_FIRST)
                                  .apply(new InputImpl(4 == (given & 4), 2 == (given & 2), 1 == (given & 1)));
        assertEquals(given, result);
    }
}