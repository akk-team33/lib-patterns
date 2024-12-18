package de.team33.patterns.hashing.pandia;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HashTest {

    private static final byte[] EMPTY = {};

    @Test
    void newHash_EMPTY() {
        assertThrows(NumberFormatException.class, () -> new Hash(EMPTY));
    }

    @ParameterizedTest
    @EnumSource
    void toBigInteger(final HashCase testCase) {
        final BigInteger expected = testCase.bigInteger;
        final Hash hash = new Hash(testCase.bytes);
        assertEquals(expected, hash.toBigInteger());
    }
}
