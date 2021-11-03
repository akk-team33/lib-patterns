package de.team33.patterns.features.e1;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import static java.util.Collections.unmodifiableMap;
import static java.util.stream.Collectors.toMap;

public class Features {

    private final Map<Key<?>, Object> backing;

    public Features(final Builder builder) {
        this.backing = unmodifiableMap(builder.backing.entrySet()
                                                      .stream()
                                                      .collect(HashMap::new, Features::put, Map::putAll));
    }

    private static <R> void put(final Map<Key<?>, Object> map, final Map.Entry<Key<?>, Supplier<?>> entry) {
        map.put(entry.getKey(), entry.getValue().get());
    }

    public static Builder builder() {
        return new Builder();
    }

    @SuppressWarnings("unchecked")
    public final <T> T get(final Key<T> key) {
        return (T) Optional.ofNullable(backing.get(key))
                           .orElseThrow(() -> new UnknownKeyException(key));
    }

    @FunctionalInterface
    public interface Key<T> {

        String name();
    }

    public static class Builder {

        private final Map<Key<?>, Supplier<?>> backing = new HashMap<>(0);

        public final <T> Builder put(final Key<T> key, final Supplier<? extends T> supplier) {
            backing.put(key, supplier);
            return this;
        }

        public final Features build() {
            return new Features(this);
        }
    }

}
