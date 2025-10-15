package de.team33.patterns.proving.kerberos.publics;

import de.team33.patterns.proving.kerberos.Guard;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.util.UUID;

import static java.lang.System.Logger.Level.DEBUG;
import static org.junit.jupiter.api.Assertions.*;

class GuardTest {

    private static final System.Logger LOGGER =
            System.getLogger(GuardTest.class.getCanonicalName());
    private static final Guard<Integer, IllegalArgumentException> INT_GUARD =
            Guard.proving(input -> 0 < input,
                          "<input> is expected to be greater than 0 - but was %d"::formatted);
    private static final Guard<String, IOException> STR_GUARD =
            Guard.<String>proving(input -> null != input && input.length() > 3,
                                  "<input>.length is expected to be greater than 3 - but <input> was '%s'"::formatted)
                 .thatThrows(IOException::new);

    @SuppressWarnings("ConstantValue")
    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    final void prove_defaultMessage(final boolean condition) {
        try {
            Guard.prove(condition);
            assertTrue(condition, () -> "<condition> is expected to be true - but was %s".formatted(condition));
        } catch (final IllegalStateException e) {
            LOGGER.log(DEBUG, e::getMessage, e);
            assertFalse(condition, () -> "<condition> is expected to be false - but was %s".formatted(condition));
            assertEquals("prove failed", e.getMessage());
        }
    }

    @SuppressWarnings("ConstantValue")
    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    final void prove_message(final boolean condition) {
        final String message = UUID.randomUUID().toString();
        try {
            Guard.prove(condition, () -> message);
            assertTrue(condition, () -> "<condition> is expected to be true - but was %s".formatted(condition));
        } catch (final IllegalStateException e) {
            LOGGER.log(DEBUG, e::getMessage, e);
            assertFalse(condition, () -> "<condition> is expected to be false - but was %s".formatted(condition));
            assertEquals(message, e.getMessage());
        }
    }

    @SuppressWarnings("ConstantValue")
    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    final void prove_IOException(final boolean condition) {
        final String message = UUID.randomUUID().toString();
        try {
            Guard.prove(condition, IOException::new, () -> message);
            assertTrue(condition, () -> "<condition> is expected to be true - but was %s".formatted(condition));
        } catch (final IOException e) {
            LOGGER.log(DEBUG, e::getMessage, e);
            assertFalse(condition, () -> "<condition> is expected to be false - but was %s".formatted(condition));
            assertEquals(message, e.getMessage());
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {-5, -3, -1, 1, 3, 5})
    final void proved(final int given) {
        try {
            final int result = INT_GUARD.proved(given);
            assertEquals(given, result);
            assertTrue(0 < given, () -> ("<given> is expected to be greater than 0 " +
                                         "- but was %s").formatted(given));
        } catch (final IllegalArgumentException e) {
            LOGGER.log(DEBUG, e::getMessage, e);
            assertFalse(0 < given, () -> ("<given> is expected to be less than or equal to 0 " +
                                          "- but was %s").formatted(given));
            assertEquals("<input> is expected to be greater than 0 - but was %d".formatted(given), e.getMessage());
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "1", "12", "123", "1234", "12345", "123456"})
    @NullSource
    final void proved(final String given) {
        final int length = (null == given) ? -1 : given.length();
        try {
            final String result = STR_GUARD.proved(given);
            assertSame(given, result);
            assertTrue(3 < length, () -> ("<length>is expected to be greater than 3 " +
                                          "- but was %d").formatted(length));
        } catch (final IOException e) {
            LOGGER.log(DEBUG, e::getMessage, e);
            assertFalse(3 < length, () -> ("<length>is expected to be less than 4 " +
                                           "- but was %d").formatted(length));
            assertEquals("<input>.length is expected to be greater than 3 - but <input> was '%s'".formatted(given),
                         e.getMessage());
        }
    }
}