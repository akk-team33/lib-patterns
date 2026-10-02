package de.team33.patterns.typing.proteus;

import de.team33.patterns.value.sinope.Equation;

import java.lang.reflect.WildcardType;
import java.util.List;
import java.util.Optional;

final class WildcardSupport extends TypeSupport {

    private static final Equation<WildcardSupport> EQUATION =
            Equation.of(WildcardSupport.class, type -> type.range);

    private final TypeRange range;

    WildcardSupport(final WildcardType type, final TypeSupport context) {
        this.range = TypeRange.by(type, context);
    }

    @SuppressWarnings("ReturnOfNull")
    @Override
    final Class<?> core() {
        return null;
    }

    @Override
    final List<String> formalParameters() {
        return List.of();
    }

    @Override
    final List<TypeSupport> actualParameters() {
        return List.of();
    }

    final TypeSupport upperBound() {
        return range.upperBounds().get(0);
    }

    @SuppressWarnings("WeakerAccess")
    final Optional<TypeSupport> lowerBound() {
        return range.lowerBounds().stream().findFirst();
    }

    @Override
    final boolean isAssignableFrom(final TypeSupport other) {
        if (other instanceof final WildcardSupport wildcard) {
            return isAssignableFromWildcard(wildcard);
        } else if (upperBound().isAssignableFrom(other)) {
            return lowerBound().map(other::isAssignableFrom)
                               .orElse(true);
        } else {
            return false;
        }
    }

    private boolean isAssignableFromWildcard(final WildcardSupport other) {
        if (upperBound().isAssignableFrom(other.upperBound())) {
            return lowerBound().map(leftLower -> other.lowerBound()
                                                      .map(rightLower -> rightLower.isAssignableFrom(leftLower))
                                                      .orElse(false))
                               .orElse(true);
        } else {
            return false;
        }
    }

    @Override
    public final boolean equals(final Object obj) {
        return EQUATION.equals(this, obj);
    }

    @Override
    public final int hashCode() {
        return features().get(Key.HASH_CODE, () -> EQUATION.hashCode(this));
    }

    @Override
    public final String toString() {
        return features().get(Key.TO_STRING, () -> EQUATION.toString(this));
    }
}
