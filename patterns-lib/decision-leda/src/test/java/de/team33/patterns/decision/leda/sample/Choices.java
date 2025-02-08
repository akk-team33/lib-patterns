package de.team33.patterns.decision.leda.sample;

import de.team33.patterns.decision.leda.PreDecision;

import static de.team33.patterns.decision.leda.sample.Result.*;

public class Choices {

    private static final PreDecision<Input, Result> DECISION = PreDecision.basedOn(Input::k1, Input::k2, Input::k3)
                                                                          .replying(B, E, A, C, D, B, D, A);

    public static Result map(final X x, final Y y, final Z z) {
        return DECISION.apply(new Input(x, y, z));
    }

    private static class Input {

        final X x;
        final Y y;
        final Z z;

        Input(final X x, final Y y, final Z z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        final boolean k1() {
            return x.k1();
        }

        final boolean k2() {
            return y.k2();
        }

        final boolean k3() {
            return z.k3();
        }
    }
}

