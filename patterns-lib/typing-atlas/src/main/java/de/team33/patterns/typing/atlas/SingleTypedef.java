package de.team33.patterns.typing.atlas;

import de.team33.patterns.lazy.narvi.Lazy;

import java.lang.reflect.TypeVariable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.joining;

abstract class SingleTypedef extends Typedef {

    private final transient Lazy<String> stringValue = Lazy.init(this::toStringValue);
    private final transient Lazy<List<String>> formalParameters = Lazy.init(this::newFormalParameters);

    private List<String> newFormalParameters() {
        return unmodifiableList(
                Stream.of(asClass().getTypeParameters())
                      .map(TypeVariable::getName)
                      .collect(Collectors.toList())
        );
    }

    @Override
    final List<String> getFormalParameters() {
        return formalParameters.get();
    }

    private String toStringValue() {
        final List<? extends Typedef> actual = getActualParameters();
        return asClass().getCanonicalName() + (
                actual.isEmpty() ? "" : actual.stream()
                                              .map(Typedef::toString)
                                              .collect(joining(", ", "<", ">")));
    }

    @Override
    public final String toString() {
        return stringValue.get();
    }
}
