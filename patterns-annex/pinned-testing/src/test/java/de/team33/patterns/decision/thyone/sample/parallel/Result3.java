package de.team33.patterns.decision.thyone.sample.parallel;

import de.team33.patterns.decision.thyone.Choices;

import java.util.function.Function;

public enum Result3 {

    CASE_000,
    CASE_001,
    CASE_010,
    CASE_011,
    CASE_100,
    CASE_101,
    CASE_110,
    CASE_111;

    private static final Function<Input, Result3> FUNCTION =
            Choices.parallel(Input::isRed, Input::isGreen, Input::isBlue)
                   .replying(CASE_000, CASE_001, CASE_010, CASE_011, CASE_100, CASE_101, CASE_110, CASE_111);

    public static Result3 of(final Input input) {
        return FUNCTION.apply(input);
    }
}
