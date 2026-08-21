package de.team33.patterns.records.triton;

import de.team33.patterns.typing.theta.Type;

import java.util.List;

/**
 * Provides structural information about a {@code record} type.
 * <p>
 * Use {@link Triton#descriptor(Class)} to get an instance.
 */
public interface Description<T extends Record> {

    /**
     * Returns the type of the described record.
     */
    Type<T> type();

    /**
     * Returns the names of the record components.
     */
    List<String> names();

    /**
     * Returns the type of the specified record component.
     */
    Type<?> componentType(String name);
}
