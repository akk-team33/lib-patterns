package de.team33.test.patterns.collection.ceres.publics;

import de.team33.patterns.collection.ceres.Mapping;
import de.team33.test.patterns.collection.ceres.testing.Supply;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

abstract class MappingSetupTestBase<S extends Mapping.Setup<String, List<String>, Map<String, List<String>>, S>>
        extends Supply {

    abstract S setup();

    abstract Map<String, List<String>> resultOf(S setup);

    @Test
    final void put() {
        final Map<String, List<String>> expected = anyStringListMap(3);
        final List<String> keys = new ArrayList<>(expected.keySet());

        final Map<String, List<String>> result = resultOf(setup().put(keys.get(0), expected.get(keys.get(0)))
                                                                 .put(keys.get(1), expected.get(keys.get(1)))
                                                                 .put(keys.get(2), expected.get(keys.get(2))));

        assertEquals(expected, result);
    }

    @Test
    final void remove() {
        final Map<String, List<String>> origin = anyStringListMap(3);
        final List<String> keys = new ArrayList<>(origin.keySet());
        final S setup = setup().putAll(origin);

        final Map<String, List<String>> result = resultOf(setup.remove(null)
                                                               .remove(anyInt())
                                                               .remove(keys.get(0))
                                                               .remove(keys.get(1))
                                                               .remove(keys.get(2)));

        assertEquals(Collections.emptyMap(), result);
    }

    @Test
    final void putAll() {
        final Map<String, List<String>> expected = anyStringListMap(3);

        final Map<String, List<String>> result = resultOf(setup().putAll(expected));

        assertEquals(expected, result);
    }

    @Test
    final void clear() {
        final S setup = setup().putAll(anyStringListMap(3));

        final Map<String, List<String>> result = resultOf(setup.clear());

        assertEquals(Collections.emptyMap(), result);
    }
}
