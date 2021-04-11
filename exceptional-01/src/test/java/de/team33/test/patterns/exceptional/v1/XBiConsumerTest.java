package de.team33.test.patterns.exceptional.v1;

import de.team33.patterns.exceptional.v1.XBiConsumer;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

import static org.junit.Assert.*;

/**
 * Test to demonstrate how to treat a BiConsumer as XBiConsumer.
 * <p>
 * BiConsumer/XBiConsumer were chosen as an example for all functional constructs defined in this library.
 */
public class XBiConsumerTest {

    private static final BiConsumer<AtomicInteger, int[]> CONSUMER = (p1, p2) -> p2[0] = p1.incrementAndGet();

    private static <T, U, X extends Exception> void perform(final XBiConsumer<T, U, X> consumer,
                                                            final T t, final U u) throws X {
        consumer.accept(t, u);
    }

    @Test
    public final void treatBiConsumerAsXBiConsumer() {
        final AtomicInteger atomic = new AtomicInteger(0);
        final int[] array = {0};

        // treat BiConsumer as is ...
        CONSUMER.accept(atomic, array);
        assertEquals("<atomic> is expected to be incremented (0 -> 1)", 1, atomic.get());
        assertEquals("<array[0]> is expected to be incremented (0 -> 1)", 1, array[0]);

        // treat BiConsumer as XBiConsumer ...
        perform(CONSUMER::accept, atomic, array);
        assertEquals("<atomic> is expected to be incremented (1 -> 2)", 2, atomic.get());
        assertEquals("<array[0]> is expected to be incremented (1 -> 2)", 2, array[0]);
    }
}
