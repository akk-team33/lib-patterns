package de.team33.patterns.records.triton;

import java.util.List;

/**
 * Provides structural information about a {@code record} type.
 * <p>
 * Use {@link Triton#descriptor(Class)} to get an instance.
 *
 * @deprecated consider {@link Description} as a replacement.
 */
@SuppressWarnings("unused")
@Deprecated
public interface Descriptor<T extends Record> {

    /**
     * Returns the underlying {@code record} type.
     */
    Class<T> recordType();

    /**
     * Returns the names of the record components of the underlying {@code record} type.
     */
    List<String> names();

    /**
     * Returns the declared type of the specified {@code record} component.
     */
    Class<?> type(String name);
}
