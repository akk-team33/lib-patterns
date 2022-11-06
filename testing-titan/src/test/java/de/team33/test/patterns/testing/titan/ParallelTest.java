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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ParallelTest extends Random {

    private static final Operation<Input> OPERATION = input -> {
        System.out.printf("threadIndex: %d, executionIndex: %d, operationIndex: %d, loopIndex: %d%n",
                          input.threadIndex, input.executionIndex, input.operationIndex, input.loopIndex);
        return input;
    };

    private static class Maxima {

        private int threadIndex = Integer.MIN_VALUE;
        private int executionIndex = Integer.MIN_VALUE;
        private int operationIndex = Integer.MIN_VALUE;
        private int loopIndex = Integer.MIN_VALUE;

        final void add(final Input input) {
            threadIndex = Math.max(threadIndex, input.threadIndex);
            executionIndex = Math.max(executionIndex, input.executionIndex);
            operationIndex = Math.max(operationIndex, input.operationIndex);
            loopIndex = Math.max(loopIndex, input.loopIndex);
        }

        final void add(final Maxima other) {
            threadIndex = Math.max(threadIndex, other.threadIndex);
            executionIndex = Math.max(executionIndex, other.executionIndex);
            operationIndex = Math.max(operationIndex, other.operationIndex);
            loopIndex = Math.max(loopIndex, other.loopIndex);
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144})
    final void byThreads(final int numberOfThreads) throws Throwable {
        final Report<Input> report = Parallel.report(numberOfThreads, OPERATION)
                                             .reThrowAny();
//        assertEquals(numberOfThreads, report.stream().mapToInt(input -> input.threadIndex).distinct().count());
//        assertEquals(numberOfThreads, report.stream().mapToInt(input -> input.executionIndex).distinct().count());
        assertEquals(report.size(), report.stream().mapToInt(input -> input.operationIndex).distinct().count());
        final Maxima maxima = report.stream()
                                    .collect(Maxima::new, Maxima::add, Maxima::add);
        assertEquals(numberOfThreads - 1, maxima.threadIndex);
        assertEquals(numberOfThreads - 1, maxima.executionIndex);
        assertTrue((numberOfThreads - 1) <= maxima.operationIndex);
        assertTrue(0 <= maxima.loopIndex);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144})
    final void byThreads_minNumberOfOperations(final int numberOfThreads) throws Exception {
        final int minNumberOfOperations = numberOfThreads + nextInt(100);
        final Condition condition = Condition.byThreads(numberOfThreads, minNumberOfOperations);
        final Maxima maxima = Parallel.stream(condition, OPERATION)
                                      .collect(Maxima::new, Maxima::add, Maxima::add);
        assertEquals(numberOfThreads - 1, maxima.threadIndex);
        assertEquals(numberOfThreads - 1, maxima.executionIndex);
        assertTrue((minNumberOfOperations - 1) <= maxima.operationIndex);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144})
    final void byThreads_maxNumberOfOperations(final int numberOfThreads) throws Exception {
        final Condition condition = Condition.byOperations(numberOfThreads, numberOfThreads);
        final Maxima maxima = Parallel.stream(condition, OPERATION)
                                      .collect(Maxima::new, Maxima::add, Maxima::add);
        assertTrue(numberOfThreads >= maxima.threadIndex);
        assertTrue(numberOfThreads >= maxima.executionIndex);
        assertEquals(numberOfThreads - 1, maxima.operationIndex);
    }

    @RepeatedTest(16)
    final void report_minNumberOfOperations() {
        final int minNumberOfOperations = 1 + nextInt(100);
        final Report<Input> report = Parallel.report(Condition.byThreads(1, minNumberOfOperations), OPERATION);
        final int maxIndexOfOperations = report.stream()
                                               .mapToInt(input -> input.operationIndex)
                                               .reduce(Integer.MIN_VALUE, Math::max);
        assertEquals(minNumberOfOperations - 1, maxIndexOfOperations);

        report.stream()
              .forEach(input -> {
                  assertEquals(0, input.threadIndex);
                  assertEquals(0, input.executionIndex);
              });
    }

    @RepeatedTest(16)
    final void stream_numberOfThreads() throws Exception {
        final int numberOfThreads = 1 + nextInt(100);
        final int maxExecutionIndex = Parallel.stream(numberOfThreads, OPERATION)
                                              .mapToInt(input -> input.executionIndex)
                                              .reduce(Integer.MIN_VALUE, Math::max);
        assertEquals(numberOfThreads - 1, maxExecutionIndex);
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
