package de.team33.patterns.properties.e5;

import java.util.Map;
import java.util.function.BiConsumer;

public interface ReMapping<T> {

    Map<String, ? extends BiConsumer<T, Object>> backing();

    default TargetOperation<T> map(final Map<?, ?> origin) {
        return new ReMappingOperation<>(backing(), origin);
    }
}
