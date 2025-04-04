package de.team33.patterns.exceptional.dione.publics;

import de.team33.patterns.exceptional.dione.Conversion;
import de.team33.patterns.exceptional.dione.WrappedException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.fail;


@SuppressWarnings("NewExceptionWithoutArguments")
class ConversionTest {

    private static <X extends Exception>
    String rise(final Function<? super String, X> newException, final Object... args) throws X {
        throw newException.apply("args: " + Arrays.asList(args));
    }

    @Test
    final void runnable() {
        try {
            Conversion.runnable(() -> rise(IOException::new))
                      .run();
            fail("expected to fail but worked");
        } catch (final RuntimeException e) {
            assertSame(WrappedException.class, e.getClass());
            assertSame(IOException.class, e.getCause().getClass());
            assertEquals("args: []", e.getCause().getMessage());
        }
    }

    @Test
    final void consumer() {
        try {
            Conversion.consumer(t -> rise(IOException::new, t))
                      .accept(278);
            fail("expected to fail but worked");
        } catch (final RuntimeException e) {
            assertSame(WrappedException.class, e.getClass());
            assertSame(IOException.class, e.getCause().getClass());
            assertEquals("args: [278]", e.getCause().getMessage());
        }
    }

    @Test
    final void biConsumer() {
        try {
            Conversion.biConsumer((t, u) -> rise(IOException::new, t, u))
                      .accept(3.141592654, "a string");
            fail("expected to fail but worked");
        } catch (final RuntimeException e) {
            assertSame(WrappedException.class, e.getClass());
            assertSame(IOException.class, e.getCause().getClass());
            assertEquals("args: [3.141592654, a string]", e.getCause().getMessage());
        }
    }

    @Test
    final void supplier() {
        try {
            final String result = Conversion.supplier(() -> rise(IOException::new))
                                            .get();
            fail("expected to fail but was " + result);
        } catch (final RuntimeException e) {
            assertSame(WrappedException.class, e.getClass());
            assertSame(IOException.class, e.getCause().getClass());
            assertEquals("args: []", e.getCause().getMessage());
        }
    }

    @Test
    final void predicate() {
        try {
            final boolean result = Conversion.predicate(t -> null == rise(IOException::new, t))
                                             .test(null);
            fail("expected to fail but was " + result);
        } catch (final RuntimeException e) {
            assertSame(WrappedException.class, e.getClass());
            assertSame(IOException.class, e.getCause().getClass());
            assertEquals("args: [null]", e.getCause().getMessage());
        }
    }

    @Test
    final void biPredicate() {
        try {
            final boolean result = Conversion.biPredicate((t, u) -> null == rise(IOException::new, t, u))
                                             .test(0, 'x');
            fail("expected to fail but was " + result);
        } catch (final RuntimeException e) {
            assertSame(WrappedException.class, e.getClass());
            assertSame(IOException.class, e.getCause().getClass());
            assertEquals("args: [0, x]", e.getCause().getMessage());
        }
    }

    @Test
    final void function() {
        try {
            final String result = Conversion.function(t -> rise(IOException::new, t))
                                            .apply("another string");
            fail("expected to fail but was " + result);
        } catch (final RuntimeException e) {
            assertSame(WrappedException.class, e.getClass());
            assertSame(IOException.class, e.getCause().getClass());
            assertEquals("args: [another string]", e.getCause().getMessage());
        }
    }

    @Test
    final void biFunction() {
        try {
            final String result = Conversion.biFunction((t, u) -> rise(IOException::new, t, u))
                                            .apply('a', 'b');
            fail("expected to fail but was " + result);
        } catch (final RuntimeException e) {
            assertSame(WrappedException.class, e.getClass());
            assertSame(IOException.class, e.getCause().getClass());
            assertEquals("args: [a, b]", e.getCause().getMessage());
        }
    }

    @Test
    final void run() {
        // regular ...
        final AtomicInteger i = new AtomicInteger(0);
        Conversion.run(i::incrementAndGet);
        assertEquals(1, i.get());

        // exceptional ...
        try {
            Conversion.run(() -> {
                throw new IOException();
            });
            fail("expected to fail with a WrappedException");
        } catch (final WrappedException e) {
            assertInstanceOf(IOException.class, e.getCause());
        }
    }

    @Test
    final void get() {
        // regular ...
        final AtomicInteger i = new AtomicInteger(0);
        assertEquals(1, Conversion.get(i::incrementAndGet));

        // exceptional ...
        try {
            Conversion.get(() -> {
                throw new IOException();
            });
            fail("expected to fail with a WrappedException");
        } catch (final WrappedException e) {
            assertInstanceOf(IOException.class, e.getCause());
        }
    }
}
