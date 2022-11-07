package de.team33.test.patterns.testing.titan;

import de.team33.patterns.testing.titan.Input;
import de.team33.patterns.testing.titan.Parallel;
import de.team33.patterns.testing.titan.Report;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ParallelTest extends Random {

    @RepeatedTest(16)
    final void report_minNumberOfOperations() {
        final int minNumberOfOperations = 1 + nextInt(100);
        final Report<Input> report = Parallel.report(minNumberOfOperations, 1, input -> input);
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
        final int maxExecutionIndex = Parallel.stream(numberOfThreads, input -> input)
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
