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
            Choices.serial(Objects::isNull, String::isEmpty, input -> input.trim()
                                                                           .isEmpty(), input -> 1 == input.length())
                   .andThen(i -> {
                       switch (i) {
                           case 0:
                               return CASE_NULL;
                           case 1:
                               return CASE_EMPTY;
                           case 2:
                               return CASE_BLANK;
                           case 3:
                               return CASE_SINGLE;
                           default:
                               return CASE_OTHER;
                       }
                   });

    public static Result2 of(final String input) {
        return FUNCTION.apply(input);
    }
}
