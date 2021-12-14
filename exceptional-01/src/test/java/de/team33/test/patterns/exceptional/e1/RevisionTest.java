package de.team33.test.patterns.exceptional.e1;

import de.team33.patterns.exceptional.e1.Revision;
import de.team33.patterns.exceptional.e1.WrappedException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.fail;

@SuppressWarnings("NestedTryStatement")
class RevisionTest {

    private static final String EXPECTED_AN_EXCEPTION = "It is expected that an exception was thrown previously";
    private static final List<Exception> EXCEPTION_LIST = Arrays.asList(
            new IOException(), new SQLException(), new IllegalArgumentException(), new IllegalStateException()
    );

    private static <X extends Exception> void doThrow(final Supplier<X> supplier) throws X {
        throw supplier.get();
    }

    @Test
    final void reThrowCause() {
        for (final Exception exception : EXCEPTION_LIST) {
            try {
                try {
                    doThrow(() -> new WrappedException(exception));
                    fail(EXPECTED_AN_EXCEPTION);
                } catch (final WrappedException caught) {
                    Revision.of(caught.getCause())
                            .reThrow(IOException.class)
                            .reThrow(SQLException.class)
                            .reThrow(IllegalArgumentException.class)
                            .reThrow(IllegalStateException.class);
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
    final void reThrow() {
        for (final Exception exception : EXCEPTION_LIST) {
            try {
                try {
                    doThrow(() -> exception);
                    fail(EXPECTED_AN_EXCEPTION);
                } catch (final Exception caught) {
                    Revision.of(caught)
                            .reThrow(IOException.class)
                            .reThrow(SQLException.class)
                            .reThrow(IllegalArgumentException.class)
                            .reThrow(IllegalStateException.class);
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
    final void throwIf() {
        for (final Exception exception : EXCEPTION_LIST) {
            try {
                try {
                    doThrow(() -> new WrappedException(exception));
                    fail(EXPECTED_AN_EXCEPTION);
                } catch (final WrappedException caught) {
                    Revision.of(caught.getCause())
                            .throwIf(IOException.class::isInstance, IOException.class::cast)
                            .throwIf(SQLException.class::isInstance, SQLException.class::cast)
                            .throwIf(IllegalArgumentException.class::isInstance, IllegalArgumentException.class::cast)
                            .throwIf(IllegalStateException.class::isInstance, IllegalStateException.class::cast);
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
    final void finish() {
        final IOException original = new IOException("test");
        final IOException result = Revision.of(original)
                                           .finish();
        assertSame(original, result);
    }

    @Test
    final void finishMapped() {
        final IOException original = new IOException("test");
        final IOException result = Revision.of(original)
                                           .finish(Function.identity());
        assertSame(original, result);
    }
}
