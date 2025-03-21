package de.team33.patterns.collection.ceres.publics;

import de.team33.patterns.collection.ceres.Collecting;

import java.util.LinkedList;
import java.util.List;

class CollectingBuilderTest extends CollectingSetupTestBase<Collecting.Builder<String, List<String>>> {

    @Override
    final Collecting.Builder<String, List<String>> setup() {
        return Collecting.builder(LinkedList::new);
    }

    @Override
    final List<String> resultOf(final Collecting.Builder<String, List<String>> builder) {
        return builder.build();
    }
}
