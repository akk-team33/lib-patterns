package de.team33.patterns.typing.theta;

import java.util.List;

abstract class CoreSupport extends TypeSupport {

    private List<Object> toList() {
        return features().get(Key.TO_LIST, () -> List.of(core(), actualParameters()));
    }

    @Override
    public final boolean equals(final Object obj) {
        return (this == obj) || ((obj instanceof final CoreSupport other) && toList().equals(other.toList()));
    }

    @Override
    public final int hashCode() {
        return features().get(Key.HASH_CODE, () -> toList().hashCode());
    }
}
