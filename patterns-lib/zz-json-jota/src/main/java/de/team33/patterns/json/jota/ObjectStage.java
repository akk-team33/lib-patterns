package de.team33.patterns.json.jota;

import de.team33.patterns.building.elara.DataBuilder;

import java.util.*;
import java.util.function.BiFunction;

public class ObjectStage extends AbstractMap<String, Object> {

    private final Map<String, Object> core;

    private ObjectStage(final Map<String, Object> core) {
        this.core = Collections.unmodifiableMap(new LinkedHashMap<>(core));
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public final Set<Entry<String, Object>> entrySet() {
        return core.entrySet();
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    public static final class Builder extends DataBuilder<Map<String, Object>, ObjectStage, Builder> {

        private Builder() {
            super(new LinkedHashMap<>(), ObjectStage::new, Builder.class);
        }

        public final Builder put(final String key, final Object value) {
            return setup(map -> map.put(key, value));
        }

        public final Builder putIfAbsent(final String key, final Object value) {
            return setup(map -> map.putIfAbsent(key, value));
        }

        public final Builder putAll(final Map<String, ?> other) {
            return setup(map -> map.putAll(other));
        }

        public final Builder remove(final Object key) {
            return setup(map -> map.remove(key));
        }

        public final Builder remove(final Object key, final Object value) {
            return setup(map -> map.remove(key, value));
        }

        public final Builder replace(final String key, final Object value) {
            return setup(map -> map.replace(key, value));
        }

        public final Builder replace(final String key, final Object oldValue, final Object newValue) {
            return setup(map -> map.replace(key, oldValue, newValue));
        }

        public final Builder replaceAll(final BiFunction<? super String, ? super Object, ?> function) {
            return setup(map -> map.replaceAll(function));
        }

        public final Builder merge(final String key, final Object value,
                                   final BiFunction<? super Object, ? super Object, ?> remappingFunction) {
            return setup(map -> map.merge(key, value, remappingFunction));
        }

        public final Builder clear() {
            return setup(Map::clear);
        }
    }
}
