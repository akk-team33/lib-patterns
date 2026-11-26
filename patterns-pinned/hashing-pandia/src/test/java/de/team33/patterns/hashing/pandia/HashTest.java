package de.team33.patterns.hashing.pandia;

import de.team33.patterns.hashing.pandia.testing.Supply;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HashTest extends Supply {

    private static final byte[] EMPTY = {};

    @Test
    final void newHash_null() {
        assertThrows(IllegalArgumentException.class, () -> new Hash(null));
    }

    @Test
    final void newHash_EMPTY() {
        assertThrows(IllegalArgumentException.class, () -> new Hash(EMPTY));
    }

    @Test
    final void bytes() {
        final byte[] origin = anyBits(256).toByteArray();
        assertArrayEquals(origin, new Hash(origin).bytes());
    }

    @ParameterizedTest
    @EnumSource
    final void toBigInteger(final HashCase testCase) {
        final BigInteger expected = testCase.bigInteger;
        final Hash hash = new Hash(testCase.bytes);
        assertEquals(expected, hash.toBigInteger());
    }

    @ParameterizedTest
    @EnumSource
    final void toHexString(final HashCase testCase) {
        final String expected = testCase.hexString;
        final Hash hash = new Hash(testCase.bytes);
        assertEquals(expected, hash.toHexString());
    }

    @ParameterizedTest
    @EnumSource
    final void toBase36String(final HashCase testCase) {
        final String expected = testCase.base36String;
        final Hash hash = new Hash(testCase.bytes);
        assertEquals(expected, hash.toString("0123456789abcdefghijklmnopqrstuvwxyz"));
    }
}
