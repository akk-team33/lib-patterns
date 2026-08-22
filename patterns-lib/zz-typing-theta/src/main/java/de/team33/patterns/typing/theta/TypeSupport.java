package de.team33.patterns.typing.theta;

import de.team33.patterns.lazy.lambda.Features;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

abstract class TypeSupport {

    private final Features features = new Features();

    @Override
    public final boolean equals(final Object obj) {
        return (this == obj) || ((obj instanceof final TypeSupport other) && toList().equals(other.toList()));
    }

    @Override
    public final int hashCode() {
        return features.get(Key.HASH_CODE, () -> toList().hashCode());
    }

    @Override
    public abstract String toString();

    abstract Class<?> core();

    abstract List<String> formalParameters();

    abstract List<TypeSupport> actualParameters();

    final Features features() {
        return features;
    }

    final TypeSupport actualParameter(final String name) {
        final List<String> formalParameters = formalParameters();
        return Optional.of(formalParameters.indexOf(name))
                       .filter(index -> 0 <= index)
                       .map(index -> actualParameterByIndex(name, index))
                       .orElseThrow(() -> new IllegalArgumentException(
                               String.format("formal parameter <%s> not found in %s", name, formalParameters)));
    }

    final TypeSupport memberSupport(final Type type) {
        return TypeCase.support(type, this);
    }

    private List<Object> toList() {
        return features().get(Key.TO_LIST, () -> List.of(core(), actualParameters()));
    }

    private TypeSupport actualParameterByIndex(final String name, final int index) {
        final List<TypeSupport> actualParameters = actualParameters();
        if (index < actualParameters.size()) {
            return actualParameters.get(index);
        } else {
            throw new IllegalStateException(
                    String.format("actual parameter for <%s> not found in %s", name, actualParameters));
        }
    }

    interface Key<T> extends Features.Key<T> {

        Key<List<Object>> TO_LIST = named("TO_LIST");
        Key<Integer> HASH_CODE = named("HASH_CODE");
        Key<String> TO_STRING = named("TO_STRING");
        Key<List<String>> FORMAL_PARAMETERS = named("FORMAL_PARAMETERS");
        Key<List<TypeSupport>> ACTUAL_PARAMETERS = named("ACTUAL_PARAMETERS");

        static <T> Key<T> named(final String name) {
            return new Key<T>() {
                @Override
                public String toString() {
                    return name;
                }
            };
        }
    }
}
