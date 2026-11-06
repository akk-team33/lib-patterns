package de.team33.patterns.typing.proteus;

import java.lang.reflect.TypeVariable;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

abstract class SingleSupport extends CoreSupport {

    private static String join(final List<? extends TypeSupport> actual) {
        return actual.isEmpty() ? "" : actual.stream()
                                             .map(TypeSupport::toString)
                                             .collect(joining(", ", "<", ">"));
    }

    @Override
    final List<String> formalParameters() {
        return features().get(Key.FORMAL_PARAMETERS,
                              () -> Stream.of(core().getTypeParameters())
                                          .map(TypeVariable::getName)
                                          .toList());
    }

    @Override
    final boolean isParametersCompatible(final TypeSupport other) {
        // Precondition: core().isAssignableFrom(other.core())
        if (actualParameters().isEmpty()) {
            // No matter if this is raw or simply has no parameters ...
            return true;
        } else if (other.isRaw()) {
            // => other is raw but this is not ...
            return false;
        } else {
            // Precondition: this.core() is not Object.class
            // => this must be in type hierarchy of other to be assignable
            return other.typeHierarchy().stream()
                        .filter(type -> core().equals(type.core()))
                        .findAny()
                        .map(TypeSupport::actualParameters)
                        .map(this::isParametersCompatible)
                        .orElseThrow(); // should not happen at all
        }
    }

    private boolean isParametersCompatible(final List<? extends TypeSupport> otherParameters) {
        // Precondition: actualParameters().size == otherParameters.size()
        return IntStream.range(0, otherParameters.size())
                        .allMatch(index -> isParameterCompatible(index, otherParameters.get(index)));
    }

    private boolean isParameterCompatible(final int index, final TypeSupport otherParameter) {
        final TypeSupport thisParameter = actualParameters().get(index);
        if (thisParameter instanceof final WildcardSupport wildcard) {
            return wildcard.isAssignableFrom(otherParameter);
        } else {
            return thisParameter.equals(otherParameter);
        }
    }


    @Override
    public final String toString() {
        return features().get(Key.TO_STRING,
                              () -> core().getCanonicalName() + join(actualParameters()));
    }
}
