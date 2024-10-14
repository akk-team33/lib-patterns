package de.team33.patterns.enums.pan.publics;

import de.team33.patterns.enums.pan.Values;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.nio.file.AccessMode;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

class ValuesTest {

    static final Values<AccessMode> ACCESS_MODE_VALUES = Values.of(AccessMode.class);

    @Test
    final void stream() {
        final AccessMode[] expected = AccessMode.values();
        final AccessMode[] result = ACCESS_MODE_VALUES.stream().toArray(AccessMode[]::new);
        assertArrayEquals(expected, result);
    }

    @ParameterizedTest
    @EnumSource
    final void findAny_fallback(final AccessMode expected) {
        final Predicate<AccessMode> filter = item -> item.name().equals(UUID.randomUUID().toString());
        final AccessMode result = ACCESS_MODE_VALUES.findAny(filter, AccessMode.READ);
        assertEquals(AccessMode.READ, result);
    }

    @ParameterizedTest
    @EnumSource
    final void findFirst_fallback(final AccessMode expected) {
        final Predicate<AccessMode> filter = item -> item.name().equals(UUID.randomUUID().toString());
        final AccessMode result = ACCESS_MODE_VALUES.findFirst(filter, AccessMode.EXECUTE);
        assertEquals(AccessMode.EXECUTE, result);
    }

    @ParameterizedTest
    @EnumSource
    final void mapAny(final AccessMode expected) {
        final Predicate<AccessMode> filter = item -> item.name().equals(expected.name());
        final Function<AccessMode, AccessMode> mapping = item -> item;

        final AccessMode result = ACCESS_MODE_VALUES.mapAny(filter, mapping)
                                                    .orElseThrow(AssertionError::new);

        assertSame(expected, result);
    }

    @ParameterizedTest
    @EnumSource
    final void mapFirst(final AccessMode expected) {
        final Predicate<AccessMode> filter = item -> item.name().equals(expected.name());
        final Function<AccessMode, AccessMode> mapping = item -> item;

        final AccessMode result = ACCESS_MODE_VALUES.mapFirst(filter, mapping)
                                                    .orElseThrow(AssertionError::new);

        assertSame(expected, result);
    }

    @Test
    final void mapAll() {
        final AccessMode[] expected = AccessMode.values();
        final AccessMode[] result = ACCESS_MODE_VALUES.mapAll(item -> item).toArray(AccessMode[]::new);
        assertArrayEquals(expected, result);
    }
}
