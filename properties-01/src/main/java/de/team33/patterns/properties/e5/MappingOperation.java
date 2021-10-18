package de.team33.patterns.properties.e5;

import java.util.Map;
import java.util.function.Function;

class MappingOperation<T> implements TargetOperation<Map<String, Object>> {

    private final Map<String, ? extends Function<T, ?>> getters;
    private final T origin;

    MappingOperation(final Map<String, ? extends Function<T, ?>> getters, final T origin) {
        this.getters = getters;
        this.origin = origin;
    }

    @Override
    public final <M extends Map<String, Object>> M to(final M target) {
        getters.forEach((name, getter) -> target.put(name, getter.apply(origin)));
        return target;
    }
}
