package de.team33.patterns.properties.e5;

import java.util.function.BiConsumer;
import java.util.function.Function;

public interface Accessor<T> extends Function<T, Object>, BiConsumer<T, Object> {
}
