package de.team33.patterns.typing.theta;

import de.team33.patterns.lazy.lambda.Features;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

abstract class Backing {

    final Features features = new Features();

    private List<Object> listView() {
        return features.get(Key.LIST_VIEW, () -> List.of(core(), actualParameters()));
    }

    abstract Class<?> core();

    abstract List<String> formalParameters();

    abstract List<Backing> actualParameters();

    final Backing getActualParameter(final String name) {
        final List<String> formalParameters = formalParameters();
        return Optional.of(formalParameters.indexOf(name))
                       .filter(index -> 0 <= index)
                       .map(index -> getActualParameterByIndex(name, index))
                       .orElseThrow(() -> new IllegalArgumentException(
                               String.format("formal parameter <%s> not found in %s", name, formalParameters)));
    }

    private Backing getActualParameterByIndex(final String name, final int index) {
        final List<Backing> actualParameters = actualParameters();
        if (index < actualParameters.size()) {
            return actualParameters.get(index);
        } else {
            throw new IllegalStateException(
                    String.format("actual parameter for <%s> not found in %s", name, actualParameters));
        }
    }

    final Backing memberBacking(final Type type) {
        return TypeCase.toBacking(type, this);
    }

    @Override
    public final boolean equals(final Object obj) {
        return (this == obj) || ((obj instanceof final Backing other) && listView().equals(other.listView()));
    }

    @Override
    public final int hashCode() {
        return features.get(Key.HASH_CODE, () -> listView().hashCode());
    }

    @Override
    public abstract String toString();

    interface Key<T> extends Features.Key<T> {

        Key<List<Object>> LIST_VIEW = named("LIST_VIEW");
        Key<Integer> HASH_CODE = named("HASH_VALUE");
        Key<String> TO_STRING = named("TO_STRING");
        Key<List<String>> FORMAL_PARAMETERS = named("FORMAL_PARAMETERS");

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
