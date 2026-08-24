package de.team33.patterns.lazy.narvi;

import de.team33.patterns.arbitrary.mimas.Generator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings({"MethodOnlyUsedFromInnerClass", "TypeMayBeWeakened"})
final class LazyFeaturesTest implements Generator {

    private final Random random = new SecureRandom();
    private final Features features = new Features();

    static Stream<Key<?>> keys() {
        return Key.VALUES.stream();
    }

    @Override
    public final BigInteger anyBits(final int numBits) {
        return new BigInteger(numBits, random);
    }

    @ParameterizedTest
    @MethodSource("keys")
    final <R> void peek(final Key<R> key) {
        assertEquals(Optional.empty(), features.peek(key));

        final R expected = features.get(key);
        assertEquals(expected, features.peek(key)
                                       .orElseThrow(() -> new AssertionError(
                                               "expected to be present - but was absent")));
    }

    @ParameterizedTest
    @MethodSource("keys")
    final <R> void get_reset_one(final Key<R> key) {
        final R initial = features.get(key);
        assertSame(initial, features.get(key));

        features.reset(key);
        assertNotSame(initial, features.get(key));
    }

    @Test
    final void get_reset_all() {
        final List<?> initial = Key.VALUES.stream().map(features::get).toList();
        assertEquals(initial, Key.VALUES.stream().map(features::get).toList());

        features.reset();
        assertNotEquals(initial, Key.VALUES.stream().map(features::get).toList());
    }

    private Integer integer() {
        return features.get(Key.INTEGER);
    }

    private String string() {
        return features.get(Key.STRING);
    }

    private Instant instant() {
        return features.get(Key.INSTANT);
    }

    private List<Object> toList() {
        return features.get(Key.TO_LIST);
    }

    @SuppressWarnings("ClassNameSameAsAncestorName")

    @FunctionalInterface
    interface Key<R> extends LazyFeatures.Key<Features, R> {

        Key<Integer> INTEGER = Features::newInteger;
        Key<String> STRING = Features::newString;
        Key<Instant> INSTANT = Features::newInstant;
        Key<List<Object>> TO_LIST = Features::newToList;
        Key<Integer> HASH_CODE = Features::newHashCode;
        Key<String> TO_STRING = Features::newToString;
        // Key<Object> NULL = host -> null;

        @SuppressWarnings("StaticMethodOnlyUsedInOneClass")
        List<Key<?>> VALUES = List.of(INTEGER, STRING, INSTANT, TO_STRING, HASH_CODE, TO_LIST/*, NULL*/);
    }

    @SuppressWarnings("ReturnOfInnerClass")
    class Features extends LazyFeatures<Features> {

        @Override
        protected final Features host() {
            return this;
        }

        private Integer newInteger() {
            return anyInt();
        }

        private String newString() {
            return anyString();
        }

        private Instant newInstant() {
            return Instant.now().plusNanos(anyInt());
        }

        private List<Object> newToList() {
            return List.of(integer(), string(), instant());
        }

        private int newHashCode() {
            return toList().hashCode();
        }

        private String newToString() {
            return toList().toString();
        }
    }
}