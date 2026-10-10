package de.team33.patterns.arbitrary.mimas;

import java.util.List;
import java.util.Map;

/**
 * @deprecated Consider using {@link Generator#anyDataset(Map)} to generate datasets and subsequently
 * create data objects from them.
 */
@Deprecated
public interface Charger {

    /**
     * @deprecated see {@link Charger}
     */
    @Deprecated
    default <T> T charge(final T target, final String... ignore) {
        return new Charging<>(this, target, List.of(ignore)).result();
    }
}
