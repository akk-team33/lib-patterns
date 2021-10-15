package de.team33.patterns.properties.e4;

import java.util.Map;

/**
 * Abstracts a tool used to handle instances of a particular type by bringing their properties into a unified
 * representation, more precisely into a {@link Map} representation.
 *
 * @param <T> The type whose instances can be analyzed.
 */
public interface Uniformer<T> {

    TargetOperation<Map<String, Object>> map(T origin);
}
