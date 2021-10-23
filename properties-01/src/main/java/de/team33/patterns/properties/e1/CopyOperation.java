package de.team33.patterns.properties.e1;

import java.util.Map;

class CopyOperation<T> implements TargetOperation<T> {

    private final Map<String, ? extends Accessor<T>> accessors;
    private final T origin;

    CopyOperation(final Map<String, ? extends Accessor<T>> accessors, final T origin) {
        this.accessors = accessors;
        this.origin = origin;
    }

    @Override
    public final <TX extends T> TX to(final TX target) {
        accessors.values().forEach(accessor -> accessor.accept(target, accessor.apply(origin)));
        return target;
    }
}
