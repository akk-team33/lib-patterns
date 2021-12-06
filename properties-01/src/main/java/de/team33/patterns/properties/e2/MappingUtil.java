package de.team33.patterns.properties.e2;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

final class MappingUtil {

    private MappingUtil() {
    }

    @SuppressWarnings("AnonymousInnerClass")
    static <T> TargetOperation<Map<String, Object>>
    mappingOperation(final Map<String, ? extends Function<T, ?>> getters, final T origin) {
        return new TargetOperation<Map<String, Object>>() {
            @Override
            public <M extends Map<String, Object>> M to(final M target) {
                getters.forEach((name, getter) -> target.put(name, getter.apply(origin)));
                return target;
            }
        };
    }

    @SuppressWarnings("AnonymousInnerClass")
    static <T> TargetOperation<T>
    reMappingOperation(final Map<String, ? extends BiConsumer<T, Object>> setters, final Map<?, ?> origin) {
        return new TargetOperation<T>() {
            @Override
            public <U extends T> U to(final U target) {
                setters.forEach((name, setter) -> setter.accept(target, origin.get(name)));
                return target;
            }
        };
    }

    @SuppressWarnings("AnonymousInnerClass")
    static <T> TargetOperation<T>
    copyOperation(final Map<String, ? extends Accessor<T, Object>> accessors, final T origin) {
        return new TargetOperation<T>() {
            @Override
            public <U extends T> U to(final U target) {
                accessors.values().forEach(accessor -> accessor.accept(target, accessor.apply(origin)));
                return target;
            }
        };
    }
}
