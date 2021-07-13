package de.team33.test.patterns.lazy.v1;

import de.team33.patterns.lazy.v1.XLazy;
import org.junit.Test;

import java.util.Date;

import static de.team33.test.patterns.lazy.v1.Runner.parallel;
import static de.team33.test.patterns.lazy.v1.Runner.sequential;
import static org.junit.Assert.*;

public class XLazyTest {

    private static final String ZERO_SMALLER_FIRST =
            "Although <lazy> already exists, the date <zero> must be smaller than the (first) evaluation of <lazy>";

    private int counter = 0;
    private final XLazy<Date, InterruptedException> lazy = new XLazy<>(() -> {
        counter += 1;
        Thread.sleep(1);
        return new Date();
    });

    @Test
    public final void getFirst() throws InterruptedException {
        final Date zero = new Date();
        Thread.sleep(1);
        assertTrue(ZERO_SMALLER_FIRST, zero.compareTo(lazy.get()) < 0);
    }

    @Test
    public final void getSame() throws InterruptedException {
        assertSame("If <lazy> is evaluated several times, the result must always be the same.", lazy.get(), lazy.get());
    }

    @Test
    public final void getSequential() throws InterruptedException {
        assertEquals(0, counter);
        sequential(100, lazy::get);
        assertEquals(1, counter);
    }

    @Test
    public final void getParallel() throws InterruptedException {
        assertEquals(0, counter);
        parallel(100, lazy::get);
        assertEquals(1, counter);
    }
}
