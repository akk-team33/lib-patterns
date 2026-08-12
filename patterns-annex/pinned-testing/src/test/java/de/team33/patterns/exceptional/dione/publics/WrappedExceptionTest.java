package de.team33.patterns.exceptional.dione.publics;

import de.team33.patterns.exceptional.dione.WrappedException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.UUID;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class WrappedExceptionTest {

    private static final String MISSING_EXCEPTION =
            "Missing: an exception to be wrapped in a " + WrappedException.class.getSimpleName();

    private static String anyMessage() {
        return UUID.randomUUID().toString();
    }

    @Test
    final void reThrowCauseIf() {
        final Throwable cause = new IOException((String) null);
        final WrappedException caught = new WrappedException(cause);

        try {
            caught.reThrowCauseIf(c -> c instanceof IOException, Function.identity());
            fail("expected to fail");
        } catch (final Throwable e) {
            assertInstanceOf(IOException.class, e);
        }
    }

    @Test
    final void reThrowCauseAs() {
        final Throwable cause = new IOException((String) null);
        final WrappedException caught = new WrappedException(cause);

        try {
            caught.reThrowCauseAs(IOException.class);
            fail("expected to fail");
        } catch (final Throwable e) {
            assertInstanceOf(IOException.class, e);
        }
    }

    @Test
    final void noMessageAlsoInCause() {
        final Throwable cause = new IOException((String) null);
        final WrappedException case1 = new WrappedException(cause);
        final WrappedException case2 = new WrappedException(null, cause);

        assertEquals("Wrapped: " + cause.getClass().getCanonicalName(), case1.getMessage());
        assertEquals(case1.getMessage(), case2.getMessage());

        assertEquals(cause, case1.getCause());
        assertEquals(case1.getCause(), case2.getCause());
    }

    @Test
    final void noMessageButInCause() {
        final String message = anyMessage();
        final Throwable cause = new IOException(message);
        final WrappedException case1 = new WrappedException(cause);
        final WrappedException case2 = new WrappedException(null, cause);

        assertEquals("Wrapped: " + message, case1.getMessage());
        assertEquals(case1.getMessage(), case2.getMessage());

        assertEquals(cause, case1.getCause());
        assertEquals(case1.getCause(), case2.getCause());
    }

    @Test
    final void noParameters() {
        final WrappedException case1 = new WrappedException(null);
        final WrappedException case2 = new WrappedException(null, null);

        assertEquals("Wrapped: nothing!?", case1.getMessage());
        assertEquals(case1.getMessage(), case2.getMessage());

        final Throwable cause1 = case1.getCause();
        final Throwable cause2 = case2.getCause();
        assertTrue(cause1 instanceof IllegalStateException);
        assertSame(cause1.getClass(), cause2.getClass());

        assertEquals(MISSING_EXCEPTION, cause1.getMessage());
        assertEquals(cause1.getMessage(), cause2.getMessage());
    }

    @Test
    final void noCause() {
        final String message = anyMessage();
        final WrappedException case2 = new WrappedException(message, null);

        assertEquals("Wrapped: nothing!? Message: " + message, case2.getMessage());
        assertTrue(case2.getCause() instanceof IllegalStateException);
        assertEquals(MISSING_EXCEPTION, case2.getCause().getMessage());
    }

    @Test
    final void allParameters() {
        final String message = anyMessage();
        final Throwable cause1 = new IOException((String) null);
        final Throwable cause2 = new IOException(anyMessage());
        final WrappedException case1 = new WrappedException(message, cause1);
        final WrappedException case2 = new WrappedException(message, cause2);

        assertNotEquals(message, cause1.getMessage());
        assertNotEquals(message, cause2.getMessage());
        assertNotEquals(cause1.getMessage(), cause2.getMessage());

        assertEquals(message, case1.getMessage());
        assertEquals(case1.getMessage(), case2.getMessage());

        assertEquals(cause1, case1.getCause());
        assertEquals(cause2, case2.getCause());
    }
}
