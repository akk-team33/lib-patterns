package de.team33.patterns.decision.leda.sample.recent;

import de.team33.patterns.decision.leda.ProVariety;
import de.team33.patterns.decision.leda.sample.Input;

public enum Result {

    A, B, C, D, E;

    private static final ProVariety<Input, Result> CHOICE =
            ProVariety.joined(Input::isConditionOne, Input::isConditionTwo, Input::isConditionThree)
                      .replying(A, B, C, D, E, A, B, C);

    public static Result map(final Input input) {
        return CHOICE.apply(input);
    }
}
