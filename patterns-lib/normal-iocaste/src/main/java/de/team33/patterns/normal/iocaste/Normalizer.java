package de.team33.patterns.normal.iocaste;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * A tool to generate normalized representations of data objects.
 */
public class Normalizer {

    private final Map<Class<?>, Function<Object, Object>> methods = new ConcurrentHashMap<>(0);
    private final List<Entry> entries;

    private Normalizer(final List<Entry> entries) {
        this.entries = Collections.unmodifiableList(new ArrayList<>(entries));
    }

    public static Builder builder() {
        return new Builder().setToSimple(Boolean.class, Character.class, Number.class)
                            .setToSimple(CharSequence.class, Function.identity());
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

    private Function<Object, Object> methodFor(final Class<?> originClass) {
        return methods.computeIfAbsent(originClass, this::applicableFor);
    }

    private Function<Object, Object> applicableFor(final Class<?> originClass) {
        return entries.stream()
                      .filter(entry -> entry.type.isAssignableFrom(originClass))
                      .reduce(Normalizer::prior)
                      .map(entry -> entry.method)
                      .orElseThrow(() -> new IllegalArgumentException("nor method found for " + originClass));
    }

    private static Entry prior(final Entry left, final Entry right) {
        return right.type.isAssignableFrom(left.type) ? left : right;
    }

    private Normal resultOf(final Object stage) {
        return new NormalSimple((CharSequence) stage);
    }

    private enum Type {

        SIMPLE,
        AGGREGATE,
        COMPOSITE;

        static Type of(final Object stage) {
            throw new UnsupportedOperationException("not yet implemented");
        }
    }

    private static class Entry {

        final Class<?> type;
        final Function<Object, Object> method;

        @SuppressWarnings({"rawtypes", "unchecked"})
        private Entry(final Class type, final Function method) {
            this.type = type;
            this.method = method;
        }
    }

    public static class Builder {

        private final List<Entry> entries = new LinkedList<>();

        public final Normalizer build() {
            return new Normalizer(entries);
        }

        public final <T> Builder setToSimple(final Class<T> originClass, final Function<T, CharSequence> method) {
            entries.removeIf(entry -> entry.type.equals(originClass));
            entries.add(new Entry(originClass, method));
            return this;
        }

        public final Builder setToSimple(final Class<?> ... classes) {
            return Stream.of(classes)
                         .map(originClass -> setToSimple(originClass, Object::toString))
                         .reduce(this, (left, right) -> right);
        }
    }
}
