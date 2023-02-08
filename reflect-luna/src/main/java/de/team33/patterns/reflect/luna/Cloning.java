package de.team33.patterns.reflect.luna;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class Cloning {

    public static final Cloning NOTHING = builder().build();

    private static final UnaryOperator<Object> IDENTITY = UnaryOperator.identity();

    @SuppressWarnings("rawtypes")
    private final Map<Class, UnaryOperator> backing;

    private Cloning(final Builder builder) {
        this.backing = Collections.unmodifiableMap(new HashMap<>(builder.backing));
    }

    public static Builder builder() {
        return new Builder();
    }

    public final <T> UnaryOperator<T> get(final Class<T> subjectClass) {
        //noinspection unchecked
        return Optional.ofNullable(backing.get(subjectClass)).orElse(IDENTITY);
    }

    public static class Builder {

        @SuppressWarnings("rawtypes")
        private final Map<Class, UnaryOperator> backing = new HashMap<>(0);

        public final <T> Function<UnaryOperator<T>, Builder> on(final Class<T> subjectClass) {
            return operator -> {
                backing.put(subjectClass, operator);
                return this;
            };
        }

        public final Cloning build() {
            return new Cloning(this);
        }
    }
}
