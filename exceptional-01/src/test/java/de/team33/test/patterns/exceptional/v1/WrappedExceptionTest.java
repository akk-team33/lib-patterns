package de.team33.test.patterns.exceptional.v1;

import de.team33.patterns.exceptional.v1.WrappedException;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class WrappedExceptionTest {

    private static String anyMessage() {
        return UUID.randomUUID().toString();
    }

    @Test(expected = NullPointerException.class)
    public final void initByNull() {
        fail("expected to fail but was " + new WrappedException(null));
    }

    @Test
    public final void initByNullAny() {
        final String message = anyMessage();
        final IOException cause = new IOException(message);
        final WrappedException sample = new WrappedException(null, cause);
        assertSame("The <sample> is expected to wrap the exactly given <cause> as cause",
                   cause, sample.getCause());
        assertTrue("The <sample> is expected to contain the original <message> within its message",
                   sample.getMessage().contains(message));
    }

    @Test(expected = NullPointerException.class)
    public final void initByAnyNull() {
        fail("expected to fail but was " + new WrappedException(anyMessage(), null));
    }

    @Test(expected = NullPointerException.class)
    public final void initByNullNull() {
        fail("expected to fail but was " + new WrappedException(null, null));
    }

    @Test
    public final void initByCause() {
        final String message = anyMessage();
        final IOException cause = new IOException(message);
        final WrappedException sample = new WrappedException(cause);
        assertSame("The <sample> is expected to wrap the exactly given <cause> as cause",
                   cause, sample.getCause());
        assertTrue("The <sample> is expected to contain the original <message> within its message",
                   sample.getMessage().contains(message));
    }

    @Test
    public final void initByCauseWithoutMessage() {
        final IOException cause = new IOException();
        final WrappedException sample = new WrappedException(cause);
        assertSame("The <sample> is expected to wrap the exactly given <cause> as cause",
                   cause, sample.getCause());
        assertTrue("The <sample> is expected to contain the original <message> within its message",
                   sample.getMessage().contains(cause.getClass().getCanonicalName()));
    }

    @Test
    public final void initByBoth() {
        final String message = anyMessage();
        final IOException cause = new IOException(anyMessage());
        final WrappedException sample = new WrappedException(message, cause);
        assertSame("The <sample> is expected to wrap the exactly given <cause> as cause",
                   cause, sample.getCause());
        assertEquals("The <sample> is expected to contain the exactly given <message> as message",
                     message, sample.getMessage());
    }
}
