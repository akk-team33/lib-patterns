package de.team33.patterns.serial.charon;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class ChargedTest {

    private List<Integer> values = Arrays.asList(0, 1, 2, 3, 4);

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7, 8})
    final void seriesOf(final int index) {
        final Series<?> subject = Charged.seriesOf(values, index);
        assertEquals(index < values.size(), subject.isCharged());
        assertEquals(index >= values.size(), subject.isEmpty());
        if (subject instanceof Charged) {
            assertEquals(index, subject.head());
            assertEquals(values.subList(index + 1, values.size()), subject.tail().asList());
            assertEquals(values.size() - index, subject.size());
            assertEquals(values.subList(index, values.size()), subject.asList());
        } else {
            assertInstanceOf(Empty.class, subject);
        }
    }
}