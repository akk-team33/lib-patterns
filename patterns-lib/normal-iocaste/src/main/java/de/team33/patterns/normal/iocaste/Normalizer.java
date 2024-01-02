package de.team33.patterns.normal.iocaste;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * A tool to generate normalized representations of data objects.
 */
public class Normalizer {

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Returns a {@link Normal normalized} representation of a given data object.
     */
    public final Normal normal(final Object origin) {
        if (null == origin) {
            //noinspection ReturnOfNull
            return null;
        }

        final Function<Object, Object> method = methodFor(origin.getClass());
        final Object stage = method.apply(origin);
        return resultOf(stage);
    }

    private final Map<Class<?>, Function<Object, Object>> methods = new ConcurrentHashMap<>(0);

    private Function<Object, Object> methodFor(final Class<?> originClass) {
        return methods.computeIfAbsent(originClass, this::applicableFor);
    }

    private Function<Object, Object> applicableFor(final Class<?> originClass) {
        return Object::toString;
    }

    private Normal resultOf(final Object stage) {
        return new NormalSimple((CharSequence) stage);
    }

    public static class Builder {

        public final Normalizer build() {
            return new Normalizer();
        }
    }

    private enum Type {

        SIMPLE,
        AGGREGATE,
        COMPOSITE;

        static Type of(final Object stage) {
            throw new UnsupportedOperationException("not yet implemented");
        }
    }
}
