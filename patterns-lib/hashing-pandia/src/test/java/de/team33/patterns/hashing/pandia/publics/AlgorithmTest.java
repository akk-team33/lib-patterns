package de.team33.patterns.hashing.pandia.publics;

import de.team33.patterns.hashing.pandia.Algorithm;
import de.team33.patterns.hashing.pandia.AlgorithmCase;
import de.team33.patterns.hashing.pandia.Hash;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class AlgorithmTest {

    @Test
    void ident() {
    }

    @ParameterizedTest
    @EnumSource
    final void hash_string(final AlgorithmCase given) {
        final Hash result = given.algorithm.hash(given.string);
        assertEquals(given.expected, result);
    }

    @ParameterizedTest
    @EnumSource
    final void hash_file(final AlgorithmCase given) {
        final Hash result = given.algorithm.hash(given.path);
        assertEquals(given.expected, result);
    }

    @Test
    final void hash_file_missing() {
        try {
            final Hash result = Algorithm.MD5.hash(AlgorithmCase.MD5_EMPTY.path.resolve("missing"));
            fail("expected to fail - but was " + result);
        } catch (final IllegalStateException e) {
            // as expected!
            // e.printStackTrace();
        }
    }

    @ParameterizedTest
    @EnumSource
    final void hash_bytes(final AlgorithmCase given) {
        final Hash result = given.algorithm.hash(given.bytes);
        assertEquals(given.expected, result);
    }
}