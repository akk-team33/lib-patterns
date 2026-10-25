package de.team33.patterns.typing.proteus;

import de.team33.patterns.value.sinope.Equation;

import java.util.List;

@SuppressWarnings("EqualsDoesntCheckParameterClass")
abstract class CoreSupport extends TypeSupport {

    private static final Equation<CoreSupport> EQUATION = Equation.of(CoreSupport.class, CoreSupport::toList);

    private List<Object> toList() {
        return features().get(Key.TO_LIST, () -> List.of(core(), actualParameters()));
    }

    @Override
    final boolean isAssignableFrom(final TypeSupport other) {
        if (other instanceof final WildcardSupport wildcard) {
            return isAssignableFrom(wildcard.upperBound());
        } else {
            return core().isAssignableFrom(other.core()) && isParametersCompatible(other);
        }
    }

    abstract boolean isParametersCompatible(final TypeSupport other);

    @Override
    public final boolean equals(final Object obj) {
        return EQUATION.equals(this, obj);
    }

    @Override
    public final int hashCode() {
        return features().get(Key.HASH_CODE, () -> EQUATION.hashCode(this));
    }
}
