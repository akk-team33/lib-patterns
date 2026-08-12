package de.team33.patterns.decision.thyone.sample.parallel;

import de.team33.patterns.decision.thyone.Choices;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

@SuppressWarnings("unused")
public enum Result3b {

    CASE_000,
    CASE_001,
    CASE_010,
    CASE_011,
    CASE_100,
    CASE_101,
    CASE_110,
    CASE_111;

    private static final List<Predicate<? super Input>> CRITERIA =
            List.of(Input::isRed, Input::isGreen, Input::isBlue);
    private static final Function<Input, Result3b> FUNCTION =
            Choices.parallel(CRITERIA)
                   .replying(List.of());

    public static Result3b of(final Input input) {
        return FUNCTION.apply(input);
    }
}
