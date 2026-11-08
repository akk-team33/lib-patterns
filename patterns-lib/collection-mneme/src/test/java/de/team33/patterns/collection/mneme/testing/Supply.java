package de.team33.patterns.collection.mneme.testing;

import de.team33.patterns.arbitrary.mimas.Generator;

import java.time.Instant;
import java.util.function.Supplier;

public class Supply extends Generator.Basic {

    private final Supplier<Object>[] suppliers = suppliers(
            this::anyByte, this::anyLong, this::anyBigInteger, this::anyString,
            this::anyInstant, this::anyDouble, Supply::new, () -> null);

    @SafeVarargs
    private static Supplier<Object>[] suppliers(final Supplier<Object>... suppliers) {
        return suppliers;
    }

    public final Instant anyInstant() {
        return Instant.now().plusMillis(anyInt());
    }

    public final Object anyObject() {
        return anyOf(suppliers).get();
    }
}
