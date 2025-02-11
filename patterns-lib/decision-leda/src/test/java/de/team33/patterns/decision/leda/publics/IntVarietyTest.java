package de.team33.patterns.decision.leda.publics;

import de.team33.patterns.decision.leda.BitOrder;
import de.team33.patterns.decision.leda.IntVariety;
import de.team33.patterns.decision.leda.Variety;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class IntVarietyTest {

    private static final Predicate<Input> P = i -> true;
    private static final List<Predicate<Input>> P_EMPTY = Collections.emptyList();

    private final IntVariety<Input> variety = IntVariety.joined(Input::isA, Input::isB, Input::isC);

    @Test
    final void joined_more() {
        try {
            final IntVariety<Input> variety = IntVariety.joined(Stream.generate(() -> P)
                                                                      .limit(Integer.SIZE + 1)
                                                                      .collect(Collectors.toList()));
            fail("expected to fail - but apply() returns " + variety.apply(new Input(false, false, false)));
        } catch (final IllegalArgumentException e) {
            // as expected
            // e.printStackTrace();
            assertEquals("Max. 32 conditions can be handled - but 33 are given.", e.getMessage());
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7})
    void joined_empty_apply(final int given) {
        final IntVariety<Input> variety = IntVariety.joined(P_EMPTY);
        final int result = variety.apply(new Input(4 == (given & 4), 2 == (given & 2), 1 == (given & 1)));
        assertEquals(0, result);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7})
    void joined_one_apply(final int given) {
        final IntVariety<Input> variety = IntVariety.joined(BitOrder.LSB_FIRST, Input::isA);
        final int result = variety.apply(new Input(4 == (given & 4), 2 == (given & 2), 1 == (given & 1)));
        assertEquals((0 == (given & 4)) ? 0 : 1, result);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7})
    void joined_max_apply(final int given) {
        final IntVariety<Input> variety = IntVariety.joined(Stream.generate(() -> P)
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