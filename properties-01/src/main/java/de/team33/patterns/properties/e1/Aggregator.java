package de.team33.patterns.properties.e1;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

class Aggregator<T> {

    private final Map<String, MethodProperty<T>> backing = new TreeMap<>();

    final void add(final Method method) {
        final MethodType type = MethodType.of(method);
        final String name = type.formalName(method);
        backing.computeIfAbsent(name, MethodProperty::new)
               .set(type, method);
    }

    final void addAll(final Aggregator<T> other) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    final Stream<Property<T>> stream() {
        return backing.values().stream().map(MethodProperty::moreCommon);
    }
}
