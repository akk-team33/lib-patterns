package de.team33.patterns.properties.e5;

import java.util.Map;
import java.util.function.BiConsumer;

class ReverseMapOperation<T> implements TargetOperation<T> {

    private final Map<String, ? extends BiConsumer<T, Object>> setters;
    private final Map<?, ?> origin;

    @SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
    ReverseMapOperation(final Map<String, ? extends BiConsumer<T, Object>> setters, final Map<?, ?> origin) {
        this.setters = setters;
        this.origin = origin;
    }

    @Override
    public final <T1 extends T> T1 to(final T1 target) {
        setters.forEach((name, setter) -> setter.accept(target, origin.get(name)));
        return target;
    }
}
