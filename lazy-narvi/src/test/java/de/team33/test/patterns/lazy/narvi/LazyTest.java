package de.team33.test.patterns.lazy.narvi;

import de.team33.patterns.exceptional.e1.XSupplier;
import de.team33.patterns.lazy.narvi.Lazy;
import de.team33.patterns.testing.titan.Parallel;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LazyTest {


    private final AtomicInteger counter = new AtomicInteger(0);
    private final Lazy<Integer> lazy = Lazy.init(Lazy.supplier(() -> {
        // This operation should take a little time and give other threads a chance ...
        Thread.sleep(1);
        return counter.incrementAndGet();
    }));

    /**
     * Ensures that the initial code associated with a {@link Lazy} instance is not executed until the
     * {@link Lazy#get()} method is first called.
     */
    @Test
    final void get_lateBound() {
        assertEquals(1, counter.incrementAndGet(),
                     "this direct access is expected to be the first access");
        assertEquals(2, lazy.get(),
                     "this indirect access is expected to be the second access");
    }

    /**
     * Ensures that a {@link Lazy} instance with sequential access always returns the same result value,
     * or more precisely, always returns the same result instance as the first time.
     */
    @Test
    final void get_same_sequential() {
        final List<Integer> results = Stream.generate(lazy::get)
                                            .limit(100)
                                            .collect(Collectors.toList());
        final Integer expected = results.get(0);
        results.forEach(result -> assertSame(expected, result));
    }

    /**
     * Ensures that a {@link Lazy} instance with parallel access always returns the same result value,
     * or more precisely, always returns the same result instance as the first time.
     */
    @Test
    final void get_same_parallel() throws Exception {
        final List<Integer> results = Parallel.stream(100, ignored -> lazy.get())
                                              .collect(Collectors.toList());
        final Integer expected = results.get(0);
        results.forEach(result -> assertSame(expected, result));
    }

    /**
     * Ensures that {@link Lazy#supplier(XSupplier)} encapsulates code so that any checked exception
     * that is thrown is wrapped in an (unchecked) {@link Lazy.InitException}.
     */
    @Test
    final void exceptional() {
        final Lazy<?> lazy = Lazy.init(Lazy.supplier(() -> {
            throw new SQLException("this is a test");
        }));
        assertThrows(Lazy.InitException.class, lazy::get);
    }
}
