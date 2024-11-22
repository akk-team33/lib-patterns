package de.team33.patterns.exceptional.dione.publics;

import de.team33.patterns.exceptional.dione.Conversion;
import de.team33.patterns.exceptional.dione.Converter;
import de.team33.patterns.exceptional.dione.WrappedException;
import de.team33.patterns.exceptional.dione.Wrapping;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;


class ConverterTest {

    private static <X extends Exception>
    String rise(final Function<? super String, X> newException, final Object... args) throws X {
        throw newException.apply("args: " + Arrays.asList(args));
    }

    @ParameterizedTest
    @EnumSource(Case.class)
    final void runnable(final Case cs) {
        // normal ...
        final int[] ints = {0};
        cs.wrapper.runnable(() -> ints[0] += 1).run();
        assertEquals(1, ints[0]);

        // exceptional ...
        try {
            cs.wrapper.runnable(() -> rise(IOException::new))
                      .run();
            fail("expected to fail but worked");
        } catch (final RuntimeException e) {
            assertSame(cs.runtimeExceptionType, e.getClass());
            assertSame(IOException.class, e.getCause().getClass());
            assertEquals("args: []", e.getCause().getMessage());
        }
    }

    @ParameterizedTest
    @EnumSource(Case.class)
    final void consumer(final Case cs) {
        // normal ...
        final int[] ints = {0};
        cs.wrapper.consumer((Integer i) -> ints[0] += i).accept(5);
        assertEquals(5, ints[0]);

        // exceptional ...
        try {
            cs.wrapper.consumer(t -> rise(IOException::new, t))
                      .accept(278);
            fail("expected to fail but worked");
        } catch (final RuntimeException e) {
            assertSame(cs.runtimeExceptionType, e.getClass());
            assertSame(IOException.class, e.getCause().getClass());
            assertEquals("args: [278]", e.getCause().getMessage());
        }
    }

    @ParameterizedTest
    @EnumSource(Case.class)
    final void biConsumer(final Case cs) {
        // normal ...
        final int[] ints = {0};
        cs.wrapper.biConsumer((Integer i, Integer k) -> ints[0] += i + k).accept(1, 2);
        assertEquals(3, ints[0]);

        // exceptional ...
        try {
            cs.wrapper.biConsumer((t, u) -> rise(IOException::new, t, u))
                      .accept(3.141592654, "a string");
            fail("expected to fail but worked");
        } catch (final RuntimeException e) {
            assertSame(cs.runtimeExceptionType, e.getClass());
            assertSame(IOException.class, e.getCause().getClass());
            assertEquals("args: [3.141592654, a string]", e.getCause().getMessage());
        }
    }

    @ParameterizedTest
    @EnumSource(Case.class)
    final void supplier(final Case cs) {
        // normal ...
        assertEquals(4, cs.wrapper.supplier(() -> 3 + 1).get());

        // exceptional ...
        try {
            final String result = cs.wrapper.supplier(() -> rise(IOException::new))
                                            .get();
            fail("expected to fail but was " + result);
        } catch (final RuntimeException e) {
            assertSame(cs.runtimeExceptionType, e.getClass());
            assertSame(IOException.class, e.getCause().getClass());
            assertEquals("args: []", e.getCause().getMessage());
        }
    }

    @ParameterizedTest
    @EnumSource(Case.class)
    final void predicate(final Case cs) {
        // normal ...
        assertTrue(cs.wrapper.predicate((Integer i) -> i % 2 == 0).test(8));

        // exceptional ...
        try {
            final boolean result = cs.wrapper.predicate(t -> null == rise(IOException::new, t))
                                             .test(null);
            fail("expected to fail but was " + result);
        } catch (final RuntimeException e) {
            assertSame(cs.runtimeExceptionType, e.getClass());
            assertSame(IOException.class, e.getCause().getClass());
            assertEquals("args: [null]", e.getCause().getMessage());
        }
    }

    @ParameterizedTest
    @EnumSource(Case.class)
    final void biPredicate(final Case cs) {
        // normal ...
        assertTrue(cs.wrapper.biPredicate((Integer i, Integer k) -> i % 2 == 0 && k % 2 == 1).test(8, 9));

        // exceptional ...
        try {
            final boolean result = cs.wrapper.biPredicate((t, u) -> null == rise(IOException::new, t, u))
                                             .test(0, 'x');
            fail("expected to fail but was " + result);
        } catch (final RuntimeException e) {
            assertSame(cs.runtimeExceptionType, e.getClass());
            assertSame(IOException.class, e.getCause().getClass());
            assertEquals("args: [0, x]", e.getCause().getMessage());
        }
    }

    @ParameterizedTest
    @EnumSource(Case.class)
    final void function(final Case cs) {
        // normal ...
        assertEquals(7, cs.wrapper.function((Integer i) -> i + 1).apply(6));

        // exceptional (unchecked) ...
        try {
            final String result = cs.wrapper.function(t -> rise(IllegalStateException::new, t))
                                            .apply("another string");
            fail("expected to fail but was " + result);
        } catch (final RuntimeException e) {
            assertSame(IllegalStateException.class, e.getClass());
            assertNull(e.getCause());
            assertEquals("args: [another string]", e.getMessage());
        }

        // exceptional ...
        try {
            final String result = cs.wrapper.function(t -> rise(IOException::new, t))
                                            .apply("another string");
            fail("expected to fail but was " + result);
        } catch (final RuntimeException e) {
            assertSame(cs.runtimeExceptionType, e.getClass());
            assertSame(IOException.class, e.getCause().getClass());
            assertEquals("args: [another string]", e.getCause().getMessage());
        }
    }

    @ParameterizedTest
    @EnumSource(Case.class)
    final void biFunction(final Case cs) {
        // normal ...
        assertEquals(7, cs.wrapper.biFunction(Integer::sum).apply(4, 3));

        // exceptional ...
        try {
            final String result = cs.wrapper.biFunction((t, u) -> rise(IOException::new, t, u))
                                            .apply('a', 'b');
            fail("expected to fail but was " + result);
        } catch (final RuntimeException e) {
            assertSame(cs.runtimeExceptionType, e.getClass());
            assertSame(IOException.class, e.getCause().getClass());
            assertEquals("args: [a, b]", e.getCause().getMessage());
        }
    }

    enum Case {

        WRAPPED_EXCEPTION(WrappedException.class,
                          Converter.using(WrappedException::new)),
        ILLEGAL_STATE_EXCEPTION(IllegalStateException.class,
                                Converter.using(Wrapping.method(IllegalStateException::new))),
        RUNTIME_EXCEPTION(RuntimeException.class,
                          Converter.using(Wrapping.varying(RuntimeException::new)));

        final Class<?> runtimeExceptionType;
        final Converter wrapper;

        Case(final Class<?> runtimeExceptionType, final Converter wrapper) {
            this.runtimeExceptionType = runtimeExceptionType;
            this.wrapper = wrapper;
        }
    }
}
