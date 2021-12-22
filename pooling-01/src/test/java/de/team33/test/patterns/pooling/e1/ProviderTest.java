package de.team33.test.patterns.pooling.e1;

import de.team33.patterns.exceptional.e1.XFunction;
import de.team33.patterns.pooling.e1.Provider;
import de.team33.patterns.testing.e1.Parallel;
import de.team33.patterns.testing.e1.Report;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class ProviderTest {

    private static final int MAX = 100;

    private final AtomicInteger nextInt = new AtomicInteger(0);
    private final Provider<Dispenser> provider = new Provider<>(this::newDispenser);

    private Dispenser newDispenser() {
        return new Dispenser(nextInt.incrementAndGet());
    }

    @Test
    final void run() throws InterruptedException {
        final XFunction<Integer, Integer, RuntimeException> operation = index -> {
            final List<Integer> results = new ArrayList<>(0);
            provider.run(dsp -> results.add(dsp.getValue()));
            return results.get(0);
        };
        final Report<Integer> report = Parallel.apply(MAX, operation);
        assertEquals(MAX, report.getResults().size());
        assertTrue(1 < report.getResults().stream().reduce(0, Math::max));
        assertEquals(0, report.getThrowables().size());
    }

    @Test
    final void runEx() throws InterruptedException {
        final XFunction<Integer, Integer, InterruptedException> operation = index -> {
            final List<Integer> results = new ArrayList<>(0);
            provider.runEx(dsp -> {
                results.add(dsp.getValue());
                Thread.sleep(2);
            });
            return results.get(0);
        };
        final Report<Integer> report = Parallel.apply(MAX, operation);
        assertEquals(MAX, report.getResults().size());
        assertTrue(1 < report.getResults().stream().reduce(0, Math::max));
        assertEquals(0, report.getThrowables().size());
    }

    @Test
    final void get() {
        for (int i = 0; i < MAX; ++i) {
            assertEquals(Integer.valueOf(1), provider.get(Dispenser::getValue));
        }
    }

    @Test
    final void getEx() throws InterruptedException {
        for (int i = 0; i < MAX; ++i) {
            assertEquals(Integer.valueOf(1), provider.getEx(dsp -> {
                Thread.sleep(1);
                return dsp.getValue();
            }));
        }
    }
}
