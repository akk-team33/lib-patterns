package de.team33.patterns.lazy.narvi.publics;

import de.team33.patterns.exceptional.dione.XSupplier;
import de.team33.patterns.lazy.narvi.Lazy;
import de.team33.testing.async.thebe.Parallel;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.team33.patterns.exceptional.dione.Conversion.supplier;
import static org.junit.jupiter.api.Assertions.*;

abstract class LazyTestBase<T> {


    private final AtomicInteger counter = new AtomicInteger(0);
    private final XSupplier<Integer, ?> initial = () -> {
        // This operation should take a little time and give other threads a chance ...
        Thread.sleep(2);
        return counter.incrementAndGet();
    };
    private final XSupplier<Integer, Exception> lazyIndex;
    private final XSupplier<Integer, Exception> badLazy = new XSupplier<>() {

        private Integer value;

        @Override
        public Integer get() throws Exception {
            if (null == value) {
                value = initial.get();
            }
            return value;
        }
    };
    final T subject;

    LazyTestBase(final Input<T> input) {
        this.subject = input.toSubject.apply(initial);
        this.lazyIndex = input.toSupplier.apply(subject);
    }

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
    final void get_lateBound() throws Exception {
        assertEquals(1, counter.incrementAndGet(),
                     "this direct access is expected to be the first access");
        assertEquals(2, lazyIndex.get(),
                     "this indirect access is expected to be the second access");
    }

    /**
     * Ensures that a {@link Lazy} instance with sequential access always returns the same result value,
     * or more precisely, always returns the same result instance as the first time.
     */
    @Test
    final void get_same_sequential() {
        final List<Integer> results = Stream.generate(supplier(lazyIndex))
                                            .limit(100)
                                            .toList();
        final Integer expected = results.get(0);
        results.forEach(result -> assertSame(expected, result));
    }

    /**
     * Ensures that a {@link Lazy} instance with parallel access always returns the same result value,
     * or more precisely, always returns the same result instance as the first time.
     */
    @Test
    final void get_same_parallel() throws Exception {
        final List<Integer> results = Parallel.stream(100, context -> lazyIndex.get()).toList();
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

    record Input<T>(Function<XSupplier<Integer, ?>, T> toSubject,
                    Function<T, XSupplier<Integer, Exception>> toSupplier) {}
}
