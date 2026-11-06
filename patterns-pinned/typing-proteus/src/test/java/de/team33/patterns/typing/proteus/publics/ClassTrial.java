package de.team33.patterns.typing.proteus.publics;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ClassTrial {

    static Stream<AssignableCase> assignableCases() {
        return Stream.of(assignableCase(long.class, int.class, false),
                         assignableCase(int.class, long.class, false),
                         assignableCase(Integer.class, int.class, false),
                         assignableCase(Object.class, int.class, false),
                         assignableCase(Object.class, double.class, false),
                         assignableCase(Object.class, Integer.class, true),
                         assignableCase(Object.class, Double.class, true),
                         assignableCase(Object.class, Number.class, true),
                         assignableCase(Object.class, Comparable.class, true),
                         assignableCase(Number.class, Double.class, true),
                         assignableCase(Comparable.class, Double.class, true),
                         assignableCase(Number.class, Comparable.class, false),
                         assignableCase(Comparable.class, Number.class, false),
                         assignableCase(Object.class, void.class, false),
                         assignableCase(Object.class, Void.class, true),
                         assignableCase(Void.class, void.class, false),
                         assignableCase(void.class, Void.class, false),
                         assignableCase(Void.class, Void.class, true),
                         assignableCase(void.class, void.class, true),
                         assignableCase(Integer.class, Object.class, false));
    }

    private static AssignableCase assignableCase(final Class<?> left, final Class<?> right, final boolean expected) {
        return new AssignableCase(left, right, expected);
    }

    @Test
    final void test_void() {
        assertTrue(void.class.isPrimitive());
    }

    @Test
    final void testVoid() {
        assertFalse(Void.class.isPrimitive());
    }

    @ParameterizedTest
    @MethodSource("assignableCases")
    final void isAssignableFrom(final AssignableCase given) {
        assertEquals(given.expected, given.left.isAssignableFrom(given.right));
    }

    record AssignableCase(Class<?> left, Class<?> right, boolean expected) {

        @Override
        public String toString() {
            return "%s <- %s (%s)".formatted(left, right, expected);
        }
    }
}
