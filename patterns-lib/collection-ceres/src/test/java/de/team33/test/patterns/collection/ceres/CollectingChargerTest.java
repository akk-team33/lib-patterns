package de.team33.test.patterns.collection.ceres;

import de.team33.patterns.collection.ceres.Collecting;

import java.util.LinkedList;
import java.util.List;

public class CollectingChargerTest extends CollectingSetupTestBase<Collecting.Charger<String, List<String>>> {

    @Override
    final Collecting.Charger<String, List<String>> setup() {
        return Collecting.charger(new LinkedList<>());
    }

    @Override
    final List<String> resultOf(final Collecting.Charger<String, List<String>> charger) {
        return charger.charged();
    }
}
