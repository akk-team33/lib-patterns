package net.team33.patterns.reflect.fields;

import java.lang.reflect.Field;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Provides {@link Function methods} to get a {@link Stream} of {@link Field Fields} from a {@link Class}.
 */
public enum ToStream implements Function<Class<?>, Stream<Field>> {

    /**
     * Delivers all {@link Field}s straightly declared by a given {@link Class}
     */
    FLAT {
        @Override
        public Stream<Field> apply(final Class<?> subject) {
            return Stream.of(subject.getDeclaredFields());
        }
    },

    /**
     * Delivers all {@link Field}s declared by a given {@link Class} or any of its superclasses.
     */
    DEEP {
        @Override
        public Stream<Field> apply(final Class<?> subject) {
            return (null == subject)
                    ? Stream.empty()
                    : Stream.concat(apply(subject.getSuperclass()), Stream.of(subject.getDeclaredFields()));
        }
    }
}
