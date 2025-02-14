package de.team33.patterns.decision.carpo.publics;

import de.team33.patterns.decision.carpo.BitOrder;
import de.team33.patterns.decision.carpo.Variety;
import de.team33.patterns.decision.carpo.testing.Input;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class VarietyTest {

    private static final Predicate<Input> P = i -> true;
    private static final List<Predicate<Input>> P_EMPTY = Collections.emptyList();

    private final Variety<Input> variety = Variety.joined(Input::isA, Input::isB, Input::isC);

    @Test
    final void joined_more() {
        try {
            final Variety<Input> variety = Variety.joined(Stream.generate(() -> P)
                                                                .limit(Integer.SIZE + 1)
                                                                .collect(Collectors.toList()));
            fail("expected to fail - but apply() returns " + variety.apply(new Input(false, false, false)));
        } catch (final IllegalArgumentException e) {
            // as expected
            // e.printStackTrace();
            assertEquals("Max. 32 predicates can be handled - but 33 are given.", e.getMessage());
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7})
    void joined_empty_apply(final int given) {
        final Variety<Input> variety = Variety.joined(P_EMPTY);
        final int result = variety.apply(new Input(4 == (given & 4), 2 == (given & 2), 1 == (given & 1)));
        assertEquals(0, result);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7})
    void joined_one_apply(final int given) {
        final Variety<Input> variety = Variety.joined(BitOrder.LSB_FIRST, Input::isA);
        final int result = variety.apply(new Input(4 == (given & 4), 2 == (given & 2), 1 == (given & 1)));
        assertEquals((0 == (given & 4)) ? 0 : 1, result);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7})
    void joined_max_apply(final int given) {
        final Variety<Input> variety = Variety.joined(Stream.generate(() -> P)
                                                            .limit(Integer.SIZE)
                                                            .collect(Collectors.toList()));
        final int result = variety.apply(new Input(4 == (given & 4), 2 == (given & 2), 1 == (given & 1)));
        assertEquals(-1, result);
    }

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
}