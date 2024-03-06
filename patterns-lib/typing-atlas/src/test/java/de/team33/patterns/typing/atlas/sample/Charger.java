package de.team33.patterns.typing.atlas.sample;

import java.util.Arrays;

public interface Charger {

    default <T> T charge(final T target, final String... ignore) {
        return new Charging<>(this, target, Arrays.asList(ignore)).result();
    }
}
