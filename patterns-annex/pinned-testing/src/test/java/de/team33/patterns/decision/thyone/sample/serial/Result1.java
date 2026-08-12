package de.team33.patterns.decision.thyone.sample.serial;

import de.team33.patterns.decision.thyone.Choices;

import java.util.Objects;

public enum Result1 {

    CASE_NULL,
    CASE_EMPTY,
    CASE_BLANK,
    CASE_SINGLE,
    CASE_OTHER;

    private static final Choices<String> CHOICES = Choices.serial(
            Objects::isNull,
            String::isEmpty,
            String::isBlank,
            input -> 1 == input.length());

    public static Result1 of(final String input) {
        return switch (CHOICES.apply(input)) {
            case 0 -> CASE_NULL;
            case 1 -> CASE_EMPTY;
            case 2 -> CASE_BLANK;
            case 3 -> CASE_SINGLE;
            default -> CASE_OTHER;
        };
    }
}
