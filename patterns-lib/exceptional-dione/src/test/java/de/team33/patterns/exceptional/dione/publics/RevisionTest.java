package de.team33.patterns.exceptional.dione.publics;

import de.team33.patterns.exceptional.dione.Revision;
import de.team33.patterns.exceptional.dione.WrappedException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.fail;

@SuppressWarnings("NestedTryStatement")
class RevisionTest {

    private static final String EXPECTED_AN_EXCEPTION = "It is expected that an exception was thrown previously";
    private static final List<Exception> EXCEPTION_LIST = List.of(
            new IOException(), new SQLException(), new IllegalArgumentException(), new IllegalStateException()
    );

    private static <X extends Exception> void doThrow(final Supplier<X> supplier) throws X {
        throw supplier.get();
    }

    @Test
    final void of_null() throws Exception {
        final Throwable result = Revision.of(null)
                                         .reThrow(IOException.class)
                                         .throwIf(SQLException.class::isInstance,
                                                  SQLException.class::cast)
                                         .reThrow(IllegalArgumentException.class)
                                         .throwIf(IllegalStateException.class::isInstance,
                                                  IllegalStateException.class::cast)
                                         .close(Function.identity());
        assertNull(result);
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
    final void close() {
        final IOException original = new IOException("test");
        final IOException result = Revision.of(original)
                                           .close();
        assertSame(original, result);
    }

    @Test
    final void closeMapped() {
        final IOException original = new IOException("test");
        final IOException result = Revision.of(original)
                                           .close(Function.identity());
        assertSame(original, result);
    }
}
