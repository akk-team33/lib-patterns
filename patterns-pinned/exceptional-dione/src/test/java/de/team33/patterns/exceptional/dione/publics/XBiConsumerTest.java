package de.team33.patterns.exceptional.dione.publics;

import de.team33.patterns.exceptional.dione.XBiConsumer;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test to demonstrate how to treat a BiConsumer as XBiConsumer.
 * <p>
 * BiConsumer/XBiConsumer were chosen as an example for all functional constructs defined in this library.
 */
class XBiConsumerTest {

    private static final BiConsumer<AtomicInteger, int[]> CONSUMER = (p1, p2) -> p2[0] = p1.incrementAndGet();

    private static <T, U, X extends Exception> void perform(final XBiConsumer<T, U, X> consumer,
                                                            final T t, final U u) throws X {
        consumer.accept(t, u);
    }

    @Test
    final void treatBiConsumerAsXBiConsumer() {
        final AtomicInteger atomic = new AtomicInteger(0);
        final int[] array = {0};

        // treat BiConsumer as is ...
        CONSUMER.accept(atomic, array);
        assertEquals(1, atomic.get(), "<atomic> is expected to be incremented (0 -> 1)");
        assertEquals(1, array[0], "<array[0]> is expected to be incremented (0 -> 1)");

        // treat BiConsumer as XBiConsumer ...
        perform(CONSUMER::accept, atomic, array);
        assertEquals(2, atomic.get(), "<atomic> is expected to be incremented (1 -> 2)");
        assertEquals(2, array[0], "<array[0]> is expected to be incremented (1 -> 2)");
    }
}
