package de.team33.patterns.decision.thyone.publics;

import de.team33.patterns.decision.thyone.sample.parallel.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class ParallelTest {

    static Stream<Input> input() {
        return Stream.of(new Input(null, null, null),
                         new Input(null, null, "B"),
                         new Input(null, "G", null),
                         new Input(null, "G", "B"),
                         new Input("R", null, null),
                         new Input("R", null, "B"),
                         new Input("R", "G", null),
                         new Input("R", "G", "B"));
    }

    @Test
    final void testResult0() {
        final Set<Result0> expected = EnumSet.allOf(Result0.class);
        final Set<Result0> result = input().map(Result0::of)
                                           .collect(Collectors.toSet());
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @MethodSource("input")
    final void testResult1(final Input input) {
        final Result0 expected = Result0.of(input);
        final Result1 result = Result1.of(input);
        assertEquals(expected.name(), result.name());
    }

    @ParameterizedTest
    @MethodSource("input")
    final void testResult2(final Input input) {
        final Result0 expected = Result0.of(input);
        final Result2 result = Result2.of(input);
        assertEquals(expected.name(), result.name());
    }

    @ParameterizedTest
    @MethodSource("input")
    final void replying_Result3(final Input input) {
        final Result0 expected = Result0.of(input);
        final Result3 result = Result3.of(input);
        assertEquals(expected.name(), result.name());
    }

    @ParameterizedTest
    @MethodSource("input")
    final void replying_Result3b(final Input input) {
        try {
            final Result3b result = Result3b.of(input);
            fail(() -> "expected to fail - but was %s".formatted(result));
        } catch (final IllegalArgumentException e) {
            // ok - as expected
            // e.printStackTrace();
        }
    }

    @ParameterizedTest
    @MethodSource("input")
    final void applying_Result4(final Input input) {
        final Result0 expected = Result0.of(input);
        final Result4 result = Result4.of(input);
        assertEquals(expected.name(), result.name());
    }

    @ParameterizedTest
    @MethodSource("input")
    final void applying_Result4b(final Input input) {
        try {
            final Result4b result = Result4b.of(input);
            fail(() -> "expected to fail - but was %s".formatted(result));
        } catch (final IllegalArgumentException e) {
            // ok - as expected
            // e.printStackTrace();
        }
    }
}
