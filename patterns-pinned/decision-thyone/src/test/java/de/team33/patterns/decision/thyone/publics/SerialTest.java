package de.team33.patterns.decision.thyone.publics;

import de.team33.patterns.decision.thyone.sample.serial.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class SerialTest {

    @SuppressWarnings("HardcodedLineSeparator")
    static Stream<String> input() {
        return Stream.of("  \t\r\n  ",
                         null,
                         "A",
                         "",
                         "something else");
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
    final void testResult1(final String input) {
        final Result0 expected = Result0.of(input);
        final Result1 result = Result1.of(input);
        assertEquals(expected.name(), result.name());
    }

    @ParameterizedTest
    @MethodSource("input")
    final void testResult2(final String input) {
        final Result0 expected = Result0.of(input);
        final Result2 result = Result2.of(input);
        assertEquals(expected.name(), result.name());
    }

    @ParameterizedTest
    @MethodSource("input")
    final void testResult3(final String input) {
        final Result0 expected = Result0.of(input);
        final Result3 result = Result3.of(input);
        assertEquals(expected.name(), result.name());
    }

    @ParameterizedTest
    @MethodSource("input")
    final void testResult3b(final String input) {
        final Result3b result = Result3b.of(input);
        assertNull(result);
    }
}
