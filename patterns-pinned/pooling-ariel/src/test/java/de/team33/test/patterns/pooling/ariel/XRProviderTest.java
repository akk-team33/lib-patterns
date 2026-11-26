package de.team33.test.patterns.pooling.ariel;

import de.team33.patterns.pooling.ariel.XRProvider;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static de.team33.patterns.exceptional.dione.Conversion.function;
import static org.junit.jupiter.api.Assertions.*;


class XRProviderTest {

    private static final int MAX = 500;
    private static final long IDLE_TIME = 10; // milliseconds!
    private static final long LIFE_TIME = 100; // milliseconds!

    private final XRProvider<AtomicInteger, SQLException> provider = //
            new XRProvider<>(XRProviderTest::newAtomicInteger, IDLE_TIME, LIFE_TIME);
    private final XRProvider<AtomicInteger, SQLException> failing = //
            new XRProvider<>(XRProviderTest::newSQLException, IDLE_TIME, LIFE_TIME);

    private static AtomicInteger newAtomicInteger() {
        return new AtomicInteger();
    }

    private static AtomicInteger newSQLException() throws SQLException {
        throw new SQLException("failing");
    }

    @Test
    final void run_throwing_X() {
        assertThrows(IOException.class, () -> provider.runEx(atom -> {
            throw new IOException();
        }));
    }

    @Test
    final void run_throwing_R() {
        assertThrows(SQLException.class, () -> failing.run(atom -> {
        }));
    }

    @Test
    final void runEx_throwing_R() {
        assertThrows(SQLException.class, () -> failing.runEx(atom -> {
        }));
    }

    @Test
    final void get_throwing_R() {
        assertThrows(SQLException.class, () -> failing.get(AtomicInteger::get));
    }

    @Test
    final void getEx_throwing_R() {
        assertThrows(SQLException.class, () -> failing.getEx(AtomicInteger::get));
    }

    @Test
    final void run_sequential() {
        assertEquals(0, provider.size(),
                     "Before the first access, the provider must not contain any subjects.");
        final int max = IntStream.range(0, MAX)
                                 .map(index -> {
                                     final int[] result = {-1};
                                     try {
                                         provider.run(atom -> result[0] = atom.incrementAndGet());
                                     } catch (final SQLException e) {
                                         fail("should not really occur: " + e);
                                     }
                                     return result[0];
                                 })
                                 .reduce(Math::max)
                                 .orElse(-1);
        assertEquals(MAX, max,
                     "After multiple sequential accesses," +
                             " the largest result must correspond exactly to the number of accesses.");
        assertEquals(1, provider.size(),
                     "After multiple sequential access, the provider may still only contain one subject.");
    }

    @Test
    final void run_parallel() {
        assertEquals(0, provider.size(),
                     "Before the first access, the provider must not contain any subjects.");
        final int max = IntStream.range(0, MAX)
                                 .parallel()
                                 .map(index -> {
                                     final int[] result = {-1};
                                     try {
                                         provider.run(atom -> result[0] = atom.incrementAndGet());
                                     } catch (final SQLException e) {
                                         fail("should not really occur: " + e);
                                     }
                                     return result[0];
                                 })
                                 .reduce(Math::max)
                                 .orElse(-1);
        assertTrue(MAX > max,
                   "After multiple parallel accesses," +
                           " the largest result must be less than the number of accesses.");
        assertTrue(1 < provider.size(),
                   "After multiple parallel access, the provider must contain more than one subject.");
        assertTrue(MAX > provider.size(),
                   "After MAX parallel accesses, the provider must contain less than MAX subjects.");
    }

    @Test
    final void runEx_sequential() {
        assertEquals(0, provider.size(),
                     "Before the first access, the provider must not contain any subjects.");
        final int max = IntStream.range(0, MAX)
                                 .boxed()
                                 .map(function(index -> {
                                     final int[] result = {-1};
                                     provider.runEx(atom -> {
                                         Thread.sleep(0);
                                         result[0] = atom.incrementAndGet();
                                     });
                                     return result[0];
                                 }))
                                 .reduce(Math::max)
                                 .orElse(-1);
        assertEquals(MAX, max,
                     "After multiple sequential accesses," +
                             " the largest result must correspond exactly to the number of accesses.");
        assertEquals(1, provider.size(),
                     "After multiple sequential access, the provider may still only contain one subject.");
    }

    @Test
    final void runEx_parallel() {
        assertEquals(0, provider.size(),
                     "Before the first access, the provider must not contain any subjects.");
        final int max = IntStream.range(0, MAX)
                                 .boxed()
                                 .parallel()
                                 .map(function(index -> {
                                     final int[] result = {-1};
                                     provider.runEx(atom -> {
                                         Thread.sleep(0);
                                         result[0] = atom.incrementAndGet();
                                     });
                                     return result[0];
                                 }))
                                 .reduce(Math::max)
                                 .orElse(-1);
        assertTrue(MAX > max,
                   "After multiple parallel accesses," +
                           " the largest result must be less than the number of accesses.");
        assertTrue(1 < provider.size(),
                   "After multiple parallel access, the provider must contain more than one subject.");
        assertTrue(MAX > provider.size(),
                   "After MAX parallel accesses, the provider must contain less than MAX subjects.");
    }

    @Test
    final void get_sequential() {
        assertEquals(0, provider.size(),
                     "Before the first access, the provider must not contain any subjects.");
        final int max = IntStream.range(0, MAX)
                                 .map(index -> {
                                     try {
                                         return provider.get(AtomicInteger::incrementAndGet);
                                     } catch (final SQLException e) {
                                         fail("should not really occur: " + e);
                                         return -1;
                                     }
                                 })
                                 .reduce(Math::max)
                                 .orElse(-1);
        assertEquals(MAX, max,
                     "After multiple sequential accesses," +
                             " the largest result must correspond exactly to the number of accesses.");
        assertEquals(1, provider.size(),
                     "After multiple sequential accesses, the provider still needs to contain only one subject.");
    }

    @Test
    final void get_parallel() {
        assertEquals(0, provider.size(),
                     "Before the first access, the provider must not contain any subjects.");
        final int max = IntStream.range(0, MAX)
                                 .parallel()
                                 .map(index -> {
                                     try {
                                         return provider.get(AtomicInteger::incrementAndGet);
                                     } catch (final SQLException e) {
                                         fail("should not really occur: " + e);
                                         return -1;
                                     }
                                 })
                                 .reduce(Math::max)
                                 .orElse(-1);
        assertTrue(MAX > max);
        assertTrue(1 < provider.size(),
                   "After multiple parallel access, the provider must contain more than one subject.");
        assertTrue(MAX > provider.size(),
                   "After MAX parallel accesses, the provider must contain less than MAX subjects.");
    }

    @Test
    final void getEx_sequential() {
        assertEquals(0, provider.size(),
                     "Before the first access, the provider must not contain any subjects.");
        final int max = IntStream.range(0, MAX)
                                 .map(index -> {
                                     try {
                                         return provider.getEx(AtomicInteger::incrementAndGet);
                                     } catch (final SQLException e) {
                                         fail("should not really occur: " + e);
                                         return -1;
                                     }
                                 })
                                 .reduce(Math::max)
                                 .orElse(-1);
        assertEquals(MAX, max,
                     "After multiple sequential accesses," +
                             " the largest result must correspond exactly to the number of accesses.");
        assertEquals(1, provider.size(),
                     "After multiple sequential accesses, the provider still needs to contain only one subject.");
    }

    @Test
    final void getEx_parallel() {
        assertEquals(0, provider.size(),
                     "Before the first access, the provider must not contain any subjects.");
        final int result = IntStream.range(0, MAX)
                                    .parallel()
                                    .map(index -> {
                                        try {
                                            return provider.getEx(AtomicInteger::incrementAndGet);
                                        } catch (final SQLException e) {
                                            fail("should not really occur: " + e);
                                            return -1;
                                        }
                                    })
                                    .reduce(Math::max)
                                    .orElse(-1);
        assertTrue(MAX > result);
        assertTrue(1 < provider.size(),
                   "After multiple parallel access, the provider must contain more than one subject.");
        assertTrue(MAX > provider.size(),
                   "After MAX parallel accesses, the provider must contain less than MAX subjects.");
    }
}
