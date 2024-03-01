package de.team33.patterns.typing.atlas;

import de.team33.patterns.lazy.narvi.Lazy;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

abstract class Typedef {

    private final transient Lazy<List<Object>> listView;
    private final transient Lazy<Integer> hashValue;

    Typedef() {
        this.listView = Lazy.init(() -> Arrays.asList(asClass(), getActualParameters()));
        this.hashValue = Lazy.init(() -> listView.get().hashCode());
    }

    abstract Class<?> asClass();

    abstract List<String> getFormalParameters();

    abstract List<? extends Typedef> getActualParameters();

    final Typedef getActualParameter(final String name) {
        final List<String> formalParameters = getFormalParameters();
        return Optional.of(formalParameters.indexOf(name))
                       .filter(index -> 0 <= index)
                       .map(index -> getActualParameterByIndex(name, index))
                       .orElseThrow(() -> new IllegalArgumentException(
                               String.format("formal parameter <%s> not found in %s", name, formalParameters)));
    }

    private Typedef getActualParameterByIndex(final String name, final int index) {
        final List<? extends Typedef> actualParameters = getActualParameters();
        if (index < actualParameters.size()) {
            return actualParameters.get(index);
        } else {
            throw new IllegalStateException(
                    String.format("actual parameter for <%s> not found in %s", name, actualParameters));
        }
    }

    @SuppressWarnings("DesignForExtension")
    Typedef getMemberType(final Type type) {
        return TypeCase.toTypedef(type, this);
    }

    private boolean equals(final Typedef other) {
        return listView.get().equals(other.listView.get());
    }

    /**
     * Two instances of <em>Type</em> are equal if they are {@linkplain #asClass() based} on the same class
     * and defined by the same {@linkplain #getActualParameters() actual parameters}.
     */
    @Override
    public final boolean equals(final Object obj) {
        return (this == obj) || ((obj instanceof Typedef) && equals((Typedef) obj));
    }

    @Override
    public final int hashCode() {
        return hashValue.get();
    }

    @Override
    public abstract String toString();
}
