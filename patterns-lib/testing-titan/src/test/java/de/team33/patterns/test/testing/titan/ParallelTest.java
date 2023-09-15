package de.team33.patterns.test.testing.titan;

import de.team33.patterns.testing.titan.Context;
import de.team33.patterns.testing.titan.Operation;
import de.team33.patterns.testing.titan.Parallel;
import de.team33.patterns.testing.titan.Report;
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

    private static final Operation<Context> OPERATION = context -> {
        Thread.sleep(0);
//        System.out.printf("threadIndex: %d, operationIndex: %d, loopIndex: %d%n",
//                          input.threadIndex, input.operationIndex, input.loopIndex);
        return context;
    };

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144})
    final void stream(final int numberOfThreads) throws Exception {
        Parallel.stream(numberOfThreads, OPERATION)
                .forEach(result -> assertTrue(result.operationIndex >= result.threadIndex));
    };

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144})
    final void report(final int numberOfThreads) throws Exception {
        final Set<Integer> expectedThreadIndices = IntStream.range(0, numberOfThreads)
                                                            .boxed()
                                                            .collect(Collectors.toSet());
        final Report<Context> report = Parallel.report(numberOfThreads, OPERATION)
                                               .reThrowAny();
        assertEquals(expectedThreadIndices,
                     report.stream().map(context -> context.threadIndex).collect(Collectors.toSet()),
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

    @Test
    final void report_reThrow() {
        final Report<Object> report = Parallel.report(16, context -> {
            throw new IOException("operationIndex: " + context.operationIndex);
        });
        assertThrows(IOException.class, () -> report.reThrow(IOException.class));
    }

    @Test
    final void stream_reThrow() {
        assertThrows(IOException.class, () -> Parallel.stream(16, context -> {
            throw new IOException("operationIndex: " + context.operationIndex);
        }));
    }
}
