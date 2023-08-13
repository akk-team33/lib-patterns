package de.team33.test.patterns.exceptional.dione;

import de.team33.patterns.exceptional.dione.Wrapping;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.UUID;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class WrappingTest {

    @Test
    final void normal_withImplicitMessage() {
        final IOException original =
                new IOException(UUID.randomUUID().toString());
        final Function<Exception, IllegalArgumentException> wrapping =
                Wrapping.method(IllegalArgumentException::new);
        final IllegalArgumentException result =
                wrapping.apply(original);
        assertSame(original, result.getCause(),
                   "The <result> is expected to wrap the <original>");
        assertEquals(original.getMessage(), result.getMessage(),
                     "The <result> is expected to contain the same message as the <original>");
    }

    @Test
    final void normal_withExplicitMessage() {
        final String message = UUID.randomUUID().toString();
        final IOException original =
                new IOException(UUID.randomUUID().toString());
        assertNotEquals("The <original> message is expected to be different from the predefined <message>",
                        message, original.getMessage());

        final Function<Exception, IllegalArgumentException> wrapping =
                Wrapping.method(message, IllegalArgumentException::new);
        final IllegalArgumentException result =
                wrapping.apply(original);
        assertSame(original, result.getCause(),
                   "The <result> is expected to wrap the <original>");
        assertEquals(message, result.getMessage(),
                     "The <result> is expected to contain the specified <message>");
    }

    @Test
    final void varying_withImplicitMessage() {
        final IOException original =
                new IOException(UUID.randomUUID().toString());
        final Function<Exception, IllegalArgumentException> wrapping =
                Wrapping.varying(IllegalArgumentException::new);
        final IllegalArgumentException result =
                wrapping.apply(original);
        assertSame(original, result.getCause(),
                   "The <result> is expected to wrap the <original>");
        assertEquals(original.getMessage(), result.getMessage(),
                     "The <result> is expected to contain the same message as the <original>");
    }

    @Test
    final void varying_withExplicitMessage() {
        final String message = UUID.randomUUID().toString();
        final IOException original =
                new IOException(UUID.randomUUID().toString());
        assertNotEquals("The <original> message is expected to be different from the predefined <message>",
                        message, original.getMessage());

        final Function<Exception, IllegalArgumentException> wrapping =
                Wrapping.varying(message, IllegalArgumentException::new);
        final IllegalArgumentException result =
                wrapping.apply(original);
        assertSame(original, result.getCause(),
                   "The <result> is expected to wrap the <original>");
        assertEquals(message, result.getMessage(),
                     "The <result> is expected to contain the specified <message>");
    }
}
