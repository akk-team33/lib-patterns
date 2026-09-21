package de.team33.patterns.predicates.aletheia;

import de.team33.patterns.arbitrary.mimas.Generator;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.security.SecureRandom;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class AndTest {

    private static final Generator GENERATOR = Generator.of(new SecureRandom());


    private static final Predicate<Object> P3 = candidate -> (candidate instanceof Long lc) && ((lc % 3) != 0);
    private static final Predicate<Number> P5 = candidate -> (candidate instanceof Long lc) && ((lc % 5) != 0);
    private static final Predicate<Comparable<?>> P7 = candidate -> (candidate instanceof Long lc) && ((lc % 7) != 0);
    private static final Predicate<Long> P11 = candidate -> (candidate % 11) != 0;
    private static final Predicate<Comparable<?>> P13 = candidate -> (candidate instanceof Long lc) && ((lc % 13) != 0);
    private static final Predicate<Object> P17 = candidate -> (candidate instanceof Long lc) && ((lc % 17) != 0);
    private static final Predicate<Number> P19 = candidate -> (candidate instanceof Long lc) && ((lc % 19) != 0);
    private static final Predicate<? super Long>[] PREDICATES = array(P3, P5, P7, P11, P13, P17, P19,
                                                                      Predicates.accept(), Predicates.reject());

    @SuppressWarnings("SameParameterValue")
    @SafeVarargs
    private static Predicate<? super Long>[] array(final Predicate<? super Long>... predicates) {
        return predicates;
    }

    private static Stream<Predicate<? super Long>> anyPlainPredicates() {
        return anyPredicates().filter(predicate -> Predicates.accept() != predicate)
                              .filter(predicate -> Predicates.reject() != predicate);
    }

    private static Stream<Predicate<? super Long>> anyPredicates() {
        return Stream.generate(() -> GENERATOR.anyOf(PREDICATES));
    }

    @Disabled
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 5, 8, 13, 21})
    final void and(final int limit) {
        final Predicate<Long> and = anyPredicates().limit(limit)
                                                   .map(predicate -> new And<Long>().and(predicate))
                                                   .reduce(Predicate::and)
                                                   .orElseGet(Predicates::accept);
        assertSame(Predicates.reject(), and.and(Predicates.reject()));
        assertSame(and, and.and(Predicates.accept()));
        final Predicate<Long> moreAnd = anyPlainPredicates().limit(1 + GENERATOR.anyInt(10))
                                                            .map(predicate -> new And<Long>().and(predicate))
                                                            .reduce(Predicate::and)
                                                            .orElseGet(Predicates::accept);
        final Predicate<Long> andAnd = and.and(moreAnd);
        assertNotSame(and, andAnd);
        assertInstanceOf(And.class, andAnd);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 5, 8, 13, 21})
    final void test(final int limit) {
        final long sample = GENERATOR.anyLong();
        final List<Predicate<? super Long>> predicates = anyPredicates().limit(limit)
                                                                        .toList();
        final Predicate<Long> plain = candidate -> predicates.stream()
                                                             .allMatch(predicate -> predicate.test(candidate));
        final Predicate<Long> and = predicates.stream()
                                              .map(predicate -> new And<Long>().and(predicate))
                                              .reduce(Predicate::and)
                                              .orElseGet(Predicates::accept);
        assertEquals(plain.test(sample), and.test(sample));
    }

    @Test
    void and() {
    }

    @Test
    void or() {
    }
}