package de.team33.patterns.predicates.aletheia.publics;

import de.team33.patterns.predicates.aletheia.Predicates;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("AssertBetweenInconvertibleTypes")
class PredicatesTest {

    private static final Random RANDOM = new SecureRandom();

    private static final Predicate<Object> P3 = candidate -> (candidate instanceof Long lc) && ((lc % 3) != 0);
    private static final Predicate<Number> P5 = candidate -> (candidate instanceof Long lc) && ((lc % 5) != 0);
    private static final Predicate<Comparable<?>> P7 = candidate -> (candidate instanceof Long lc) && ((lc % 7) != 0);

    @SuppressWarnings("SameParameterValue")
    @SafeVarargs
    private static <T> T[] array(final T... values) {
        return values;
    }

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

    @Test
    final void and_null() {
        assertThrows(NullPointerException.class, () -> Predicates.and(P3, P5, null, P7));
    }

    @Test
    final void and_reject() {
        final Predicate<Long> and = Predicates.and(P3, P5, Predicates.reject(), P7);
        assertSame(Predicates.reject(), and);
    }

    @Test
    final void and_test() {
        final Predicate<Long> plain = candidate -> Stream.<Predicate<? super Long>>of(P3, P5, P7)
                                                         .allMatch(p -> p.test(candidate));
        final Predicate<Long> and = Predicates.and(P3, P5, P7);
        for (int i = 0; i < 100; ++i) {
            final long candidate = RANDOM.nextLong();
            assertEquals(plain.test(candidate), and.test(candidate));
        }
    }

    @Test
    final void and_and_null() {
        assertThrows(NullPointerException.class, () -> Predicates.and(P3, P5, P7).and(null));
    }

    @Test
    final void and_and_reject() {
        final Predicate<Long> and = Predicates.and(P3, P5, P7);
        assertSame(Predicates.reject(), and.and(Predicates.reject()));
    }
}