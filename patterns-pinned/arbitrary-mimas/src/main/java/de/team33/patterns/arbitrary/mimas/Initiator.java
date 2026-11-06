package de.team33.patterns.arbitrary.mimas;

import java.util.List;
import java.util.Map;

/**
 * @deprecated Consider using {@link Generator#anyDataset(Map)} to generate datasets and subsequently
 * create {@code record}s or other data objects from them.
 */
@Deprecated
public interface Initiator {

    /**
     * @deprecated see {@link Initiator}
     */
    @Deprecated
    default <T> T initiate(final Class<T> targetType, final String... ignore) {
        return new Initiating<>(this, targetType, List.of(ignore)).result();
    }
}
