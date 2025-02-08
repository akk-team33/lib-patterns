package de.team33.patterns.decision.leda.sample.recent;

import de.team33.patterns.decision.leda.PreDecision;
import de.team33.patterns.decision.leda.sample.Input;

public enum Result {

    A, B, C, D, E;

    private static final PreDecision<Input, Result> CHOICE =
            PreDecision.basedOn(Input::isConditionOne, Input::isConditionTwo, Input::isConditionThree)
                       .replying(A, B, C, D, E, A, B, C);

    public static Result map(final Input input) {
        return CHOICE.apply(input);
    }
}
