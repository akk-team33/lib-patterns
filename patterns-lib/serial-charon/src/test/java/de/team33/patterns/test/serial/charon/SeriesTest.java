package de.team33.patterns.test.serial.charon;

import de.team33.patterns.serial.charon.Series;
import de.team33.patterns.shared.serial.charon.Supply;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class SeriesTest {

    private static final Supply SUPPLY = new Supply();

    @Test
    final void empty() {
        final Series<?> empty = Series.empty();
        assertTrue(empty.isEmpty());
        assertFalse(empty.isCharged());
        assertThrows(NoSuchElementException.class, empty::head);
        assertSame(empty, empty.tail());
        assertSame(empty, Series.empty());
    }

    @Test
    final void of_array() {
        String string0 = SUPPLY.nextString();
        String string1 = SUPPLY.nextString();
        String string2 = SUPPLY.nextString();
        final Series<?> sample = Series.of(string0, string1, string2);
        assertFalse(sample.isEmpty());
        assertTrue(sample.isCharged());
        assertEquals(string0, sample.head());
        assertEquals(sample.tail(), Series.of(string1, string2));
        assertEquals(Series.empty(), sample.tail().tail().tail());
    }

    @Test
    final void of_streamable() {
        String string0 = SUPPLY.nextString();
        String string1 = SUPPLY.nextString();
        String string2 = SUPPLY.nextString();
        final Series<?> sample = Series.of(() -> Stream.of(string0, string1, string2));
        assertFalse(sample.isEmpty());
        assertTrue(sample.isCharged());
        assertEquals(string0, sample.head());
        assertEquals(sample.tail(), Series.of(string1, string2));
        assertEquals(Series.empty(), sample.tail().tail().tail());
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    final void isEmpty(final boolean empty) {
        final List<String> origin = empty ? Collections.emptyList() : SUPPLY.nextChargedList();
        final Series<String> result = Series.of(origin);
        assertEquals(empty, result.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    final void isCharged(final boolean charged) {
        final List<String> origin = charged ? SUPPLY.nextChargedList() : Collections.emptyList();
        final Series<String> result = Series.of(origin);
        assertEquals(charged, result.isCharged());
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    final void ifCharged(final boolean charged) {
        final List<String> origin = charged ? SUPPLY.nextChargedList() : Collections.emptyList();
        final String fallback = SUPPLY.nextString();
        final Series<String> sample = Series.of(origin);
        final String expected = charged ? origin.get(0) : fallback;
        final String result = sample.ifCharged((head, tail) -> head).orElse(fallback);
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    final void head(final boolean charged) {
        final List<String> origin = charged ? SUPPLY.nextList(1) : Collections.emptyList();
        final Series<String> sample = Series.of(origin);
        try {
            final String result = sample.head();
            assertTrue(charged);
            assertEquals(origin.get(0), result);
        } catch (final NoSuchElementException e) {
            assertFalse(charged);
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3})
    final void tail(final int size) {
        final List<String> origin = SUPPLY.nextList(size);
        final Series<String> sample = Series.of(origin);
        final Series<String> tail = sample.tail();
        assertEquals(size < 2, tail.isEmpty());
    }

    @Test
    final void size() {
        final List<String> origin = SUPPLY.nextList(0, 10);
        final Series<String> sample = Series.of(origin);
        assertEquals(origin.size(), sample.size());
    }

    @Test
    final void asList() {
        final List<String> origin = SUPPLY.nextList(0, 10);
        final Series<String> sample = Series.of(origin);
        assertEquals(origin, sample.asList());
    }

    @Test
    final void testEquals() {
        final List<String> origin = SUPPLY.nextList(1, 5);
        final Series<String> left = Series.of(origin);
        final Series<String> right = Series.of(origin);
        assertNotSame(left, right,
                      () -> "The following test result would not be significant if left and right were identical.");
        assertEquals(left, right,
                     () -> "Two series with the same origin are expected to be equal.");
        assertNotEquals(left, Series.of(SUPPLY.nextList(6, 10)),
                        () -> "Two series with the different origins are expected to be different.");
    }

    @Test
    final void testHashCode() {
        final List<String> origin = SUPPLY.nextList(1, 10);
        final Series<String> left = Series.of(origin);
        final Series<String> right = Series.of(origin);
        assertNotSame(left, right,
                      () -> "The following test result would not be significant if left and right were identical.");
        assertEquals(left.hashCode(), right.hashCode(),
                     () -> "The hash code of two equal series are also expected to be equal.");
    }

    @Test
    final void testToString() {
        final List<String> origin = SUPPLY.nextList(1, 10);
        final Series<String> left = Series.of(origin);
        final Series<String> right = Series.of(origin);
        assertNotSame(left, right,
                      () -> "The following test results would not be significant if left and right were identical.");
        assertEquals(left.toString(), right.toString(),
                     () -> "The string representations of two equal series are also expected to be equal.");
        assertEquals(origin.toString(), right.toString(),
                     () -> "The string representation of a Series is expected to be equal to " +
                           "the string representation of the original List.");
    }
}
