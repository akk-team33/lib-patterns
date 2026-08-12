package de.team33.patterns.decision.thyone.sample.parallel;

import de.team33.patterns.decision.thyone.Choices;

import java.util.function.Function;

public enum Result2 {

    CASE_000,
    CASE_001,
    CASE_010,
    CASE_011,
    CASE_100,
    CASE_101,
    CASE_110,
    CASE_111;

    private static final Function<Input, Result2> FUNCTION =
            Choices.parallel(Input::isRed, Input::isGreen, Input::isBlue)
                   .andThen(i -> switch (i) {
                       case 0b000 -> CASE_000;
                       case 0b001 -> CASE_001;
                       case 0b010 -> CASE_010;
                       case 0b011 -> CASE_011;
                       case 0b100 -> CASE_100;
                       case 0b101 -> CASE_101;
                       case 0b110 -> CASE_110;
                       default /* case 0b111 */ -> CASE_111;
                   });

    public static Result2 of(final Input input) {
        return FUNCTION.apply(input);
    }
}
