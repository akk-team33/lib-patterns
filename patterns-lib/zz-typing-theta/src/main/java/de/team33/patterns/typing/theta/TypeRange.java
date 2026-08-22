package de.team33.patterns.typing.theta;

import de.team33.patterns.lazy.lambda.Features;

import java.util.List;

abstract class TypeRange {

    private final Features features = new Features();

    abstract List<TypeSupport> upperBounds();

    abstract List<TypeSupport> lowerBounds();

    final Features features() {
        return features;
    }

    private List<Object> toList() {
        return features.get(Key.TO_LIST, () -> List.of(upperBounds(), lowerBounds()));
    }

    @Override
    public final boolean equals(final Object obj) {
        return (this == obj) || ((obj instanceof final TypeRange other) && toList().equals(other.toList()));
    }

    @Override
    public final int hashCode() {
        return features.get(Key.HASH_CODE, () -> toList().hashCode());
    }

    @Override
    public abstract String toString();

    interface Key<T> extends Features.Key<T> {

        Key<List<Object>> TO_LIST = named("TO_LIST");
        Key<Integer> HASH_CODE = named("HASH_CODE");

        static <T> Key<T> named(final String name) {
            return new Key<>() {
                @Override
                public String toString() {
                    return name;
                }
            };
        }
    }
}
