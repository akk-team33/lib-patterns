package de.team33.patterns.collection.mneme.publics;

import de.team33.patterns.collection.mneme.ImmutableCollection;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ImmutableCollectionTest extends ImmutableCollectionTestBase<Object, ImmutableCollection<Object>> {

    private static final List<Object> ANY_LIST = Arrays.asList(Instant.now(), null, 2, "3");

    ImmutableCollectionTest() {
        super(ImmutableCollection.proxy(new ArrayList<>()),
              ImmutableCollection.proxy(new ArrayList<>(ANY_LIST)));
    }

    @Override
    final Object anyElement() {
        return Instant.now();
    }

    @Override
    final Collection<Object> anyElements() {
        //noinspection AssignmentOrReturnOfFieldWithMutableType
        return ANY_LIST;
    }

    @Test
    final void proxy_iterator() {
        final List<Object> result = new ArrayList<>();
        ImmutableCollection.proxy(ANY_LIST).iterator().forEachRemaining(result::add);
        assertEquals(ANY_LIST, result);
    }

    @Test
    final void proxy_size() {
        assertEquals(ANY_LIST.size(), ImmutableCollection.proxy(ANY_LIST).size());
    }
}