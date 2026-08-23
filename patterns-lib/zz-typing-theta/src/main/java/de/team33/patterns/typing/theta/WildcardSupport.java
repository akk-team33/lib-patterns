package de.team33.patterns.typing.theta;

import java.lang.reflect.WildcardType;
import java.util.List;

final class WildcardSupport extends TypeSupport {

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

    @Override
    public final boolean equals(final Object obj) {
        return (this == obj) || ((obj instanceof final WildcardSupport other) && range.equals(other.range));
    }

    @Override
    public final int hashCode() {
        return features().get(Key.HASH_CODE, range::hashCode);
    }

    @Override
    public final String toString() {
        return features().get(Key.TO_STRING, range::toString);
    }
}
