package de.team33.patterns.decision.carpo.sample.recent;

import de.team33.patterns.decision.carpo.Variety;
import de.team33.patterns.decision.carpo.testing.Input;

import java.util.function.Function;

public enum FResult {

    A, B, C, D, E;

    private static final Function<Input, FResult> FUNCTION =
            Variety.joined(Input::isC, Input::isB, Input::isA)
                   .replying(A, E, C, B, B, A, D, C);

    public static FResult map(final Input input) {
        return FUNCTION.apply(input);
    }
}
