package de.team33.test.patterns.refreshing.e1;

import de.team33.patterns.refreshing.e1.Recent;
import de.team33.patterns.refreshing.e1.Recent.Rule;
import de.team33.patterns.testing.e1.Parallel;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;
import java.util.logging.Logger;

import static de.team33.patterns.refreshing.e1.Recent.rule;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RecentTest {

    private static final Logger LOG = Logger.getLogger(RecentTest.class.getCanonicalName());
    private static final String DURATION_TOO_SHORT = "duration is too short in relation to life span," +
                                                     " increase number of executions!";
    private static final int LIFE_SPAN = 25;
    private static final int MAX_LIFE_SPAN = LIFE_SPAN + 25;

    @Test
    final void get_new_if_timeout() throws Exception {
        final Supplier<Subject> recentSubject = new Recent<>(rule(() -> new Subject(MAX_LIFE_SPAN), LIFE_SPAN));
        final long time0 = System.currentTimeMillis();
        final Subject first = recentSubject.get();
        Parallel.apply(100, i -> {
                    for (Subject next = first; first == next; next = recentSubject.get()) {
                        // LOG.info("same");
                    }
                    return System.currentTimeMillis() - time0;
                })
                .reThrowAny()
                .getResults()
                .forEach(delta -> assertTrue(delta > LIFE_SPAN, () -> "delta = " + delta));
    }

    @Test
    final void get_duration_without_close() throws Exception {
        final Supplier<Subject> recentSubject = new Recent<>(rule(() -> new Subject(MAX_LIFE_SPAN), LIFE_SPAN));
        Subject.NEXT_ID.set(0);

        final long duration = Parallel.invoke(1000000, 100, index -> recentSubject.get().getId())
                                      .reThrowAny()
                                      .getDuration();

        final long nSubjects = Subject.NEXT_ID.get();
        assertTrue(nSubjects > 3, DURATION_TOO_SHORT);
        final long maxSubjects = (duration / LIFE_SPAN) + 1;
        assertTrue(nSubjects <= maxSubjects, () -> "nSubjects = " + nSubjects + ", maxSubjects = " + maxSubjects);
    }

    @Test
    final void get_duration_with_close() throws Exception {
        final Rule<Subject> rule = rule(() -> new Subject(MAX_LIFE_SPAN), Subject::close, LIFE_SPAN);
        final Supplier<Subject> recentSubject = new Recent<>(rule);
        Subject.NEXT_ID.set(0);

        final long duration = Parallel.invoke(1000000, 100, index -> recentSubject.get().getId())
                                      .reThrowAny()
                                      .getDuration();

        final long nSubjects = Subject.NEXT_ID.get();
        assertTrue(nSubjects > 3, DURATION_TOO_SHORT);
        final long maxSubjects = (duration / LIFE_SPAN) + 1;
        assertTrue(nSubjects <= maxSubjects, () -> "nSubjects = " + nSubjects + ", maxSubjects = " + maxSubjects);
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
            LOG.info(() -> String.format("closed Subject: id = %d", id));
        }
    }
}
