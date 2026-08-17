package de.team33.patterns.typing.theta;

import java.lang.reflect.TypeVariable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.joining;

abstract class SingleBacking extends Backing {

    @Override
    final List<String> formalParameters() {
        return unmodifiableList(
                Stream.of(core().getTypeParameters())
                      .map(TypeVariable::getName)
                      .collect(Collectors.toList())
                               );
    }

    @Override
    final String toStringValue() {
        final List<Backing> actual = actualParameters();
        return core().getCanonicalName() + (
                actual.isEmpty() ? "" : actual.stream()
                                              .map(Backing::toString)
                                              .collect(joining(", ", "<", ">")));
    }
}
