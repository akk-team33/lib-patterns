package de.team33.patterns.typing.atlas.sample;

import de.team33.patterns.typing.atlas.Type;

import java.util.Arrays;

public interface Charger {

    default <T> T charge(final Class<T> type, final T target, final String... ignore) {
        return charge(Type.of(type), target, ignore);
    }

    default <T> T charge(final Type<T> type, final T target, final String... ignore) {
        return new Charging<>(this, type, target, Arrays.asList(ignore)).result();
    }
}
