package de.team33.test.patterns.refreshing.e1;

import de.team33.patterns.refreshing.e1.Recent;
import de.team33.patterns.refreshing.e1.Recent.Rule;
import de.team33.patterns.testing.e1.Parallel;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RecentTest {

    private static final Logger LOG = Logger.getLogger(RecentTest.class.getCanonicalName());
    private static final int LIFETIME = 10;
    private static final Supplier<Random> NEW_SUBJECT = () -> {
        final Random result = new Random();
        LOG.info("new: " + result);
        return result;
    };
    private static final Consumer<Random> OLD_SUBJECT = subject -> {
        Objects.requireNonNull(subject);
        LOG.info(() -> "old: " + subject);
    };
    private static final Rule<Random> PLAIN_RULE = Recent.rule(NEW_SUBJECT, LIFETIME);
    private static final Rule<Random> LOGGING_RULE = Recent.rule(NEW_SUBJECT,
                                                                 OLD_SUBJECT,
                                                                 LIFETIME);

    private final Recent<Random> recentRandom = new Recent<>(PLAIN_RULE);

    @RepeatedTest(100)
    final void get_delivers() {
        assertInstanceOf(Random.class, recentRandom.get());
    }

    @Test
    final void get_new_if_timeout() throws Exception {
        final Recent<Random> loggingRecent = new Recent<>(LOGGING_RULE);
        final long time0 = System.currentTimeMillis();
        final Random first = loggingRecent.get();
        Parallel.apply(100, i -> {
                    for (Random next = first; first == next; next = loggingRecent.get()) {
                        // LOG.info("same");
                    }
                    return System.currentTimeMillis() - time0;
                })
                .reThrowAny()
                .getResults()
                .forEach(delta -> assertTrue(delta > LIFETIME, () -> "delta = " + delta));
    }

    @Test
    final void get_without_close() throws Exception {
        final Recent<Subject> recentSubject = new Recent<>(Recent.rule(() -> new Subject(33), 30));
        Parallel.apply(100000, 100, index -> recentSubject.get().getId())
                .reThrowAny()
                .getResults()
                .forEach(result -> assertTrue(result < Subject.NEXT_ID.get()));
    }

    static class Subject {

        static final AtomicLong NEXT_ID = new AtomicLong(0);

        private final long id = NEXT_ID.getAndIncrement();
        private final long timeout;
        private volatile boolean closed = false;

        Subject(final long lifeSpan) {
            timeout = System.currentTimeMillis() + lifeSpan;
            LOG.info(() -> String.format("new Subject: id = %d, timeout = %d", id, timeout));
        }

        final long getId() {
            if (closed) {
                throw new IllegalStateException("closed");
            } else if (timeout < System.currentTimeMillis()) {
                throw new IllegalStateException("timeout exceeded: " + timeout);
            } else {
                return id;
            }
        }

        final void close() {
            this.closed = true;
            throw new UnsupportedOperationException("not yet implemented");
        }
    }
}
