package de.team33.patterns.decision.carpo.sample.recent;

import de.team33.patterns.decision.carpo.Variety;
import de.team33.patterns.decision.carpo.sample.Input;

import java.util.function.Function;

public enum RResult {

    A, B, C, D, E;

    private static final Function<Input, RResult> VARIETY =
            Variety.joined(Input::isConditionThree, Input::isConditionTwo, Input::isConditionOne)
                   .replying(A, B, C, D, E, A, B, C);

    public static RResult map(final Input input) {
        return VARIETY.apply(input);
    }
}
