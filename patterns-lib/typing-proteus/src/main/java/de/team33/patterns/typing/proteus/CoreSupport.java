package de.team33.patterns.typing.proteus;

import java.util.List;

@SuppressWarnings("EqualsDoesntCheckParameterClass")
abstract class CoreSupport extends TypeSupport {

    private static final Equation<CoreSupport> EQUATION = Equation.of(CoreSupport.class, CoreSupport::toList);

    private List<Object> toList() {
        return features().get(Key.TO_LIST, () -> List.of(core(), actualParameters()));
    }

    @Override
    public final boolean equals(final Object obj) {
        return EQUATION.equals(this, obj);
    }

    @Override
    public final int hashCode() {
        return features().get(Key.HASH_CODE, () -> EQUATION.hashCode(this));
    }
}
