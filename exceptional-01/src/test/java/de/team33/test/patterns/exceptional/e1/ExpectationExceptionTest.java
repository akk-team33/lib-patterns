package de.team33.test.patterns.exceptional.e1;

import de.team33.patterns.exceptional.e1.ExpectationException;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

import static org.junit.Assert.*;

public class ExpectationExceptionTest {

    @Test
    public final void noParameters() {
        final ExpectationException case1 = new ExpectationException((String) null);
        final ExpectationException case2 = new ExpectationException((Throwable) null);
        final ExpectationException case3 = new ExpectationException(null, null);

        assertEquals("Unexpected: unknown condition", case1.getMessage());
        assertEquals(case1.getMessage(), case2.getMessage());
        assertEquals(case2.getMessage(), case3.getMessage());

        assertNull(case1.getCause());
        assertEquals(case1.getCause(), case2.getCause());
        assertEquals(case2.getCause(), case3.getCause());
    }

    @Test
    public final void noMessageAlsoInCause() {
        final Throwable cause = new IOException(null, null);
        final ExpectationException case1 = new ExpectationException(cause);
        final ExpectationException case2 = new ExpectationException(null, cause);

        assertEquals("Unexpected: " + cause.getClass().getCanonicalName(), case1.getMessage());
        assertEquals(case1.getMessage(), case2.getMessage());

        assertEquals(cause, case1.getCause());
        assertEquals(case1.getCause(), case2.getCause());
    }

    @Test
    public final void noMessageButInCause() {
        final String message = UUID.randomUUID().toString();
        final Throwable cause = new SQLException(message, (Throwable) null);
        final ExpectationException case3a = new ExpectationException(cause);
        final ExpectationException case3b = new ExpectationException(null, cause);

        assertEquals("Unexpected: " + message, case3a.getMessage());
        assertEquals(case3a.getMessage(), case3b.getMessage());

        assertEquals(cause, case3a.getCause());
        assertEquals(case3a.getCause(), case3b.getCause());
    }

    @Test
    public final void noCause() {
        final String message = UUID.randomUUID().toString();
        final ExpectationException case1 = new ExpectationException(message);
        final ExpectationException case2 = new ExpectationException(message, null);

        assertEquals(message, case1.getMessage());
        assertEquals(case1.getMessage(), case2.getMessage());

        assertNull(case1.getCause());
        assertEquals(case1.getCause(), case2.getCause());
    }

    @Test
    public final void allParameters() {
        final String message = UUID.randomUUID().toString();
        final Throwable cause1 = new IOException((String) null);
        final Throwable cause2 = new IOException(UUID.randomUUID().toString());
        final ExpectationException case1 = new ExpectationException(message, cause1);
        final ExpectationException case2 = new ExpectationException(message, cause2);

        assertEquals(message, case1.getMessage());
        assertEquals(case1.getMessage(), case2.getMessage());

        assertEquals(cause1, case1.getCause());
        assertEquals(cause2, case2.getCause());
    }
}
