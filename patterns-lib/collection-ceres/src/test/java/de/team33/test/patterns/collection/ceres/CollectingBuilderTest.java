package de.team33.test.patterns.collection.ceres;

import de.team33.patterns.collection.ceres.Collecting;

import java.util.LinkedList;
import java.util.List;

public class CollectingBuilderTest extends CollectingSetupTestBase<Collecting.Builder<String, List<String>>> {

    @Override
    Collecting.Builder<String, List<String>> setup() {
        return Collecting.builder(LinkedList::new);
    }

    @Override
    List<String> resultOf(final Collecting.Builder<String, List<String>> builder) {
        return builder.build();
    }
}
