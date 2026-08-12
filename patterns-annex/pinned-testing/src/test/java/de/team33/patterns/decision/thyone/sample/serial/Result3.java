package de.team33.patterns.decision.thyone.sample.serial;

import de.team33.patterns.decision.thyone.Choices;

import java.util.Objects;
import java.util.function.Function;

public enum Result3 {

    CASE_NULL,
    CASE_EMPTY,
    CASE_BLANK,
    CASE_SINGLE,
    CASE_OTHER;

    private static final Function<String, Result3> FUNCTION =
            Choices.serial(Objects::isNull, String::isEmpty, String::isBlank, input -> 1 == input.length())
                   .replying(CASE_NULL, CASE_EMPTY, CASE_BLANK, CASE_SINGLE, CASE_OTHER);

    public static Result3 of(final String input) {
        return FUNCTION.apply(input);
    }
}
