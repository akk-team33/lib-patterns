package de.team33.patterns.typing.theta;

import de.team33.patterns.lazy.lambda.Lazy;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

abstract class Backing {

    private final transient Lazy<List<Object>> listView;
    private final transient Lazy<Integer> hashValue;
    private final transient Lazy<String> stringValue;

    Backing() {
        this.listView = Lazy.init(() -> Arrays.asList(core(), actualParameters()));
        this.hashValue = Lazy.init(() -> listView.get().hashCode());
        this.stringValue = Lazy.init(this::toStringValue);
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
        if (index < actualParameters.size())
            return actualParameters.get(index);
        else
            throw new IllegalStateException(
                    String.format("actual parameter for <%s> not found in %s", name, actualParameters));
    }

    final Backing memberBacking(final Type type) {
        return TypeCase.toAssembly(type, this);
    }

    private boolean equals(final Backing other) {
        return listView.get().equals(other.listView.get());
    }

    @Override
    public final boolean equals(final Object obj) {
        return (this == obj) || ((obj instanceof Backing) && equals((Backing) obj));
    }

    @Override
    public final int hashCode() {
        return hashValue.get();
    }

    abstract String toStringValue();

    @Override
    public final String toString() {
        return stringValue.get();
    }
}
