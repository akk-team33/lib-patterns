package de.team33.patterns.properties.e2;

import de.team33.patterns.lazy.e1.Lazy;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

/**
 * Simplifies the implementation of {@link Object#equals(Object)}, {@link Object#hashCode()}, {@link Object#toString()}
 * and also enables the implementation of a method <em>toMap()</em> for types with value semantics based on properties.
 * <p>
 * The information required for this is cashed so that it does not have to be constantly re-determined.
 * However, this means that applying types must be immutable!
 */
public class Basics {

    private final Lazy<Map<String, Object>> map;
    private final Lazy<String> string = new Lazy<>(() -> mapView().toString());
    private final Lazy<Integer> hash = new Lazy<>(() -> mapView().hashCode());

    private <T> Basics(final T subject, final Function<T, ? extends Map<String, Object>> toMap) {
        map = new Lazy<>(() -> toMap.apply(subject));
    }

    public static <T> Function<T, Basics> factory(final Function<T, ? extends Map<String, Object>> toMap) {
        return t -> new Basics(t, toMap);
    }

    public final Map<String, Object> mapView() {
        return map.get();
    }

    public final String stringView() {
        return string.get();
    }

    public final int hashView() {
        return hash.get();
    }

    public final boolean isEqual(final Basics other) {
        return mapView().equals(other.mapView());
    }
}
