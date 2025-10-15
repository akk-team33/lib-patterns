package de.team33.patterns.proving.kerberos.publics;

import de.team33.patterns.proving.kerberos.Guard;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.util.UUID;

import static java.lang.System.Logger.Level.INFO;
import static org.junit.jupiter.api.Assertions.*;

class GuardTest {

    private static final System.Logger LOGGER =
            System.getLogger(GuardTest.class.getCanonicalName());

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    final void prove_defaultMessage(final boolean condition) {
        try {
            Guard.prove(condition);
            assertTrue(condition, () -> "<condition> is expected to be true - but was %s".formatted(condition));
        } catch (final IllegalStateException e) {
            LOGGER.log(INFO, e::getMessage, e);
            assertFalse(condition, () -> "<condition> is expected to be false - but was %s".formatted(condition));
            assertEquals("prove failed", e.getMessage());
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    final void prove(final boolean condition) {
        final String message = UUID.randomUUID().toString();
        try {
            Guard.prove(condition, () -> message);
            assertTrue(condition, () -> "<condition> is expected to be true - but was %s".formatted(condition));
        } catch (final IllegalStateException e) {
            LOGGER.log(INFO, e::getMessage, e);
            assertFalse(condition, () -> "<condition> is expected to be false - but was %s".formatted(condition));
            assertEquals(message, e.getMessage());
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    final void prove_IOException(final boolean condition) {
        final String message = UUID.randomUUID().toString();
        try {
            Guard.prove(condition, () -> message, IOException::new);
            assertTrue(condition, () -> "<condition> is expected to be true - but was %s".formatted(condition));
        } catch (final IOException e) {
            LOGGER.log(INFO, e::getMessage, e);
            assertFalse(condition, () -> "<condition> is expected to be false - but was %s".formatted(condition));
            assertEquals(message, e.getMessage());
        }
    }
}