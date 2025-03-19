package de.team33.patterns.lazy.narvi;

import de.team33.patterns.arbitrary.mimas.Generator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

final class LazyPoolTest implements Generator {

    private final Random random = new SecureRandom();
    private final Features features = new Features();
    private final int intValue = anyInt();
    private final String stringValue = anyString();
    private final Instant instantValue = Instant.now().plusNanos(anyInt());

    static Stream<Key<?>> keys() {
        return Key.VALUES.stream();
    }

    @Override
    public final BigInteger anyBits(final int numBits) {
        return new BigInteger(numBits, random);
    }

    @ParameterizedTest
    @MethodSource("keys")
    final <R> void get(final Key<R> key) {
        final R initial = features.get(key);
        assertSame(initial, features.get(key));
    }

    @ParameterizedTest
    @MethodSource("keys")
    final <R> void reset(final Key<R> key) {
        final R initial = features.get(key);
        features.reset(key);
        assertNotEquals(initial, features.get(key));
    }

    @Test
    final void reset() {
        final List<?> initial = Key.VALUES.stream().map(features::get).toList();
        features.reset();
        assertNotEquals(initial, Key.VALUES.stream().map(features::get).toList());
    }

    private List<Object> toList() {
        return features.get(Key.TO_LIST);
    }

    interface Key<R> extends LazyPool.Key<Features, R> {

        Key<Integer> INTEGER = Features::newInteger;
        Key<String> STRING = Features::newString;
        Key<Instant> INSTANT = Features::newInstant;
        Key<List<Object>> TO_LIST = Features::newToList;
        Key<Integer> HASH_CODE = Features::newHashCode;
        Key<String> TO_STRING = Features::newToString;

        List<Key<?>> VALUES = List.of(INTEGER, STRING, INSTANT, TO_STRING, HASH_CODE, TO_LIST);
    }

    @SuppressWarnings("ReturnOfInnerClass")
    class Features extends LazyPool<Features> {

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
            return Instant.now();
        }

        private List<Object> newToList() {
            return List.of(instantValue, stringValue, instantValue);
        }

        private int newHashCode() {
            return toList().hashCode();
        }

        private String newToString() {
            return toList().toString();
        }
    }
}