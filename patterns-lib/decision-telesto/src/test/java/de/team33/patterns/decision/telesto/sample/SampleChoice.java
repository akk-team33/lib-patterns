package de.team33.patterns.decision.telesto.sample;

import de.team33.patterns.decision.telesto.Choice;

import java.util.function.Function;

public class SampleChoice {

    // Type of input parameter for criterion k1 ...
    public static class X {
    }

    // Type of input parameter for criterion k2 ...
    public static class Y {
    }

    // Type of input parameter for criterion k3 ...
    public static class Z {
    }

    public enum Result {

        A, B, C, D, E;

        public static Result map(final X x, final Y y, final Z z) {
            return Case.K_1_2_3.apply(new Input(x, y, z));
        }

        private static class Input {

            private static final SamplePlain.Y Y0 = new SamplePlain.Y();

            final X x;
            final Y y;
            final Z z;

            Input(final X x, final Y y, final Z z) {
                this.x = x;
                this.y = y;
                this.z = z;
            }

            // Criterion 1 ...
            final boolean isK1() {
                return x.toString().isEmpty();
            }

            // Criterion 2 ...
            final boolean isK2() {
                return y.equals(Y0);
            }

            // Criterion 3 ...
            final boolean isK3() {
                return z.toString().equals("abc");
            }
        }

        private enum Case implements Function<Input, Result> {

            K_T_T_3(Choice.on(Input::isK3).reply(A).orReply(C)),
            K_T_F_3(Choice.on(Input::isK3).reply(B).orReply(E)),
            K_F_T_3(Choice.on(Input::isK3).reply(D).orReply(A)),
            K_F_F_3(Choice.on(Input::isK3).reply(D).orReply(B)),
            K_T_2_3(Choice.on(Input::isK2).apply(K_T_T_3).orApply(K_T_F_3)),
            K_F_2_3(Choice.on(Input::isK2).apply(K_F_T_3).orApply(K_F_F_3)),
            K_1_2_3(Choice.on(Input::isK1).apply(K_T_2_3).orApply(K_F_2_3));

            private final Function<Input, Result> backing;

            Case(Function<Input, Result> backing) {
                this.backing = backing;
            }

            @Override
            public Result apply(Input input) {
                return backing.apply(input);
            }
        }
    }
}

