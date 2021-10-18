package de.team33.patterns.properties.e5;

import java.util.Map;

public interface BiMapping<T> extends Mapping<T>, ReMapping<T> {

    Map<String, ? extends Accessor<T>> backing();

    default TargetOperation<T> copy(final T origin) {
        return new CopyOperation<T>(backing(), origin);
    }
}
