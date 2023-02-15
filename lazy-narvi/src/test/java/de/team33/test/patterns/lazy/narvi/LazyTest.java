package de.team33.test.patterns.lazy.narvi;

import de.team33.patterns.lazy.narvi.Lazy;
import de.team33.patterns.testing.titan.Parallel;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LazyTest {

    private static final String ZERO_SMALLER_FIRST =
            "Although <lazy> already exists, the date <zero> must be smaller than the (first) evaluation of <lazy>";

    private int counter = 0;
    private final Lazy<Date> lazy = Lazy.init(Lazy.supplier(() -> {
        counter += 1;
        Thread.sleep(1);
        return new Date();
    }));

    @Test
    final void getFirst() throws InterruptedException {
        final Date zero = new Date();
        Thread.sleep(1);
        assertTrue(zero.compareTo(lazy.get()) < 0, ZERO_SMALLER_FIRST);
    }

    @Test
    final void getSame() {
        assertSame(lazy.get(), lazy.get(),
                   "If <lazy> is evaluated several times, the result must always be the same.");
    }

    @SuppressWarnings("CodeBlock2Expr")
    @Test
    final void getSame_parallel() throws Exception {
        final List<Date> results = Parallel.stream(16, ignored -> lazy.get())
                                           .collect(Collectors.toList());
        results.forEach(left -> {
            results.forEach(right -> {
                assertSame(left, right,
                           "If <lazy> is evaluated several times in parallel, the result must always be the same.");
            });
        });
    }

    @Test
    final void getSequential() {
        assertEquals(0, counter);
        for (int i = 0; i < 100; i++) {
            lazy.get();
        }
        assertEquals(1, counter);
    }

    @Test
    final void getParallel() throws Exception {
        assertEquals(0, counter);
        Parallel.report(100, ignored -> lazy.get())
                .reThrowAny();
        assertEquals(1, counter);
    }
}
