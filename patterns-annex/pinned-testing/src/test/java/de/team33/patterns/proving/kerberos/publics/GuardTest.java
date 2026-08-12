package de.team33.patterns.proving.kerberos.publics;

import de.team33.patterns.proving.kerberos.Guard;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.lang.System.Logger.Level.DEBUG;
import static org.junit.jupiter.api.Assertions.*;

class GuardTest {

    private static final System.Logger LOGGER =
            System.getLogger(GuardTest.class.getCanonicalName());

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
            Guard.prove(condition, () -> message, IOException::new);
            assertTrue(condition, () -> "<condition> is expected to be true - but was %s".formatted(condition));
        } catch (final IOException e) {
            LOGGER.log(DEBUG, e::getMessage, e);
            assertFalse(condition, () -> "<condition> is expected to be false - but was %s".formatted(condition));
            assertEquals(message, e.getMessage());
        }
    }

    @ParameterizedTest
    @ValueSource(longs = {-1000L, 0L, 1000L})
    final void proved_default(final long delta) {
        final Instant now = Instant.now();
        final Instant given = now.plusSeconds(delta);
        final Predicate<Instant> condition = now::isBefore;
        try {
            final Instant result = Guard.proved(given, condition);
            assertTrue(condition.test(given),
                       () -> "<instant> is expected to be after %s - but was %s".formatted(now, given));
            assertSame(given, result);
        } catch (final IllegalArgumentException e) {
            LOGGER.log(DEBUG, e::getMessage, e);
            assertFalse(condition.test(given),
                        () -> "<instant> is expected to be not after %s - but was %s".formatted(now, given));
            assertEquals("prove failed: <candidate> is <%s>".formatted(given), e.getMessage());
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {-5, -3, -1, 1, 3, 5})
    final void proved(final int given) {
        final Predicate<Integer> condition = input -> 0 < input;
        final Function<Integer, String> toMessage = "<input> is expected to be greater than 0 - but was %d"::formatted;
        try {
            final int result = Guard.proved(given, condition, toMessage);
            assertTrue(condition.test(given), () -> toMessage.apply(given));
            assertEquals(given, result);
        } catch (final IllegalArgumentException e) {
            LOGGER.log(DEBUG, e::getMessage, e);
            assertFalse(condition.test(given), () -> toMessage.apply(given)
                                                              .replace("greater than", "less than or equal to"));
            assertEquals(toMessage.apply(given), e.getMessage());
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "1", "12", "123", "1234", "12345", "123456"})
    @NullSource
    final void proved(final String given) {
        final int length = (null == given) ? -1 : given.length();
        try {
            final String result = Guard.proved(given, input -> null != input && input.length() > 3,
                                               "<input>.length is expected to be greater than 3 - but <input> was '%s'"::formatted,
                                               IOException::new);
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

    @ParameterizedTest
    @EmptySource
    @NullSource
    final void nonNull_default(final String given) {
        try {
            final String result = Guard.nonNull(given);
            assertNotNull(given, () -> "<given> is expected to be non-null - but was %s".formatted(given));
            assertSame(given, result);
        } catch (final NullPointerException e) {
            LOGGER.log(DEBUG, e::getMessage, e);
            assertNull(given);
            assertEquals("prove failed: <candidate> is <null>", e.getMessage());
        }
    }

    @ParameterizedTest
    @EmptySource
    @NullSource
    final void nonNull(final String given) {
        final Function<Object, String> toMessage = "<given> is expected to be non-null - but was %s"::formatted;
        try {
            final String result = Guard.nonNull(given, toMessage);
            assertNotNull(given, () -> toMessage.apply(result));
            assertSame(given, result);
        } catch (final NullPointerException e) {
            LOGGER.log(DEBUG, e::getMessage, e);
            assertNull(given);
            assertEquals(toMessage.apply(given), e.getMessage());
        }
    }
}