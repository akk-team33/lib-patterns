package net.team33.patterns.reflect;

/**
 * Represents a tool to map properties from one Object to a new instance of (probably) another class.
 */
public interface Mapper {

    /**
     * Maps properties from an origin to a new instance of a given class.
     */
    <T> T map(Object origin, Class<T> targetClass);
}
