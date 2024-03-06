package de.team33.patterns.typing.atlas.sample;

import de.team33.patterns.typing.atlas.Type;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static java.util.Collections.unmodifiableMap;

final class Types {

    private static final Map<Type<?>, Collection<Type<?>>> MATCHING = newMatching();

    private Types() {
    }

    private static Map<Type<?>, Collection<Type<?>>> newMatching() {
        final Map<Type<?>, Collection<Type<?>>> result = new HashMap<>();
        for (final Primary primary : Primary.values()) {
            result.put(Type.of(primary.type), primary.matching);
            result.put(Type.of(primary.boxed), primary.matching);
        }
        return unmodifiableMap(result);
    }

    static boolean isMatching(final Type<?> desired,
                              final Type<?> found) {
        return Optional.ofNullable(MATCHING.get(found))
                       .orElseGet(() -> singleton(found))
                       .contains(desired);
    }

    @SuppressWarnings("PackageVisibleField")
    private enum Primary {

        BOOLEAN(boolean.class, Boolean.class, false),
        BYTE(byte.class, Byte.class, (byte) 0),
        SHORT(short.class, Short.class, (short) 0),
        INT(int.class, Integer.class, 0),
        LONG(long.class, Long.class, 0L),
        FLOAT(float.class, Float.class, 0.0f),
        DOUBLE(double.class, Double.class, 0.0),
        CHAR(char.class, Character.class, '\0');

        final Class<?> type;
        final Class<?> boxed;
        final List<Type<?>> matching;
        final Object value;

        <T> Primary(final Class<T> type, final Class<T> boxed, final T value) {
            this.type = type;
            this.boxed = boxed;
            this.value = value;
            this.matching = asList(Type.of(type),
                                   Type.of(boxed));
        }
    }
}
