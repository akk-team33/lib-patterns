package de.team33.patterns.typing.proteus;

import de.team33.patterns.lazy.janus.Features;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

abstract class TypeSupport {

    private final Features features = new Features();

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

    private TypeSupport actualParameterByIndex(final String name, final int index) {
        final List<TypeSupport> actualParameters = actualParameters();
        if (index < actualParameters.size()) {
            return actualParameters.get(index);
        } else {
            throw new IllegalStateException(
                    String.format("actual parameter for <%s> not found in %s", name, actualParameters));
        }
    }

    abstract Class<?> core();

    abstract List<String> formalParameters();

    abstract List<TypeSupport> actualParameters();

    @Override
    public abstract boolean equals(final Object obj);

    @Override
    public abstract int hashCode();

    @Override
    public abstract String toString();

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
