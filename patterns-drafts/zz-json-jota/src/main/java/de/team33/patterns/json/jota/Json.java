package de.team33.patterns.json.jota;

import de.team33.patterns.typing.proteus.Type;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.UnaryOperator;

public final class Json {

    private static final String SIMPLE_NAME = Json.class.getSimpleName();
    private static final Map<Type, MappingEntry> MAPPINGS = new ConcurrentHashMap<>();

    private Json() {}

    public static <S, T> void setup(final Class<S> srcType, final JsonType<T> tgtType,
                                    final UnaryOperator<Mapping<S, T>> operator) {
        setup(Type.of(srcType), tgtType, operator);
    }

    public static <S, T> void setup(final Type<S> srcType, final JsonType<T> tgtType,
                                    final UnaryOperator<Mapping<S, T>> operator) {
        MAPPINGS.compute(srcType, (key, found) -> apply(found, srcType, tgtType, operator));
    }

    private static <S, T> MappingEntry<S, T> apply(final MappingEntry<S, ?> found,
                                                   final Type<S> srcType,
                                                   final JsonType<T> tgtType,
                                                   final UnaryOperator<Mapping<S, T>> operator) {
        if (found == null) {
            final Mapping<S, T> mapping = operator.apply(newMapping(srcType, tgtType));
            return new MappingEntry<>(srcType, tgtType,
                                      Objects.requireNonNull(mapping, "operator returns null"));
        } else {
            throw new IllegalStateException(
                    ("A mapping already exists ...%n" +
                     "    source type: %s%n" +
                     "    target type: %s%n").formatted(srcType, found.tgtType));
        }
    }

    private static <S, T> Mapping<S, T> newMapping(final Type<S> srcType, final JsonType<T> tgtType) {
        Objects.requireNonNull(tgtType, "target typ not specified");
        return new Mapping<>(null, null);
    }

    private record MappingEntry<S, T>(Type<S> srcType, JsonType<T> tgtType, Mapping<S, T> mapping) {}
}
