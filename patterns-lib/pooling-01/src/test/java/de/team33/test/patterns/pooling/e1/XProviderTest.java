package de.team33.test.patterns.pooling.e1;

import de.team33.patterns.pooling.e1.XProvider;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static de.team33.patterns.exceptional.dione.Conversion.function;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class XProviderTest {

    private static final int MAX = 500;

    private static XProvider<AtomicInteger, InterruptedException> newXProvider() {
        return new XProvider<>(() -> {
            Thread.sleep(0);
            return new AtomicInteger();
        });
    }

    @Test
    final void run_sequential() {
        final XProvider<AtomicInteger, InterruptedException> provider = newXProvider();
        assertEquals(0, provider.size(),
                     "Before the first access, the provider must not contain any subjects.");
        final int max = IntStream.range(0, MAX)
                                 .boxed()
                                 .map(function(index -> {
                                     final int[] result = {-1};
                                     provider.run(atom -> result[0] = atom.incrementAndGet());
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
    final void run_parallel() {
        final XProvider<AtomicInteger, InterruptedException> provider = newXProvider();
        assertEquals(0, provider.size(),
                     "Before the first access, the provider must not contain any subjects.");
        final int max = IntStream.range(0, MAX)
                                 .boxed()
                                 .parallel()
                                 .map(function(index -> {
                                     final int[] result = {-1};
                                     provider.run(atom -> result[0] = atom.incrementAndGet());
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
    final void runEx_sequential() {
        final XProvider<AtomicInteger, InterruptedException> provider = newXProvider();
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
        final XProvider<AtomicInteger, InterruptedException> provider = newXProvider();
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
        final XProvider<AtomicInteger, InterruptedException> provider = newXProvider();
        assertEquals(0, provider.size(),
                     "Before the first access, the provider must not contain any subjects.");
        final int max = IntStream.range(0, MAX)
                                 .boxed()
                                 .map(function(index -> provider.get(AtomicInteger::incrementAndGet)))
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
        final XProvider<AtomicInteger, InterruptedException> provider = newXProvider();
        assertEquals(0, provider.size(),
                     "Before the first access, the provider must not contain any subjects.");
        final int result = IntStream.range(0, MAX)
                                    .boxed()
                                    .parallel()
                                    .map(function(index -> provider.get(AtomicInteger::incrementAndGet)))
                                    .reduce(Math::max)
                                    .orElse(-1);
        assertTrue(MAX > result);
        assertTrue(1 < provider.size(),
                   "After multiple parallel access, the provider must contain more than one subject.");
        assertTrue(MAX > provider.size(),
                   "After MAX parallel accesses, the provider must contain less than MAX subjects.");
    }

    @Test
    final void getEx_sequential() {
        final XProvider<AtomicInteger, InterruptedException> provider = newXProvider();
        assertEquals(0, provider.size(),
                     "Before the first access, the provider must not contain any subjects.");
        final int max = IntStream.range(0, MAX)
                                 .boxed()
                                 .map(function(index -> provider.getEx(AtomicInteger::incrementAndGet)))
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
        final XProvider<AtomicInteger, InterruptedException> provider = newXProvider();
        assertEquals(0, provider.size(),
                     "Before the first access, the provider must not contain any subjects.");
        final int result = IntStream.range(0, MAX)
                                    .boxed()
                                    .parallel()
                                    .map(function(index -> provider.getEx(AtomicInteger::incrementAndGet)))
                                    .reduce(Math::max)
                                    .orElse(-1);
        assertTrue(MAX > result);
        assertTrue(1 < provider.size(),
                   "After multiple parallel access, the provider must contain more than one subject.");
        assertTrue(MAX > provider.size(),
                   "After MAX parallel accesses, the provider must contain less than MAX subjects.");
    }
}
