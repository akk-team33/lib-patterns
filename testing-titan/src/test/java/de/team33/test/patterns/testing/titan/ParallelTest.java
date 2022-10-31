package de.team33.test.patterns.testing.titan;

import de.team33.patterns.testing.titan.Indices;
import de.team33.patterns.testing.titan.Parallel;
import de.team33.patterns.testing.titan.Report;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParallelTest extends Random {

    @RepeatedTest(16)
    final void minNumberOfOperations() {
        final int minNumberOfOperations = 1 + nextInt(100);
        final Report<Indices> report = Parallel.report(minNumberOfOperations, 1, indices -> indices);
        final int maxIndexOfOperations = report.stream()
                                               .mapToInt(indices -> indices.operationIndex)
                                               .reduce(Integer.MIN_VALUE, Math::max);
        assertEquals(minNumberOfOperations - 1, maxIndexOfOperations);

        report.stream()
              .forEach(indices -> {
                  assertEquals(0, indices.threadIndex);
                  assertEquals(0, indices.executionIndex);
              });
    }

    @RepeatedTest(16)
    final void numberOfThreads() throws Exception {
        final int numberOfThreads = 1 + nextInt(100);
        final int maxExecutionIndex = Parallel.stream(numberOfThreads, indices -> indices)
                                              .mapToInt(indices -> indices.executionIndex)
                                              .reduce(Integer.MIN_VALUE, Math::max);
        assertEquals(numberOfThreads - 1, maxExecutionIndex);
    }
}
