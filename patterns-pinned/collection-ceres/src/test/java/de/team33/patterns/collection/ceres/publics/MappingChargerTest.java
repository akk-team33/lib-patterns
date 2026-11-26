package de.team33.patterns.collection.ceres.publics;

import de.team33.patterns.collection.ceres.Mapping;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

class MappingChargerTest
        extends MappingSetupTestBase<Mapping.Charger<String, List<String>, Map<String, List<String>>>> {

    private static Map<String, List<String>> newMap() {
        return new TreeMap<>();
    }

    @Override
    final Mapping.Charger<String, List<String>, Map<String, List<String>>> setup() {
        return Mapping.charger(newMap());
    }

    @Override
    final Map<String, List<String>>
    resultOf(final Mapping.Charger<String, List<String>, Map<String, List<String>>> setup) {
        return setup.charged();
    }
}
