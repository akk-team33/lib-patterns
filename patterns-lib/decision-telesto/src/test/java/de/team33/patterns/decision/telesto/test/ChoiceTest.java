package de.team33.patterns.decision.telesto.test;

import de.team33.patterns.decision.telesto.sample.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChoiceTest {

    @Test
    final void test() {
        for (int i = -2; i <= 2; ++i) {
            for (int k = -2; k <= 2; ++k) {
                for (int n = -2; n <= 2; ++n) {
                    final X x = new X(i);
                    final Y y = new Y(k);
                    final Z z = new Z(n);
                    final Result expected = Straight.map(x, y, z);

                    final Result result = Choices.map(x, y, z);

                    assertEquals(expected, result);
                }
            }
        }
    }
}
