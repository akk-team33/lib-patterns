package de.team33.patterns.generics.atlas;

import de.team33.patterns.lazy.narvi.Lazy;

import java.lang.reflect.TypeVariable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.joining;

abstract class SingleTypedef extends Typedef {

    private final transient Lazy<String> stringValue = Lazy.init(this::toStringValue);

    @Override
    public final List<String> getFormalParameters() {
        return unmodifiableList(
                Stream.of(asClass().getTypeParameters())
                      .map(TypeVariable::getName)
                      .collect(Collectors.toList())
        );
    }

    @Override
    public final String toString() {
        return stringValue.get();
    }

    private String toStringValue() {
        final List<Typedef> actual = getActualParameters();
        return asClass().getCanonicalName() + (
                actual.isEmpty() ? "" : actual.stream()
                                              .map(Typedef::toString)
                                              .collect(joining(", ", "<", ">")));
    }
}
