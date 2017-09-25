package net.team33.patterns.reflect;

import java.lang.reflect.Field;
import java.util.function.Function;
import java.util.stream.Stream;

public enum FieldsToStream implements Function<Class<?>, Stream<Field>> {

    /**
     * Delivers all {@link Field}s straightly declared by a given {@link Class}
     */
    SIMPLE {
        @Override
        public Stream<Field> apply(final Class<?> subject) {
            return Stream.of(subject.getDeclaredFields());
        }
    },

    /**
     * Delivers all {@link Field}s declared by a given {@link Class} or any of its superclasses.
     */
    HIERARCHICAL {
        @Override
        public Stream<Field> apply(final Class<?> subject) {
            return (null == subject)
                    ? Stream.empty()
                    : Stream.concat(apply(subject.getSuperclass()), Stream.of(subject.getDeclaredFields()));
        }
    }
}
