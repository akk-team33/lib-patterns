package de.team33.test.patterns.testing.titan;

import de.team33.patterns.testing.titan.Condition;
import de.team33.patterns.testing.titan.Input;
import de.team33.patterns.testing.titan.Operation;
import de.team33.patterns.testing.titan.Parallel;
import de.team33.patterns.testing.titan.Report;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ParallelTest extends Random {

    private static final Operation<Input> OPERATION = input -> {
        System.out.printf("threadIndex: %d, operationIndex: %d, loopIndex: %d%n",
                          input.threadIndex, input.operationIndex, input.loopIndex);
        return input;
    };

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144})
    final void byThreads(final int numberOfThreads) throws Exception {
        final Set<Integer> expectedThreadIndices = IntStream.range(0, numberOfThreads)
                                                            .boxed()
                                                            .collect(Collectors.toSet());
        final Report<Input> report = Parallel.report(numberOfThreads, OPERATION)
                                             .reThrowAny();
        assertEquals(expectedThreadIndices,
                     report.stream().map(input -> input.threadIndex).collect(Collectors.toSet()),
                     "Each started thread is expected to have performed the given operation at least once, " +
                             "so the number of different thread indices included in the result must exactly " +
                             "match the number of started threads.");

        final Set<Integer> expectedOperationIndices = IntStream.range(0, report.size())
                                                               .boxed()
                                                               .collect(Collectors.toSet());
        assertEquals(expectedOperationIndices,
                     report.stream().map(result -> result.operationIndex).collect(Collectors.toSet()),
                     "It is expected that each performed operation has been assigned a unique index, " +
                             "so the number of operation indices contained in the result must exactly match the " +
                             "number of individual results.");
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144})
    final void byThreads_minNumberOfOperations(final int numberOfThreads) throws Exception {
        final int minNumberOfOperations = numberOfThreads + nextInt(1000);
        final Set<Integer> expectedThreadIndices = IntStream.range(0, numberOfThreads)
                                                            .boxed()
                                                            .collect(Collectors.toSet());
        final Condition condition = Condition.byThreads(numberOfThreads, minNumberOfOperations);
        final Report<Input> report = Parallel.report(condition, OPERATION)
                                             .reThrowAny();
        assertEquals(expectedThreadIndices,
                     report.stream().map(input -> input.threadIndex).collect(Collectors.toSet()),
                     "Each started thread is expected to have performed the given operation at least once, " +
                             "so the number of different thread indices included in the result must exactly " +
                             "match the number of started threads.");

        final Set<Integer> expectedOperationIndices = IntStream.range(0, report.size())
                                                               .boxed()
                                                               .collect(Collectors.toSet());
        assertEquals(expectedOperationIndices,
                     report.stream().map(input -> input.operationIndex).collect(Collectors.toSet()),
                     "It is expected that each performed operation has been assigned a unique index, " +
                             "so the number of operation indices contained in the result must exactly match the " +
                             "number of individual results.");
        assertTrue(minNumberOfOperations <= expectedOperationIndices.size(),
                   () -> String.format("The operation is expected to have been performed at least the specified " +
                                               "number of times (%d) - but was %d",
                                       minNumberOfOperations, expectedOperationIndices.size()));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144})
    final void byOperations(final int numberOfOperations) throws Exception {
        final int numberOfThreads = 1 + nextInt(1 + numberOfOperations);
        final Condition condition = Condition.byOperations(numberOfOperations, numberOfThreads);
        final Report<Input> report = Parallel.report(condition, OPERATION)
                                             .reThrowAny();
        assertEquals(numberOfOperations, report.size(),
                     "The operation is expected to have been performed the specified number of times, " +
                             "so there should be as many results in the end.");
        final Set<Integer> expectedOperationIndices = IntStream.range(0, numberOfOperations)
                                                               .boxed()
                                                               .collect(Collectors.toSet());
        assertEquals(expectedOperationIndices,
                     report.stream().map(input -> input.operationIndex).collect(Collectors.toSet()),
                     "It is expected that each performed operation has been assigned a unique index, " +
                             "so the number of operation indices contained in the result must exactly match the " +
                             "number of individual results.");
        assertEquals(numberOfOperations, expectedOperationIndices.size());

        final Set<Integer> expectedThreadIndices = IntStream.range(0, numberOfThreads)
                                                            .boxed()
                                                            .collect(Collectors.toSet());
        final Set<Integer> threadsThatOperated = report.stream()
                                                       .map(result -> result.threadIndex)
                                                       .collect(Collectors.toSet());
        assertTrue(expectedThreadIndices.containsAll(threadsThatOperated),
                   () -> "");
        assertTrue(threadsThatOperated.size() <= numberOfOperations);
    }

    @Test
    final void report_reThrow() {
        final Report<Object> report = Parallel.report(16, input -> {
            throw new IOException("operationIndex: " + input.operationIndex);
        });
        assertThrows(IOException.class, () -> report.reThrow(IOException.class));
    }

    @Test
    final void stream_reThrow() {
        assertThrows(IOException.class, () -> Parallel.stream(16, input -> {
            throw new IOException("operationIndex: " + input.operationIndex);
        }));
    }
}
