package de.team33.patterns.decision.telesto.sample;

import de.team33.patterns.decision.telesto.Choice;

import java.util.function.Function;

public class SampleChoice {

    public enum Result {

        A, B, C, D, E;

        public static Result map(final X x, final Y y, final Z z) {
            return Case.HEAD.apply(new Input(x, y, z));
        }

        private enum Case implements Function<Input, Result> {

            CASE_11_(Choice.on(Input::k3).reply(A).orReply(C)),
            CASE_10_(Choice.on(Input::k3).reply(B).orReply(E)),
            CASE_01_(Choice.on(Input::k3).reply(D).orReply(A)),
            CASE_00_(Choice.on(Input::k3).reply(D).orReply(B)),
            CASE_1__(Choice.on(Input::k2).apply(CASE_11_).orApply(CASE_10_)),
            CASE_0__(Choice.on(Input::k2).apply(CASE_01_).orApply(CASE_00_)),
            HEAD(Choice.on(Input::k1).apply(CASE_1__).orApply(CASE_0__));

            private final Function<Input, Result> backing;

            Case(Function<Input, Result> backing) {
                this.backing = backing;
            }

            @Override
            public Result apply(Input input) {
                return backing.apply(input);
            }
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
}

