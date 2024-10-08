package de.team33.test.patterns.lazy.narvi;

import de.team33.patterns.lazy.narvi.XLazy;
import de.team33.testing.async.thebe.Parallel;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class XLazyTest {

    private static final String ZERO_SMALLER_FIRST =
            "Although <lazy> already exists, the date <zero> must be smaller than the (first) evaluation of <lazy>";

    private int counter = 0;
    private final XLazy<Date, InterruptedException> lazy = XLazy.init(() -> {
        counter += 1;
        Thread.sleep(1);
        return new Date();
    });

    @Test
    final void getFirst() throws InterruptedException {
        final Date zero = new Date();
        Thread.sleep(1);
        assertTrue(zero.compareTo(lazy.get()) < 0, ZERO_SMALLER_FIRST);
    }

    @Test
    final void getSame() throws InterruptedException {
        assertSame(lazy.get(), lazy.get(),
                   "If <lazy> is evaluated several times, the result must always be the same.");
    }

    @Test
    final void getSequential() throws Exception {
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
