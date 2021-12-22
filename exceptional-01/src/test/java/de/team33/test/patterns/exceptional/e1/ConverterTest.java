package de.team33.test.patterns.exceptional.e1;

import de.team33.patterns.exceptional.e1.Converter;
import de.team33.patterns.exceptional.e1.WrappedException;
import de.team33.patterns.exceptional.e1.Wrapping;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.fail;


class ConverterTest {

    private static <X extends Exception>
    String rise(final Function<? super String, X> newException, final Object... args) throws X {
        throw newException.apply("args: " + Arrays.asList(args));
    }

    @ParameterizedTest
    @EnumSource(Case.class)
    final void runnable(final Case cs) {
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
