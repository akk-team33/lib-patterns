package de.team33.patterns.decision.thyone.sample.parallel;

import de.team33.patterns.decision.thyone.Choices;

import java.util.function.Function;

public enum Result4 {

    CASE_000,
    CASE_001,
    CASE_010,
    CASE_011,
    CASE_100,
    CASE_101,
    CASE_110,
    CASE_111;

    private static final Function<Input, Result4> FUNCTION =
            Choices.parallel(Input::isRed, Input::isGreen, Input::isBlue)
                   .applying(i -> CASE_000, i -> CASE_001, i -> CASE_010, i -> CASE_011, i -> CASE_100, i -> CASE_101,
                             i -> CASE_110, i -> CASE_111);

    public static Result4 of(final Input input) {
        return FUNCTION.apply(input);
    }
}
