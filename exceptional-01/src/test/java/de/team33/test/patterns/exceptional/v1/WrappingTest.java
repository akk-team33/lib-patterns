package de.team33.test.patterns.exceptional.v1;

import de.team33.patterns.exceptional.v1.Wrapping;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;
import java.util.function.Function;

import static org.junit.Assert.*;

public class WrappingTest {

    @Test
    public final void normal_withImplicitMessage() {
        final IOException original =
                new IOException(UUID.randomUUID().toString());
        final Function<Exception, IllegalArgumentException> wrapping =
                Wrapping.method(IllegalArgumentException::new);
        final IllegalArgumentException result =
                wrapping.apply(original);
        assertSame("The <result> is expected to wrap the <original>",
                   original, result.getCause());
        assertEquals("The <result> is expected to contain the same message as the <original>",
                     original.getMessage(), result.getMessage());
    }

    @Test
    public final void normal_withExplicitMessage() {
        final String message = UUID.randomUUID().toString();
        final IOException original =
                new IOException(UUID.randomUUID().toString());
        assertNotEquals("The <original> message is expected to be different from the predefined <message>",
                        message, original.getMessage());

        final Function<Exception, IllegalArgumentException> wrapping =
                Wrapping.method(message, IllegalArgumentException::new);
        final IllegalArgumentException result =
                wrapping.apply(original);
        assertSame("The <result> is expected to wrap the <original>",
                   original, result.getCause());
        assertEquals("The <result> is expected to contain the specified <message>",
                     message, result.getMessage());
    }

    @Test
    public final void varying_withImplicitMessage() {
        final IOException original =
                new IOException(UUID.randomUUID().toString());
        final Function<Exception, IllegalArgumentException> wrapping =
                Wrapping.varying(IllegalArgumentException::new);
        final IllegalArgumentException result =
                wrapping.apply(original);
        assertSame("The <result> is expected to wrap the <original>",
                   original, result.getCause());
        assertEquals("The <result> is expected to contain the same message as the <original>",
                     original.getMessage(), result.getMessage());
    }

    @Test
    public final void varying_withExplicitMessage() {
        final String message = UUID.randomUUID().toString();
        final IOException original =
                new IOException(UUID.randomUUID().toString());
        assertNotEquals("The <original> message is expected to be different from the predefined <message>",
                        message, original.getMessage());

        final Function<Exception, IllegalArgumentException> wrapping =
                Wrapping.varying(message, IllegalArgumentException::new);
        final IllegalArgumentException result =
                wrapping.apply(original);
        assertSame("The <result> is expected to wrap the <original>",
                   original, result.getCause());
        assertEquals("The <result> is expected to contain the specified <message>",
                     message, result.getMessage());
    }
}
