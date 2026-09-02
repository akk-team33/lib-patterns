package de.team33.patterns.records.triton;

import de.team33.patterns.records.metis.Metis;
import de.team33.patterns.typing.proteus.Type;

import java.util.List;

/**
 * @deprecated obsolete when using {@link Metis#description(Class)} or {@link Metis#description(Type)}.
 */
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
