package de.team33.patterns.decision.thyone.publics;

import de.team33.patterns.decision.thyone.Distinction;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DistinctionTest {

    private final List<Predicate<String>> criteria =
            IntStream.range(0, 10)
                     .mapToObj(DistinctionTest::lengthLTEQ)
                     .toList();

    private static Predicate<String> lengthLTEQ(final int bound) {
        return s -> bound >= s.length();
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "12", "12345", "1234567", "123456789", "1234567890", "123456789ABC"})
    final void apply(final String given) {
        final int expected = Integer.min(criteria.size(), given.length());
        final int result = Distinction.chain(criteria)
                                      .apply(given);
        assertEquals(expected, result);
    }
}