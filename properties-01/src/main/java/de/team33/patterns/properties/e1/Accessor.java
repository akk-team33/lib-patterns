package de.team33.patterns.properties.e1;

import java.util.function.BiConsumer;
import java.util.function.Function;

public interface Accessor<T> extends Function<T, Object>, BiConsumer<T, Object> {
}
