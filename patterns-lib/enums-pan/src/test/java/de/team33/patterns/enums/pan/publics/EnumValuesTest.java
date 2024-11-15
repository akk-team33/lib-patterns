package de.team33.patterns.enums.pan.publics;

import de.team33.patterns.enums.pan.EnumValues;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullSource;

import java.nio.file.AccessMode;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

@Deprecated
class EnumValuesTest {

    static final EnumValues<AccessMode> ACCESS_MODE_VALUES = EnumValues.of(AccessMode.class);

    @Test
    final void stream() {
        final AccessMode[] expected = AccessMode.values();
        final AccessMode[] result = ACCESS_MODE_VALUES.stream().toArray(AccessMode[]::new);
        assertArrayEquals(expected, result);
    }

    @ParameterizedTest
    @EnumSource
    final void mapAny(final AccessMode expected) {
        final Predicate<AccessMode> filter = item -> item.name().equals(expected.name());
        final Function<AccessMode, AccessMode> mapping = item -> item;

        final AccessMode result = ACCESS_MODE_VALUES.mapAny(filter, mapping);

        assertSame(expected, result);
    }

    @ParameterizedTest
    @EnumSource
    final void mapFirst(final AccessMode expected) {
        final Predicate<AccessMode> filter = item -> item.name().equals(expected.name());
        final Function<AccessMode, AccessMode> mapping = item -> item;

        final AccessMode result = ACCESS_MODE_VALUES.mapFirst(filter, mapping);

        assertSame(expected, result);
    }

    @Test
    final void resolving_default() {
        try {
            final Predicate<AccessMode> filter = item -> item.name().equals(UUID.randomUUID().toString());
            final Function<AccessMode, AccessMode> mapping = item -> item;

            final AccessMode result = ACCESS_MODE_VALUES.mapFirst(filter, mapping);
            fail("expected to fail - but was " + result);
        } catch (final NoSuchElementException e) {
            // as expected
        }
    }

    @ParameterizedTest
    @EnumSource
    @NullSource
    final void fallback_value(final AccessMode expected) {
        final Predicate<AccessMode> filter = item -> item.name().equals(UUID.randomUUID().toString());
        final Function<AccessMode, AccessMode> mapping = item -> item;

        final EnumValues<AccessMode> values = ACCESS_MODE_VALUES.fallback(expected);

        final AccessMode result = values.mapFirst(filter, mapping);
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @EnumSource
    @NullSource
    final void fallback_supplier(final AccessMode expected) {
        final Predicate<AccessMode> filter = item -> item.name().equals(UUID.randomUUID().toString());
        final Function<AccessMode, AccessMode> mapping = item -> item;

        final EnumValues<AccessMode> values = ACCESS_MODE_VALUES.fallback(() -> expected);

        final AccessMode result = values.mapFirst(filter, mapping);
        assertEquals(expected, result);
    }

    @Test
    final void failing() {
        try {
            final Predicate<AccessMode> filter = item -> item.name().equals(UUID.randomUUID().toString());
            final Function<AccessMode, AccessMode> mapping = item -> item;

            final EnumValues<AccessMode> values = ACCESS_MODE_VALUES.failing(IllegalStateException::new);

            final AccessMode result = values.mapFirst(filter, mapping);
            fail("expected to fail - but was " + result);
        } catch (final IllegalStateException e) {
            // as expected
        }
    }
}
