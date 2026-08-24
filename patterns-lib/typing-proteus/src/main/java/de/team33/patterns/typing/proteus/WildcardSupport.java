package de.team33.patterns.typing.proteus;

import java.lang.reflect.WildcardType;
import java.util.List;

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
