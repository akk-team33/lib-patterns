package de.team33.patterns.generics.atlas;

import java.lang.reflect.TypeVariable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.joining;

abstract class SingleSetup extends Setup {

    @Override
    final List<String> getFormalParameters() {
        return unmodifiableList(
                Stream.of(asClass().getTypeParameters())
                      .map(TypeVariable::getName)
                      .collect(Collectors.toList())
        );
    }

    @Override
    public final String toString() {
        final List<Setup> actual = getActualParameters();
        return asClass().getSimpleName() + (
                actual.isEmpty() ? "" : actual.stream()
                                              .map(Setup::toString)
                                              .collect(joining(", ", "<", ">")));
    }
}
