package de.team33.patterns.generics.atlas;

import java.util.List;
import java.util.Optional;

abstract class Setup {

    abstract Class<?> asClass();

    abstract List<String> getFormalParameters();

    abstract List<Setup> getActualParameters();

    final Setup getActualParameter(final String name) {
        final List<String> formalParameters = getFormalParameters();
        return Optional.of(formalParameters.indexOf(name))
                       .filter(index -> 0 <= index)
                       .map(index -> getActualParameters().get(index))
                       .orElseThrow(() -> new IllegalArgumentException(
                               String.format("formal parameter <%s> not found in %s", name, formalParameters)));
    }

    @Override
    public abstract String toString();
}
