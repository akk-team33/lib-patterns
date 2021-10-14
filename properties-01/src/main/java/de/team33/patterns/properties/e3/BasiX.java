package de.team33.patterns.properties.e3;

import java.util.Map;
import java.util.function.Function;

public class BasiX<T> extends Basics<T> {

    private final Class<T> context;

    public BasiX(final Class<T> context, final Function<T, Map<String, Object>> mapping) {
        super(mapping);
        this.context = context;
    }

    public boolean equals(final T subject, final Object other) {
        return (subject == other) || (context.isInstance(other) && isEqual(subject, context.cast(other)));
    }
}
