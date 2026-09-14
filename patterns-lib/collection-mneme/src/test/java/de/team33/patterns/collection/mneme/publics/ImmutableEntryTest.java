package de.team33.patterns.collection.mneme.publics;

import de.team33.patterns.collection.mneme.ImmutableEntry;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.Temporal;
import java.util.AbstractMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ImmutableEntryTest {

    private final Map.Entry<UUID, Instant> other =
            new AbstractMap.SimpleEntry<>(UUID.randomUUID(), Instant.now().minusMillis(Integer.MAX_VALUE));
    private final Map.Entry<UUID, Instant> original =
            new AbstractMap.SimpleEntry<>(UUID.randomUUID(), Instant.now());
    private final ImmutableEntry<Comparable<?>, Temporal> immutable =
            ImmutableEntry.proxy(original);

    @Test
    final void testEquals() {
        //noinspection EqualsWithItself
        assertEquals(immutable, immutable);
        assertEquals(original, immutable);
        assertEquals(immutable, original);
        assertNotEquals(immutable, other);
        //noinspection MisorderedAssertEqualsArguments
        assertNotEquals(immutable, this);
        assertNotEquals(this, immutable);
    }

    @Test
    final void testHashCode() {
        assertEquals(original.hashCode(), immutable.hashCode());
    }

    @Test
    final void testToString() {
        assertEquals(original.toString(), immutable.toString());
    }

    @Test
    final void setValue() {
        final ImmutableEntry<Integer, String> entry = ImmutableEntry.proxy(new AbstractMap.SimpleEntry<>(1, "value"));
        assertThrows(UnsupportedOperationException.class, () -> entry.setValue(null));
        assertThrows(UnsupportedOperationException.class, () -> entry.setValue("value"));
        assertThrows(UnsupportedOperationException.class, () -> entry.setValue("another value"));
    }
}