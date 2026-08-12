package de.team33.patterns.decision.thyone.sample.serial;

public enum Result0 {

    CASE_NULL,
    CASE_EMPTY,
    CASE_BLANK,
    CASE_SINGLE,
    CASE_OTHER;

    public static Result0 of(final String input) {
        if (null == input) {
            return CASE_NULL;
        } else if (input.isEmpty()) {
            return CASE_EMPTY;
        } else if (input.isBlank()) {
            return CASE_BLANK;
        } else if (1 == input.length()) {
            return CASE_SINGLE;
        } else {
            return CASE_OTHER;
        }
    }
}
