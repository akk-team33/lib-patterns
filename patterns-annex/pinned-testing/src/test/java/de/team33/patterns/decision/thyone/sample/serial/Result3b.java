package de.team33.patterns.decision.thyone.sample.serial;

import de.team33.patterns.decision.thyone.Choices;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public enum Result3b {

    CASE_NULL,
    CASE_EMPTY,
    CASE_BLANK,
    CASE_SINGLE,
    CASE_OTHER;

    private static final List<Predicate<String>> CRITERIA =
            List.of(Objects::isNull, String::isEmpty, String::isBlank, input -> 1 == input.length());
    private static final Function<String, Result3b> FUNCTION =
            Choices.serial(CRITERIA)
                   .replying(Arrays.asList(null, null, null, null, null));

    public static Result3b of(final String input) {
        return FUNCTION.apply(input);
    }
}
