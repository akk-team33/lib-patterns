package de.team33.patterns.decision.thyone.sample.serial;

import de.team33.patterns.decision.thyone.Choices;

import java.util.Objects;
import java.util.function.Function;

public enum Result2 {

    CASE_NULL,
    CASE_EMPTY,
    CASE_BLANK,
    CASE_SINGLE,
    CASE_OTHER;

    private static final Function<String, Result2> FUNCTION =
            Choices.serial(Objects::isNull, String::isEmpty, String::isBlank, input -> 1 == input.length())
                   .andThen(i -> switch (i) {
                       case 0 -> CASE_NULL;
                       case 1 -> CASE_EMPTY;
                       case 2 -> CASE_BLANK;
                       case 3 -> CASE_SINGLE;
                       default -> CASE_OTHER;
                   });

    public static Result2 of(final String input) {
        return FUNCTION.apply(input);
    }
}
