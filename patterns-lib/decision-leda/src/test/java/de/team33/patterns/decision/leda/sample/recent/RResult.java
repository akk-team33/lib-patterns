package de.team33.patterns.decision.leda.sample.recent;

import de.team33.patterns.decision.leda.Variety;
import de.team33.patterns.decision.leda.sample.Input;

public enum RResult {

    A, B, C, D, E;

    private static final Variety<Input, RResult> VARIETY =
            Variety.joined(Input::isConditionThree, Input::isConditionTwo, Input::isConditionOne)
                   .replying(A, B, C, D, E, A, B, C);

    public static RResult map(final Input input) {
        return VARIETY.apply(input);
    }
}
