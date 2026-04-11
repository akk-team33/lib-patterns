package de.team33.patterns.decision.thyone.sample.parallel;

import de.team33.patterns.decision.thyone.Choices;

public enum Result1 {

    CASE_000,
    CASE_001,
    CASE_010,
    CASE_011,
    CASE_100,
    CASE_101,
    CASE_110,
    CASE_111;

    private static final Choices<Input> CHOICES = Choices.parallel(Input::isRed, Input::isGreen, Input::isBlue);

    public static Result1 of(final Input input) {
        switch (CHOICES.apply(input)) {
            case 0b000:
                return CASE_000;
            case 0b001:
                return CASE_001;
            case 0b010:
                return CASE_010;
            case 0b011:
                return CASE_011;
            case 0b100:
                return CASE_100;
            case 0b101:
                return CASE_101;
            case 0b110:
                return CASE_110;
            default /* case 0b111 */:
                return CASE_111;
        }
    }
}
