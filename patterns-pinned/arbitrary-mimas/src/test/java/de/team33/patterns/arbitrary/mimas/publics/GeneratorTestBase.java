package de.team33.patterns.arbitrary.mimas.publics;

import de.team33.patterns.arbitrary.mimas.Generator;
import de.team33.patterns.typing.proteus.Type;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.*;

import static de.team33.patterns.arbitrary.mimas.Generator.CHARACTERS;
import static de.team33.patterns.arbitrary.mimas.Generator.MAX_STRING_LENGTH;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("ClassWithTooManyMethods")
abstract class GeneratorTestBase<G extends Generator> {

    private final G generator;

    GeneratorTestBase(final G generator) {
        this.generator = generator;
    }

    final G generator() {
        return generator;
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 5, 8, 13, 30})
    final void anyBits(final int numBits) {
        final BigInteger bound = BigInteger.ONE.shiftLeft(numBits);
        final BigInteger result = generator.anyBits(numBits);
        assertFalse(BigInteger.ZERO.compareTo(result) > 0);
        assertTrue(bound.compareTo(result) > 0);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 5, 8, 13, 30, 500, 8000, 130_000, 8_000_000})
    final void anyInt_bound(final int bound) {
        final int result = generator.anyInt(bound);
        assertFalse(0 > result);
        assertTrue(bound > result);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 5, 8, 13, 30, 500, 8000, 130_000, 8_000_000})
    final void anyInt_min_bound(final int bound) {
        final int min = -bound;
        final int result = generator.anyInt(min, bound);
        assertFalse(min > result);
        assertTrue(bound > result);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 5, 8, 13, 30, 500, 8000, 130_000, 8_000_000})
    final void anySmallInt(final int bound) {
        final int result = generator.anySmallInt(bound);
        assertFalse(0 > result);
        assertTrue(bound > result);
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3, 5, 8, 13, 30, 500, 8000, 130_000, 8_000_000})
    final void anyLong_bound(final long bound) {
        final long result = generator.anyLong(bound);
        assertFalse(0 > result);
        assertTrue(bound > result);
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3, 5, 8, 13, 30, 500, 8000, 130_000, 8_000_000})
    final void anyLong_min_bound(final long bound) {
        final long min = -bound;
        final long result = generator.anyLong(min, bound);
        assertFalse(min > result);
        assertTrue(bound > result);
    }

    @Test
    final void anyBigInteger() {
        final BigInteger min = BigInteger.valueOf(Long.MIN_VALUE);
        final BigInteger max = BigInteger.valueOf(Long.MAX_VALUE);
        final BigInteger result = generator.anyBigInteger();
        assertFalse(min.compareTo(result) > 0);
        assertFalse(max.compareTo(result) < 0);
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3, 5, 8, 13, 30, 500, 8000, 130_000, 8_000_000})
    final void anyBigInteger_bound(final long bound) {
        final BigInteger bigBound = BigInteger.valueOf(bound);
        final BigInteger result = generator.anyBigInteger(bigBound);
        assertFalse(BigInteger.ZERO.compareTo(result) > 0);
        assertTrue(bigBound.compareTo(result) > 0);
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3, 5, 8, 13, 30, 500, 8000, 130_000, 8_000_000})
    final void anyBigInteger_min_bound(final long bound) {
        final BigInteger bigBound = BigInteger.valueOf(bound);
        final BigInteger bigMin = BigInteger.ZERO.subtract(bigBound);
        final BigInteger result = generator.anyBigInteger(bigMin, bigBound);
        assertFalse(bigMin.compareTo(result) > 0);
        assertTrue(bigBound.compareTo(result) > 0);
    }

    @Test
    final void anySmallBigInteger() {
        final BigInteger bigBound = BigInteger.valueOf(0x10000);
        final BigInteger result = generator.anySmallBigInteger();
        assertFalse(BigInteger.ZERO.compareTo(result) > 0);
        assertTrue(bigBound.compareTo(result) > 0);
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 20, 300, 5000, 80_000, 1_300_000, 30_000_000})
    final void anySmallBigInteger_bound(final long bound) {
        final BigInteger bigBound = BigInteger.valueOf(bound);
        final BigInteger result = generator.anySmallBigInteger(bigBound);
        assertFalse(BigInteger.ZERO.compareTo(result) > 0);
        assertTrue(bigBound.compareTo(result) > 0);
    }

    @Test
    final void anyChar() {
        final char result = generator.anyChar();
        assertFalse(0 > CHARACTERS.indexOf(result));
    }

    @Test
    final void anyChar_of() {
        final String characters = "abc-123";
        final char result = generator.anyChar(characters);
        assertFalse(0 > characters.indexOf(result));
    }

    @Test
    final void anyString() {
        final String result = generator.anyString();
        assertFalse(MAX_STRING_LENGTH < result.length());
        for (int index = 0; index < result.length(); ++index) {
            assertFalse(0 > CHARACTERS.indexOf(result.charAt(index)));
        }
    }

    @Test
    final void anyString_length() {
        final int length = 13;
        final String result = generator.anyString(length);
        assertEquals(length, result.length());
        for (int index = 0; index < result.length(); ++index) {
            assertFalse(0 > CHARACTERS.indexOf(result.charAt(index)));
        }
    }

    @Test
    final void anyString_range() {
        final int min = 5;
        final int max = 13;
        final String result = generator.anyString(min, max);
        assertFalse(min > result.length());
        assertFalse(max < result.length());
        for (int index = 0; index < result.length(); ++index) {
            assertFalse(0 > CHARACTERS.indexOf(result.charAt(index)));
        }
    }

    @Test
    final void anyString_of() {
        final String characters = "abc-123";
        final String result = generator.anyString(characters);
        assertFalse(MAX_STRING_LENGTH < result.length());
        for (int index = 0; index < result.length(); ++index) {
            assertFalse(0 > characters.indexOf(result.charAt(index)));
        }
    }

    @Test
    final void anyString_length_of() {
        final String characters = "abc-123";
        final int length = 17;
        final String result = generator.anyString(length, characters);
        assertEquals(length, result.length());
        for (int index = 0; index < result.length(); ++index) {
            assertFalse(0 > characters.indexOf(result.charAt(index)));
        }
    }

    @Test
    final void anyString_range_of() {
        final String characters = "abc-123";
        final int min = 3;
        final int max = 7;
        final String result = generator.anyString(min, max, characters);
        assertFalse(min > result.length());
        assertFalse(max < result.length());
        for (int index = 0; index < result.length(); ++index) {
            assertFalse(0 > characters.indexOf(result.charAt(index)));
        }
    }

    @Test
    final void anyOf() {
        final String[] values = {"a", "b", "c"};
        final String result = generator.anyOf(values);
        assertTrue(List.of(values).contains(result));
    }

    @Test
    final void anyOf_enum() {
        final RoundingMode result = generator.anyOf(RoundingMode.class);
        assertTrue(EnumSet.allOf(RoundingMode.class).contains(result));
    }

    @Test
    final void anyDataset() {
        final Map<String, Type<?>> description = new HashMap<>() {{
            put("index", Type.of(int.class));
            put("longIndex", Type.of(Long.class));
            put("name", Type.of(String.class));
            put("title", Type.of(String.class));
            put("subTitle", Type.of(CharSequence.class));
            put("missing", Type.of(Instant.class));
        }};

        final Map<String, Object> result = generator.anyDataset(description);
        assertEquals(description.keySet(), result.keySet());
    }

    @Test
    final void nullable_1() {
        final String result = generator.nullable(1, Generator::anyString);
        assertNull(result);
    }

    @Test
    final void optional_1() {
        final Optional<String> result = generator.optional(1, Generator::anyString);
        assertEquals(Optional.empty(), result);
    }

    @Test
    final void stream() {
        final List<String> result = generator.stream(Generator::anyString).limit(3).toList();
        assertEquals(3, result.size());
    }
}