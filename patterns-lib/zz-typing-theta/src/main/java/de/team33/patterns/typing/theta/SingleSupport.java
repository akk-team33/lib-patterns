package de.team33.patterns.typing.theta;

import java.lang.reflect.TypeVariable;
import java.util.List;
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
    public final String toString() {
        return features().get(Key.TO_STRING,
                              () -> core().getCanonicalName() + join(actualParameters()));
    }
}
