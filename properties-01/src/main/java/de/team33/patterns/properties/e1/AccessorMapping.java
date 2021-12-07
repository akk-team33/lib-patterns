package de.team33.patterns.properties.e1;

import java.util.Map;
import java.util.TreeMap;

import static java.util.Collections.unmodifiableMap;

class AccessorMapping<T> implements BiMapping<T> {

    private final Map<String, ? extends Accessor<T, Object>> methods;

    AccessorMapping(final Map<String, ? extends Accessor<T, Object>> methods) {
        this.methods = unmodifiableMap(new TreeMap<>(methods));
    }

    @Override
    public final TargetOperation<T> copy(final T origin) {
        return MappingUtil.copyOperation(methods, origin);
    }

    @Override
    public final TargetOperation<Map<String, Object>> map(final T origin) {
        return MappingUtil.mappingOperation(methods, origin);
    }

    @Override
    public final TargetOperation<T> remap(final Map<?, ?> origin) {
        return MappingUtil.reMappingOperation(methods, origin);
    }
}
