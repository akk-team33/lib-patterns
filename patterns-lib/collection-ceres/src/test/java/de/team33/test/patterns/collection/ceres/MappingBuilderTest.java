package de.team33.test.patterns.collection.ceres;

import de.team33.patterns.collection.ceres.Mapping;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Supplier;

public class MappingBuilderTest
        extends MappingSetupTestBase<Mapping.Builder<String, List<String>, Map<String, List<String>>>> {

    private static final Supplier<Map<String, List<String>>> NEW_MAP = TreeMap::new;

    @Override
    final Mapping.Builder<String, List<String>, Map<String, List<String>>> setup() {
        return Mapping.builder(NEW_MAP);
    }

    @Override
    final Map<String, List<String>>
    resultOf(final Mapping.Builder<String, List<String>, Map<String, List<String>>> setup) {
        return setup.build();
    }
}
