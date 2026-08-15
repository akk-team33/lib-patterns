package de.team33.patterns.records.triton;

import de.team33.testing.Supply;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigInteger;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class StringableTest extends TritonTestBase {

    private static final Supply SUPPLY = new Supply();

    static Stream<Case<?>> cases() {
        return Stream.of(newCase(Instant.class, Instant.now()),
                // newCase(SimpleDateFormat.class, new SimpleDateFormat("yyyy-MM-dd")),
                         newCase(Class.class, Thread.class, Class::getName),
                         newCase(Class.class, ByStringByFactory.class, Class::getName),
                         newCase(UUID.class, UUID.randomUUID()),
                // newCase(StringBuilder.class, new StringBuilder(SUPPLY.anyString())),
                         newCase(Integer.class, SUPPLY.anyInt()),
                         newCase(BigInteger.class, SUPPLY.anyBigInteger()),
                         newCase(ByStringByConstructor.class, new ByStringByConstructor(SUPPLY.anyString())),
                         newCase(ByStringByFactory.class, new ByStringByFactory(SUPPLY.anyString())));
    }

    private static <T> Case<T> newCase(final Class<T> type, final T encoded) {
        return newCase(type, encoded, Object::toString);
    }

    @SuppressWarnings("BoundedWildcard")
    private static <T> Case<T> newCase(final Class<T> type, final T encoded, final Function<T, String> toString) {
        return new Case<>(type, encoded, toString.apply(encoded));
    }

    @ParameterizedTest
    @MethodSource("cases")
    final <T> void supports(final Case<T> given) {
        assertTrue(Stringable.supports(given.type));
    }

    @ParameterizedTest
    @ValueSource(classes = {Object.class, List.class, CharSequence.class, HashMap.class})
    final <T> void supports_not(final Class<T> type) {
        assertFalse(Stringable.supports(type));
    }

    @ParameterizedTest
    @MethodSource("cases")
    final <T> void encode(final Case<T> given) {
        final String result = Stringable.encode(given.decoded);
        assertEquals(given.encoded, result);
    }

    @ParameterizedTest
    @MethodSource("cases")
    final <T> void decode(final Case<T> given) {
        final T result = Stringable.decode(given.type, given.encoded);
        assertEquals(given.decoded, result);
    }

    record Case<T>(Class<T> type, T decoded, String encoded) {
    }

    @SuppressWarnings({"WeakerAccess", "unused"})
    public static final class ByStringByConstructor {

        private final String value;

        public ByStringByConstructor(final String value) {
            this.value = value;
        }

        public ByStringByConstructor(final CharSequence value) {
            throw new UnsupportedOperationException("Not supported.");
        }

        public static ByStringByConstructor dOf(final CharSequence value) {
            throw new UnsupportedOperationException("Not supported.");
        }

        public static ByStringByConstructor cOf(final String value) {
            throw new UnsupportedOperationException("Not supported.");
        }

        public static ByStringByConstructor bOf(final CharSequence value) {
            throw new UnsupportedOperationException("Not supported.");
        }

        public static ByStringByConstructor aOf(final String value) {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public final boolean equals(final Object obj) {
            return (this == obj) || ((obj instanceof final ByStringByConstructor other) && value.equals(other.value));
        }

        @Override
        public final int hashCode() {
            return value.hashCode();
        }

        @Override
        public final String toString() {
            return value;
        }
    }

    @SuppressWarnings("unused")
    public static final class ByStringByFactory {

        private final String value;

        private ByStringByFactory(final String value) {
            this.value = value;
        }

        public static ByStringByFactory dOf(final CharSequence value) {
            throw new UnsupportedOperationException("Not supported.");
        }

        public static ByStringByFactory cOf(final String value) {
            return new ByStringByFactory(value);
        }

        public static ByStringByFactory bOf(final CharSequence value) {
            throw new UnsupportedOperationException("Not supported.");
        }

        public static ByStringByFactory aOf(final String value) {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public final boolean equals(final Object obj) {
            return (this == obj) || ((obj instanceof final ByStringByFactory other) && value.equals(other.value));
        }

        @Override
        public final int hashCode() {
            return value.hashCode();
        }

        @Override
        public final String toString() {
            return value;
        }
    }
}