package de.team33.test.patterns.features.e1;

import de.team33.patterns.features.e1.Features;
import de.team33.patterns.features.e1.UnknownKeyException;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class FeaturesTest {

    static final Features.Key<Object> UNKNOWN = () -> "UNKNOWN";
    static final Features.Key<Random> RANDOM = () -> "RANDOM";
    static final Features.Key<Date> DATE = () -> "DATE";
    static final Features.Key<Void> VOID = () -> "VOID";

    @SuppressWarnings("ConstantConditions")
    @Test
    final void get() {
        final Features features = Features.builder()
                                          .put(RANDOM, SecureRandom::new)
                                          .put(DATE, Date::new)
                                          .put(VOID, () -> null)
                                          .build();
        assertTrue(features.get(RANDOM) instanceof Random);
        assertTrue(features.get(DATE) instanceof Date);
        assertNull(features.get(VOID));
        assertThrows(UnknownKeyException.class, () -> features.get(UNKNOWN));
    }
}