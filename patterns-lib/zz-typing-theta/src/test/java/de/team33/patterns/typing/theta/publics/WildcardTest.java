package de.team33.patterns.typing.theta.publics;

import de.team33.patterns.typing.theta.Type;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("ALL")
class WildcardTest {

    static Stream<Case<?>> cases() {
        return Stream.of(new Case<>(() -> new Type<>() {},
                                    "java.lang.Object"),
                         new Case<>(() -> new Type<Object>() {},
                                    "java.lang.Object"),
                         new Case<>(() -> new Type<Class>() {},
                                    "java.lang.Class"),
                         new Case<>(() -> new Type<Class<?>>() {},
                                    "java.lang.Class<?>"),
                         new Case<>(() -> new Type<List<? extends Object>>() {},
                                    "java.util.List<?>"),
                         new Case<>(() -> new Type<Set<? extends Number>>() {},
                                    "java.util.Set<? extends java.lang.Number>"),
                         new Case<>(() -> new Type<Map<? extends Comparable<? extends CharSequence>, ? super Integer>>() {},
                                    "java.util.Map<? extends java.lang.Comparable<? extends java.lang.CharSequence>, ? super java.lang.Integer>"),
                         new Case<>(() -> new Type<Class<? extends Throwable>>() {},
                                    "java.lang.Class<? extends java.lang.Throwable>"),
                         new Case<>(() -> new Type<Class<? super IllegalArgumentException>>() {},
                                    "java.lang.Class<? super java.lang.IllegalArgumentException>"));
    }

    static Stream<EqualsCase> equalsCases() {
        return cases().flatMap(WildcardTest::equalsCases);
    }

    private static Stream<EqualsCase> equalsCases(final Case<?> left) {
        return cases().map(right -> new EqualsCase(left, right));
    }

    @ParameterizedTest
    @MethodSource("equalsCases")
    final void equals(final EqualsCase given) {
        final var left = given.left.typeSupplier.get();
        final var right = given.right.typeSupplier.get();
        assertNotSame(left, right);
        assertEquals(given.expected(), left.equals(right));
        if (given.expected()) {
            assertEquals(left.hashCode(), right.hashCode());
        }
        if (left.hashCode() != right.hashCode()) {
            assertNotEquals(left, right);
        }
    }

    @ParameterizedTest
    @MethodSource("equalsCases")
    final void hashCode(final EqualsCase given) {
        final var left = given.left.typeSupplier.get();
        final var right = given.right.typeSupplier.get();
        if (given.expected()) {
            assertEquals(left.hashCode(), right.hashCode());
        }
        if (left.hashCode() != right.hashCode()) {
            assertNotEquals(left, right);
        }
    }

    @ParameterizedTest
    @MethodSource("cases")
    final <T extends Type<?>> void toString(final Case<T> given) {
        final T type = given.typeSupplier.get();
        assertEquals(given.expToString, type.toString());
    }

    record EqualsCase(Case<?> left, Case<?> right) {

        final boolean expected() {
            return left.expToString.equals(right.expToString);
        }
    }

    record Case<T extends Type<?>>(Supplier<T> typeSupplier, String expToString) {

        @Override
        public final String toString() {
            return expToString;
        }
    }
}