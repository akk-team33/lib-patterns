package de.team33.test.patterns.exceptional.v1;

import de.team33.patterns.exceptional.v1.ExpectationException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class ExpectationExceptionTest {

    @Test
    public final void initByCause() {
        assertEquals("Unexpected: null",
                     new ExpectationException((Throwable) null).getMessage());
        assertEquals("Unexpected: " + IllegalArgumentException.class.getCanonicalName(),
                     new ExpectationException(new IllegalArgumentException()).getMessage());
        assertEquals("Unexpected: the message",
                     new ExpectationException(new IllegalArgumentException("the message")).getMessage());
    }

    @Test
    public final void initByMessage() {
        assertNull(new ExpectationException((String) null).getMessage());
        assertEquals("the message",
                     new ExpectationException("the message").getMessage());
    }

    @Test
    public final void initByBoth() {
        final ExpectationException case1 = new ExpectationException(null, null);
        assertNull(case1.getMessage());
        assertNull(case1.getCause());

        final ExpectationException case2 = new ExpectationException("the message", null);
        assertEquals("the message", case2.getMessage());
        assertNull(case2.getCause());

        final ExpectationException case3 = new ExpectationException(null, new IOException());
        assertNull(case3.getMessage());
        assertNotNull(case3.getCause());

        final ExpectationException case4 = new ExpectationException("the message", new IOException());
        assertEquals("the message", case4.getMessage());
        assertNotNull(case4.getCause());
    }
}
