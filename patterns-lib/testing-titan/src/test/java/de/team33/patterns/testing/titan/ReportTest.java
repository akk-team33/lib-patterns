package de.team33.patterns.testing.titan;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Deprecated
class ReportTest extends Random {

    private static final BiConsumer<Report.Builder<Integer>, Report.Builder<Integer>> ADD_ALL = (a, b) -> {
        throw new UnsupportedOperationException("should not be called");
    };
    private List<Integer> resultList;
    private List<Throwable> exceptionList;
    private Report<Integer> report;

    @BeforeEach
    final void setUp() {
        resultList = Stream.generate(this::nextInt)
                           .limit(20 + nextInt(80))
                           .collect(Collectors.toList());
        exceptionList = Arrays.asList(
                new IllegalStateException(),
                new IllegalArgumentException(),
                new NumberFormatException(),
                new IOException(),
                new SQLException(),
                new OutOfMemoryError(),
                new InternalError(),
                new AssertionError());
        final Report.Builder<Integer> builder = new Report.Builder<>();
        resultList.forEach(builder::add);
        exceptionList.forEach(builder::add);
        report = builder.build();
    }

    @Test
    final void getResults() {
        assertEquals(resultList, report.getResults());
    }

    @Test
    final void stream() {
        assertEquals(resultList, report.stream().collect(Collectors.toList()));
    }

    @Test
    final void getCaught() {
        assertEquals(exceptionList, report.getCaught());
    }

    @Test
    final void getCaught_RuntimeException() {
        final List<Throwable> expected = exceptionList.stream()
                                                      .filter(obj -> obj instanceof RuntimeException)
                                                      .collect(Collectors.toList());
        assertEquals(expected, report.getCaught(RuntimeException.class));
    }

    @Test
    final void getCaught_Exception() {
        final List<Throwable> expected = exceptionList.stream()
                                                      .filter(obj -> obj instanceof Exception)
                                                      .filter(obj -> !(obj instanceof RuntimeException))
                                                      .collect(Collectors.toList());
        assertEquals(expected, report.getCaught(Throwable.class, Error.class, RuntimeException.class));
    }

    @Test
    final void getCaught_Error() {
        final List<Throwable> expected = exceptionList.stream()
                                                      .filter(obj -> obj instanceof Error)
                                                      .collect(Collectors.toList());
        assertEquals(expected, report.getCaught(Throwable.class, Exception.class));
    }

    @Test
    final void reThrow_IllegalStateException() {
        assertThrows(IllegalStateException.class, () -> report.reThrow(RuntimeException.class));
    }

    @Test
    final void reThrow_IOException() {
        assertThrows(IOException.class, () -> report.reThrow(Exception.class, RuntimeException.class));
    }

    @Test
    final void reThrow_OutOfMemoryError() {
        assertThrows(AssertionError.class, () -> report.reThrow(Error.class,
                                                                OutOfMemoryError.class,
                                                                InternalError.class));
    }

    @Test
    final void reThrowAny() {
        assertThrows(OutOfMemoryError.class, () -> report.reThrowAny());
    }

    @Test
    final void reThrow_none() {
        assertSame(report, report.reThrow(UnsupportedOperationException.class));
    }
}
