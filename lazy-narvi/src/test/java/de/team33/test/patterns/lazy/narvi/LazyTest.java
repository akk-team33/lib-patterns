package de.team33.test.patterns.lazy.narvi;

import de.team33.patterns.lazy.narvi.Lazy;
import de.team33.patterns.testing.titan.Parallel;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LazyTest {

    private final Sequential sequential = new Sequential();
    private final Random random = new Random();
    private final Lazy<Integer> randLazy = Lazy.init(Lazy.supplier(() -> {
        // should take some time ...
        Thread.sleep(1);
        return random.nextInt();
    }));

    /**
     * Ensures that the initial code associated with a {@link Lazy} instance is not executed until the
     * {@link Lazy#get()} method is first called.
     */
    @Test
    final void get_lateBound() {
        final Lazy<Integer> lazy = Lazy.init(sequential::nextInt);
        assertEquals(1, sequential.nextInt(), "this direct access is expected to be the first access");
        assertEquals(2, lazy.get(), "this indirect access is expected to be the second access");
    }

    /**
     * Ensures that a {@link Lazy} instance with sequential access always returns the same result value,
     * or more precisely, always returns the same result instance as the first time.
     */
    @Test
    final void get_same_sequential() {
        final Lazy<Integer> lazy = Lazy.init(random::nextInt);
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
        final Lazy<Integer> lazy = Lazy.init(random::nextInt);
        final List<Integer> results = Parallel.stream(100, ignored -> lazy.get())
                                              .collect(Collectors.toList());
        final Integer expected = results.get(0);
        results.forEach(result -> assertSame(expected, result));
    }

    @Test
    final void exceptional() {
        final Lazy<Object> lazy = Lazy.init(Lazy.supplier(() -> {
            throw new SQLException("this is a test");
        }));
        assertThrows(Lazy.InitException.class, lazy::get);
    }

    private static final class Sequential {

        private final AtomicInteger atomic = new AtomicInteger(0);

        final int nextInt() {
            return atomic.incrementAndGet();
        }
    }
}
