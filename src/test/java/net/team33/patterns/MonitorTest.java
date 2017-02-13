package net.team33.patterns;

import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class MonitorTest {

    @SuppressWarnings("ProhibitedExceptionThrown")
    private static void join(final Thread thread) {
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Test
    public final void runnableWithoutMonitor() {
        new Trial().testRunnable(
                runnable -> runnable,
                values -> Assert.assertTrue(1 < values.size()));
    }

    @Test
    public final void runnableWithMonitor() {
        final Monitor monitor = new Monitor();
        new Trial().testRunnable(
                monitor::runnable,
                values -> Assert.assertEquals(1, values.size()));
    }

    @Test
    public final void consumerWithoutMonitor() {
        new Trial().testConsumer(
                consumer -> consumer,
                values -> Assert.assertTrue(1 < values.size()));
    }

    @Test
    public final void consumerWithMonitor() {
        final Monitor monitor = new Monitor();
        new Trial().testConsumer(
                monitor::consumer,
                values -> Assert.assertEquals(1, values.size()));
    }

    @Test
    public final void biConsumerWithoutMonitor() {
        new Trial().testBiConsumer(
                biConsumer -> biConsumer,
                values -> Assert.assertTrue(1 < values.size()));
    }

    @Test
    public final void biConsumerWithMonitor() {
        final Monitor monitor = new Monitor();
        new Trial().testBiConsumer(
                monitor::biConsumer,
                values -> Assert.assertEquals(1, values.size()));
    }

    @Test
    public final void functionWithoutMonitor() {
        new Trial().testFunction(
                function -> function,
                values -> Assert.assertTrue(1 < values.size()));
    }

    @Test
    public final void functionWithMonitor() {
        final Monitor monitor = new Monitor();
        new Trial().testFunction(
                monitor::function,
                values -> Assert.assertEquals(1, values.size()));
    }

    @Test
    public final void biFunctionWithoutMonitor() {
        new Trial().testBiFunction(
                biFunction -> biFunction,
                values -> Assert.assertTrue(1 < values.size()));
    }

    @Test
    public final void biFunctionWithMonitor() {
        final Monitor monitor = new Monitor();
        new Trial().testBiFunction(
                monitor::biFunction,
                values -> Assert.assertEquals(1, values.size()));
    }

    @Test
    public final void supplierWithoutMonitor() {
        new Trial().testSupplier(
                supplier -> supplier,
                values -> Assert.assertTrue(1 < values.size()));
    }

    @Test
    public final void supplierWithMonitor() {
        final Monitor monitor = new Monitor();
        new Trial().testSupplier(
                monitor::supplier,
                values -> Assert.assertEquals(1, values.size()));
    }

    private static class Trial {

        private final Random random = new Random();
        private final Set<Integer> values = Collections.synchronizedSet(new HashSet<>(0));
        private final Mutable<Integer> mutable = new Mutable<>();

        final void testRunnable(final Function<Runnable, Runnable> map,
                                final Consumer<Set<?>> assertion) {
            test(map.apply(() -> consume(random.nextInt())), assertion);
        }

        final void testSupplier(final Function<Supplier<Integer>, Supplier<Integer>> map,
                                final Consumer<Set<?>> assertion) {
            test(() -> map.apply(() -> reply(random.nextInt())).get(), assertion);
        }

        final void testConsumer(final Function<Consumer<Integer>, Consumer<Integer>> map,
                                final Consumer<Set<?>> assertion) {
            test(() -> map.apply(this::consume).accept(random.nextInt()), assertion);
        }

        final void testBiConsumer(final Function<BiConsumer<Integer, Integer>, BiConsumer<Integer, Integer>> map,
                                  final Consumer<Set<?>> assertion) {
            test(() -> map.apply(this::biConsume).accept(random.nextInt(), random.nextInt()), assertion);
        }

        final void testFunction(final Function<Function<Integer, Integer>, Function<Integer, Integer>> map,
                                final Consumer<Set<?>> assertion) {
            test(() -> map.apply(this::reply).apply(random.nextInt()), assertion);
        }

        final void testBiFunction(
                final Function<BiFunction<Integer, Integer, Integer>, BiFunction<Integer, Integer, Integer>> map,
                final Consumer<Set<?>> assertion) {
            test(() -> map.apply(this::biReply).apply(random.nextInt(), random.nextInt()), assertion);
        }

        private Integer reply(final Integer value) {
            return biReply(value, value);
        }

        private Integer biReply(final Integer left, final Integer right) {
            biConsume(left, right);
            return left;
        }

        final void test(final Runnable runnable, final Consumer<Set<?>> assertion) {
            final List<Thread> threads = Stream
                    .generate(() -> new Thread(runnable))
                    .limit(25)
                    .collect(toList());
            threads.forEach(Thread::start);
            threads.forEach(MonitorTest::join);
            logValues();
            assertion.accept(values);
        }

        private void consume(final Integer value) {
            biConsume(value, value);
        }

        private void biConsume(final Integer left, final Integer right) {
            try {
                if (null == mutable.value) {
                    Thread.sleep(1);
                    mutable.value = left;
                    values.add(right);
                }
            } catch (InterruptedException e) {
                //noinspection ProhibitedExceptionThrown
                throw new RuntimeException(e.getMessage(), e);
            }
        }

        @SuppressWarnings("UseOfSystemOutOrSystemErr")
        private void logValues() {
            //System.out.println(values);
        }
    }

    private static final class Mutable<T> {
        private T value = null;
    }
}