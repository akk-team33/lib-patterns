package de.team33.patterns.decision.thyone.sample.parallel;

public enum Result0 {

    CASE_000,
    CASE_001,
    CASE_010,
    CASE_011,
    CASE_100,
    CASE_101,
    CASE_110,
    CASE_111;

    @SuppressWarnings("MethodWithMultipleReturnPoints")
    public static Result0 of(final Input input) {
        if (input.isRed()) {
            if (input.isGreen()) {
                if (input.isBlue()) {
                    return CASE_111;
                } else {
                    return CASE_110;
                }
            } else {
                if (input.isBlue()) {
                    return CASE_101;
                } else {
                    return CASE_100;
                }
            }
        } else {
            if (input.isGreen()) {
                if (input.isBlue()) {
                    return CASE_011;
                } else {
                    return CASE_010;
                }
            } else {
                if (input.isBlue()) {
                    return CASE_001;
                } else {
                    return CASE_000;
                }
            }
        }
    }
}
