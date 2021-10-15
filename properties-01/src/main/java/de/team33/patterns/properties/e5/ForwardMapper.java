package de.team33.patterns.properties.e5;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

@FunctionalInterface
public interface ForwardMapper<T> {

    Map<String, ? extends Function<T, ?>> backing();

    default TargetOperation<Map<String, Object>> map(final T origin) {
        return new ForwardMapOperation<>(backing(), origin);
    }

    default Map<String, Object> toMap(final T origin) {
        return map(origin).to(new TreeMap<>());
    }
}
