package de.team33.patterns.test.testing.titan.io;

import de.team33.patterns.testing.titan.io.Redirected;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Deprecated
@SuppressWarnings("UseOfSystemOutOrSystemErr")
class RedirectedTest {

    @Test
    final void outputOf() throws IOException {
        final String original = UUID.randomUUID().toString();
        final String result = Redirected.outputOf(() -> System.out.println(original));
        final String expected = String.format("%s%n", original);
        assertEquals(expected, result);
    }
}