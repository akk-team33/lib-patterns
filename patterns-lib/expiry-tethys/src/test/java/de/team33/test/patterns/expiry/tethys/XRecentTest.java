package de.team33.test.patterns.expiry.tethys;

import de.team33.patterns.expiry.tethys.XRecent;
import de.team33.patterns.testing.titan.Parallel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;
import static org.junit.jupiter.api.Assertions.*;

class XRecentTest {

    private static final long IDLE_TIME = 10; // milliseconds!
    private static final long LIFE_TIME = 100; // milliseconds!

    private AtomicInteger nextIndex;
    private XRecent<Sample, IOException> XRecent;

    @BeforeEach
    final void beforeEach() {
        nextIndex = new AtomicInteger(0);
        XRecent = new XRecent<>(() -> new Sample(nextIndex.getAndIncrement()), IDLE_TIME, LIFE_TIME);
    }

    private static void sleep(final long millis) {
        try {
            Thread.sleep(millis);
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(e);
        }
    }

    @Test
    final void get_immediately() throws IOException {
        final Sample first = XRecent.get();
        final Sample second = XRecent.get();
        assertSame(first, second, "it is expected to get the same instance twice");
        assertEquals(1, nextIndex.get());
    }

    @Test
    final void get_afterIdleTime() throws IOException {
        final Sample first = XRecent.get();
        sleep(IDLE_TIME + 1);
        final Sample second = XRecent.get();
        assertNotSame(first, second, "after <IDLETIME> it is not expected to get the same instance twice");
        assertNotEquals(first.getIndex(), second.getIndex(),
                        "after <IDLETIME> it is not expected to get the same index twice");
        assertEquals(2, nextIndex.get());
    }

    @Test
    final void get_afterLifeTime() throws IOException {
        final Sample first = XRecent.get();
        final Instant created = first.getCreated();
        Sample second = first;
        //noinspection ObjectEquality
        while (second == first) {
            sleep(IDLE_TIME / 2); // significantly less than IDLETIME
            second = XRecent.get();
        }
        final long delta = second.getCreated().toEpochMilli() - created.toEpochMilli();
        assertTrue(delta > LIFE_TIME,
                   () -> format("<delta> is expected to be greater than <LIFETIME> (%d) - but was %d",
                                LIFE_TIME, delta));
        assertEquals(2, nextIndex.get());
    }

    @Test
    final void get_parallel() throws Exception {
        // there must always be enough threads to be executed while others are sleeping ...
        final int limit = 100;
        final Instant time00 = Instant.now();
        final List<Result> results =
                Parallel.report(limit, context -> {
                            final Sample first = XRecent.get();
                            final Instant created = first.getCreated();
                            Sample second = first;
                            //noinspection ObjectEquality
                            while (second == first) {
                                sleep(IDLE_TIME + 1); // sic!
                                second = XRecent.get();
                            }
                            final long delta =
                                    second.getCreated().toEpochMilli() -
                                    created.toEpochMilli();
                            return new Result(context.operationIndex, delta);
                        })
                        .reThrowAny()
                        .getResults();
        final long delta = Instant.now().toEpochMilli() - time00.toEpochMilli();
        final long maxExpected = limit * LIFE_TIME;
        assertTrue(delta < maxExpected, () -> format(" <delta> is expected to be less than" +
                                                     " <maxExpected> (%d) - but was %d", maxExpected, delta));
        final String unexpected = results.stream()
                                         .filter(result -> result.delta() < LIFE_TIME)
                                         .map(result -> format("[index: %02d] " +
                                                               "<delta> is expected to be greater than or " +
                                                               "equal to <LIFETIME> (%d) - but was %d",
                                                               result.index(), LIFE_TIME, result.delta()))
                                         .collect(joining(format("%n")));
        assertEquals("", unexpected);
    }

    private static class Sample {

        private final int index;
        private final Instant created;

        Sample(int index) throws IOException {
            this.index = index;
            created = Instant.now();
        }

        final Instant getCreated() {
            return created;
        }

        final int getIndex() {
            return index;
        }
    }
}
