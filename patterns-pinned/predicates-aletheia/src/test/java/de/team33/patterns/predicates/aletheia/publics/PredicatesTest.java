package de.team33.patterns.predicates.aletheia.publics;

import de.team33.patterns.predicates.aletheia.Predicates;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("ClassWithTooManyMethods")
class PredicatesTest {

    private static final Random RANDOM = new SecureRandom();

    private static Predicate<Integer> isLess(final int limit) {
        return subject -> limit > subject;
    }

    static Stream<Case> andCases() {
        return Stream.of(new Case(1, List.of(), true),
                         new Case(2, List.of(1), false),
                         new Case(3, List.of(4), true),
                         new Case(4, List.of(3, 5), false),
                         new Case(5, List.of(7, 8, 9, 10), true),
                         new Case(6, List.of(8, 9, 6, 100, 101), false),
                         anyAndCase());
    }

    private static Case anyAndCase() {
        final int testee = RANDOM.nextInt();
        final List<Integer> limits = Stream.generate(RANDOM::nextInt)
                                           .limit(RANDOM.nextInt(10))
                                           .toList();
        final boolean expected = limits.stream().allMatch(limit -> isLess(limit).test(testee));
        return new Case(testee, limits, expected);
    }

    static Stream<Case> orCases() {
        return Stream.of(new Case(1, List.of(), false),
                         new Case(2, List.of(1), false),
                         new Case(3, List.of(4), true),
                         new Case(4, List.of(3, 5), true),
                         new Case(5, List.of(7, 8, 9, 10), true),
                         new Case(6, List.of(-8, -9, 5, -100, -101), false),
                         anyOrCase());
    }

    private static Case anyOrCase() {
        final int testee = RANDOM.nextInt();
        final List<Integer> limits = Stream.generate(RANDOM::nextInt)
                                           .limit(RANDOM.nextInt(10))
                                           .toList();
        final boolean expected = limits.stream().anyMatch(limit -> isLess(limit).test(testee));
        return new Case(testee, limits, expected);
    }

    @Test
    final void accept() {
        final Predicate<Long> acceptLong = Predicates.accept();
        final Predicate<Date> acceptDate = Predicates.accept();
        //noinspection AssertBetweenInconvertibleTypes
        assertSame(acceptLong, acceptDate);
    }

    @Test
    final void accept_test() {
        final Predicate<Long> accept = Predicates.accept();
        assertTrue(accept.test(RANDOM.nextLong()));
    }

    @Test
    final void accept_and() {
        final Predicate<Long> accept = Predicates.accept();
        final Predicate<Number> other = any -> RANDOM.nextBoolean();
        assertSame(other, accept.and(other));
    }

    @Test
    final void accept_or() {
        final Predicate<Long> accept = Predicates.accept();
        final Predicate<Number> other = any -> RANDOM.nextBoolean();
        assertSame(accept, accept.or(other));
    }

    @Test
    final void accept_negate() {
        final Predicate<Long> accept = Predicates.accept();
        final Predicate<Long> reject = Predicates.reject();
        assertSame(reject, accept.negate());
    }

    @Test
    final void accept_toString() {
        assertEquals("ACCEPT", Predicates.accept().toString());
    }

    @Test
    final void reject() {
        final Predicate<Long> rejectLong = Predicates.reject();
        final Predicate<Date> rejectDate = Predicates.reject();
        //noinspection AssertBetweenInconvertibleTypes
        assertSame(rejectLong, rejectDate);
    }

    @Test
    final void reject_test() {
        final Predicate<Long> reject = Predicates.reject();
        assertFalse(reject.test(RANDOM.nextLong()));
    }

    @Test
    final void reject_and() {
        final Predicate<Long> reject = Predicates.reject();
        final Predicate<Number> other = any -> RANDOM.nextBoolean();
        assertSame(reject, reject.and(other));
    }

    @Test
    final void reject_or() {
        final Predicate<Long> reject = Predicates.reject();
        final Predicate<Number> other = any -> RANDOM.nextBoolean();
        assertSame(other, reject.or(other));
    }

    @Test
    final void reject_negate() {
        final Predicate<Long> reject = Predicates.reject();
        final Predicate<Long> accept = Predicates.accept();
        assertSame(accept, reject.negate());
    }

    @Test
    final void reject_toString() {
        assertEquals("REJECT", Predicates.reject().toString());
    }

    @ParameterizedTest
    @MethodSource("andCases")
    final void and_test(final Case given) {
        final Predicate<Integer> and = Predicates.and(given.predicates());
        assertEquals(given.expected, and.test(given.testee));
    }

    @Test
    final void and_test_empty() {
        assertEquals(Predicates.accept(), Predicates.and());
    }

    @ParameterizedTest
    @MethodSource("andCases")
    final void and_test_reject(final Case given) {
        final List<Predicate<Integer>> origin = new ArrayList<>(given.predicates());
        origin.add(Predicates.accept());
        origin.add(Predicates.reject());
        origin.addAll(given.predicates());

        assertSame(Predicates.reject(), Predicates.and(origin));
    }

    @ParameterizedTest
    @MethodSource("orCases")
    final void or_test(final Case given) {
        final Predicate<Integer> or = Predicates.or(given.predicates());
        assertEquals(given.expected, or.test(given.testee));
    }

    @Test
    final void or_test_empty() {
        assertSame(Predicates.reject(), Predicates.or());
    }

    @ParameterizedTest
    @MethodSource("orCases")
    final void or_test_accept(final Case given) {
        final List<Predicate<Integer>> origin = new ArrayList<>(given.predicates());
        origin.add(Predicates.accept());
        origin.add(Predicates.reject());
        origin.addAll(given.predicates());

        assertSame(Predicates.accept(), Predicates.or(origin));
    }

    @SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
    record Case(int testee, List<Integer> limits, boolean expected) {

        final List<Predicate<Integer>> predicates() {
            return limits.stream().map(PredicatesTest::isLess).toList();
        }
    }
}