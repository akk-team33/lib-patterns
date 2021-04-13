package de.team33.test.patterns.exceptional.v1;

import de.team33.patterns.exceptional.v1.Handling;
import de.team33.patterns.exceptional.v1.WrappedException;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

@SuppressWarnings({"NestedTryStatement", "ThrowCaughtLocally"})
public class HandlingTest {

    public static final String EXPECTED_AN_EXCEPTION = "It is expected that an exception was thrown previously";
    public static final List<Exception> EXCEPTION_LIST = Arrays.asList(
            new IOException(), new SQLException(), new IllegalArgumentException(), new IllegalStateException()
    );

    private static <X extends Exception> void doThrow(final Supplier<X> supplier) throws X {
        throw supplier.get();
    }

    private static <X extends Exception> Function<Exception, X> causeWhen(final Class<X> xClass) {
        return caught -> Optional.ofNullable(caught.getCause())
                                 .filter(xClass::isInstance)
                                 .map(xClass::cast)
                                 .orElse(null);
    }

    private static <X extends Exception> Function<Throwable, X> when(final Class<X> xClass) {
        return cause -> Optional.ofNullable(cause)
                                .filter(xClass::isInstance)
                                .map(xClass::cast)
                                .orElse(null);
    }

    @Test
    public final void reThrowCauseIf() {
        for (final Exception exception : EXCEPTION_LIST) {
            try {
                try {
                    doThrow(() -> new WrappedException(exception));
                    fail(EXPECTED_AN_EXCEPTION);
                } catch (final WrappedException caught) {
                    Handling.of(caught)
                            .reThrowCauseIf(IOException.class)
                            .reThrowCauseIf(SQLException.class)
                            .reThrowCauseIf(IllegalArgumentException.class)
                            .reThrowCauseIf(IllegalStateException.class);
                    fail(EXPECTED_AN_EXCEPTION);
                }
            } catch (final IOException caught) {
                assertSame(EXCEPTION_LIST.get(0), caught);
            } catch (final SQLException caught) {
                assertSame(EXCEPTION_LIST.get(1), caught);
            } catch (final IllegalArgumentException caught) {
                assertSame(EXCEPTION_LIST.get(2), caught);
            } catch (final IllegalStateException caught) {
                assertSame(EXCEPTION_LIST.get(3), caught);
            }
        }
    }

    @Deprecated
    @Test
    public final void reThrowIf() {
        for (final Exception exception : EXCEPTION_LIST) {
            try {
                try {
                    doThrow(() -> exception);
                    fail(EXPECTED_AN_EXCEPTION);
                } catch (final Exception caught) {
                    Handling.of(caught)
                            .reThrowIf(IOException.class)
                            .reThrowIf(SQLException.class)
                            .reThrowIf(IllegalArgumentException.class)
                            .reThrowIf(IllegalStateException.class);
                    fail(EXPECTED_AN_EXCEPTION);
                }
            } catch (final IOException caught) {
                assertSame(EXCEPTION_LIST.get(0), caught);
            } catch (final SQLException caught) {
                assertSame(EXCEPTION_LIST.get(1), caught);
            } catch (final IllegalArgumentException caught) {
                assertSame(EXCEPTION_LIST.get(2), caught);
            } catch (final IllegalStateException caught) {
                assertSame(EXCEPTION_LIST.get(3), caught);
            }
        }
    }

    @Test
    public final void throwMapped() {
        for (final Exception exception : EXCEPTION_LIST) {
            try {
                try {
                    doThrow(() -> new WrappedException(exception));
                    fail(EXPECTED_AN_EXCEPTION);
                } catch (final WrappedException caught) {
                    Handling.of(caught)
                            .throwMapped(causeWhen(IOException.class))
                            .throwMapped(causeWhen(SQLException.class))
                            .throwMapped(causeWhen(IllegalArgumentException.class))
                            .throwMapped(causeWhen(IllegalStateException.class));
                    fail(EXPECTED_AN_EXCEPTION);
                }
            } catch (final IOException caught) {
                assertSame(EXCEPTION_LIST.get(0), caught);
            } catch (final SQLException caught) {
                assertSame(EXCEPTION_LIST.get(1), caught);
            } catch (final IllegalArgumentException caught) {
                assertSame(EXCEPTION_LIST.get(2), caught);
            } catch (final IllegalStateException caught) {
                assertSame(EXCEPTION_LIST.get(3), caught);
            }
        }
    }

    @Test
    public final void throwMappedCause() {
        for (final Exception exception : EXCEPTION_LIST) {
            try {
                try {
                    doThrow(() -> new WrappedException(exception));
                    fail(EXPECTED_AN_EXCEPTION);
                } catch (final WrappedException caught) {
                    Handling.of(caught)
                            .throwMappedCause(when(IOException.class))
                            .throwMappedCause(when(SQLException.class))
                            .throwMappedCause(when(IllegalArgumentException.class))
                            .throwMappedCause(when(IllegalStateException.class));
                    fail(EXPECTED_AN_EXCEPTION);
                }
            } catch (final IOException caught) {
                assertSame(EXCEPTION_LIST.get(0), caught);
            } catch (final SQLException caught) {
                assertSame(EXCEPTION_LIST.get(1), caught);
            } catch (final IllegalArgumentException caught) {
                assertSame(EXCEPTION_LIST.get(2), caught);
            } catch (final IllegalStateException caught) {
                assertSame(EXCEPTION_LIST.get(3), caught);
            }
        }
    }

    @Test
    public final void fallback() {
        final IOException original = new IOException();
        final IOException result = Handling.of(original)
                                           .fallback();
        assertSame(original, result);
    }

    @Test
    public final void mapped() {
        final IOException original = new IOException();
        final IOException result = Handling.of(original)
                                           .mapped(Function.identity());
        assertSame(original, result);
    }

    @Test
    public final void mappedCause() {
        final IOException original = new IOException();
        final Throwable result = Handling.of(new IllegalStateException(original))
                                         .mappedCause(Function.identity());
        assertSame(original, result);
    }
}
