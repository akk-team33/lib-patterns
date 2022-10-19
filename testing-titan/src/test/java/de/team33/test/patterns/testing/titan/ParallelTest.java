package de.team33.test.patterns.testing.titan;

import de.team33.patterns.exceptional.e1.XConsumer;
import de.team33.patterns.exceptional.e1.XFunction;
import de.team33.patterns.testing.titan.Parallel;
import de.team33.patterns.testing.titan.Report;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ParallelTest {

    @Test
    final void apply_symmetric() throws Throwable {
        final int limit = 100;
        final XFunction<Integer, Integer, InterruptedException> method = index -> {
            Thread.sleep(2);
            return index;
        };

        final Report<Integer> report = Parallel.apply(limit, method)
                                               .reThrow(Throwable.class);

        assertEquals(Collections.emptyList(), report.getThrowables(),
                     "No exception is expected to be thrown");
        assertEquals(limit, report.getResults().size(),
                     "The size of the collected <values> is expected to reach the <limit>");
        final List<Integer> sorted = report.getResults().stream().sorted().collect(toList());
        assertNotEquals(sorted, report.getResults(),
                        "The collected results are expected to be unordered");
        assertEquals(new HashSet<>(report.getResults()).size(), report.getResults().size(),
                     "The collected <values> are expected to be unique");
    }

    @Test
    final void apply_asymmetric() throws Throwable {
        final XFunction<Integer, Integer, InterruptedException> method = index -> {
            Thread.sleep(2);
            return index;
        };

        final int numberOfExecutions = 449;
        final int numberOfThreads = 59;
        final Report<Integer> report = Parallel.apply(numberOfExecutions, numberOfThreads, method)
                                               .reThrow(Throwable.class);

        assertEquals(Collections.emptyList(), report.getThrowables(),
                     "No exception is expected to be thrown");
        assertEquals(numberOfExecutions, report.getResults().size(),
                     "The size of the collected <values> is expected to reach the <limit>");
        final List<Integer> sorted = report.getResults().stream().sorted().collect(toList());
        assertNotEquals(sorted, report.getResults(),
                        "The collected results are expected to be unordered");
        assertEquals(new HashSet<>(report.getResults()).size(), report.getResults().size(),
                     "The collected <values> are expected to be unique");
    }

    @Test
    final void invoke_withCaught_symmetric() throws Exception {
        final int limit = 100;
        final List<Integer> values = Collections.synchronizedList(new ArrayList<>(limit));
        final XConsumer<Integer, PositiveException> method = index -> {
            if (index % 13 == 0) {
                throw new PositiveException("index = " + index);
            } else {
                values.add(index);
            }
        };
        final int expectedToFail = ((limit - 1) / 13) + 1;

        final Report<Void> report = Parallel.invoke(limit, method)
                                            .reThrow(Error.class)
                                            .reThrow(Exception.class, PositiveException.class, NegativeException.class);

        assertEquals(expectedToFail, report.getThrowables(PositiveException.class).size());
        assertEquals(0, report.getThrowables(NegativeException.class).size());
        assertEquals(limit - expectedToFail, values.size());
    }

    @Test
    final void invoke_withCaught_asymmetric() throws Exception {
        final int numberOfExecutions = 353;
        final int numberOfThreads = 73;
        final List<Integer> values = Collections.synchronizedList(new ArrayList<>(numberOfExecutions));
        final XConsumer<Integer, PositiveException> method = index -> {
            if (index % 13 == 0) {
                throw new PositiveException("index = " + index);
            } else {
                values.add(index);
            }
        };
        final int expectedToFail = ((numberOfExecutions - 1) / 13) + 1;

        final Report<Void> report = Parallel.invoke(numberOfExecutions, numberOfThreads, method)
                                            .reThrow(Error.class)
                                            .reThrow(Exception.class, PositiveException.class, NegativeException.class);

        assertEquals(expectedToFail, report.getThrowables(PositiveException.class).size());
        assertEquals(0, report.getThrowables(NegativeException.class).size());
        assertEquals(numberOfExecutions - expectedToFail, values.size());
    }

    static class PositiveException extends Exception {

        PositiveException(final String message) {
            super(message);
        }
    }

    static class NegativeException extends Exception {
    }
}
