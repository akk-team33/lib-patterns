package de.team33.patterns.predicates.aletheia.publics;

import de.team33.patterns.predicates.aletheia.Predicates;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("AssertBetweenInconvertibleTypes")
class PredicatesTest {

    private static final Random RANDOM = new SecureRandom();

    @Test
    final void accept() {
        final Predicate<Long> acceptLong = Predicates.accept();
        final Predicate<Date> acceptDate = Predicates.accept();
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
}