package de.team33.test.patterns.lazy.narvi;

import de.team33.patterns.exceptional.dione.XSupplier;
import de.team33.patterns.lazy.narvi.Lazy;
import de.team33.testing.async.thebe.Parallel;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class LazyTest {


    private final AtomicInteger counter = new AtomicInteger(0);
    private final XSupplier<Integer, ?> initial = () -> {
        // This operation should take a little time and give other threads a chance ...
        Thread.sleep(1);
        return counter.incrementAndGet();
    };
    private final Lazy<Integer> lazy = Lazy.initEx(initial);
    private final XSupplier<Integer, ?> badLazy = new XSupplier<Integer, Exception>() {

        private Integer value = null;

        @Override
        public Integer get() throws Exception {
            if (null == value) {
                value = initial.get();
            }
            return value;
        }
    };

    /**
     * Ensures that {@link Lazy#init(Supplier)} can be called simply with a lambda expression.
     * (No conflicts with an overloaded method)
     */
    @Test
    final void init() {
        final Lazy<String> lazy = Lazy.init(() -> UUID.randomUUID().toString());
        assertEquals(lazy.get(), lazy.get());
    }

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
        final List<Integer> results = Parallel.stream(100, context -> lazy.get())
                                              .collect(Collectors.toList());
        final Integer expected = results.get(0);
        results.forEach(result -> assertSame(expected, result));
    }

    /**
     * Cross-checking {@link #get_same_parallel()} using a bad lazy implementation.
     */
    @Test
    final void get_same_parallel_crossChecking_with_badLazy() throws Exception {
        final Set<Integer> results = Parallel.stream(100, context -> badLazy.get())
                                             .collect(Collectors.toSet());
        assertNotEquals(1, results.size());
    }

    /**
     * Ensures that {@link Lazy#initEx(XSupplier)} encapsulates code so that any checked exception
     * that is thrown is wrapped in an (unchecked) {@link Lazy.InitException}.
     */
    @Test
    final void exceptional() {
        final Lazy<?> lazy = Lazy.initEx(() -> {
            throw new SQLException("this is a test");
        });
        assertThrows(Lazy.InitException.class, lazy::get);
    }
}
