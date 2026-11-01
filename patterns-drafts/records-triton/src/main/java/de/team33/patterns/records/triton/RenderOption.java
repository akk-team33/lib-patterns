package de.team33.patterns.records.triton;

/**
 * Defines options for JSON rendering.
 */
public enum RenderOption {

    /**
     * Causes {@code null} values to be skipped during rendering of JSON objects.
     * <p>
     * By default, {@code null} values will be rendered.
     */
    SKIP_NULL,

    /**
     * Causes JSON objects to be rendered in a single line.
     * <p>
     * By default, JSON objects ar rendered in multi-line format.
     */
    INLINE_OBJECT,

    /**
     * Causes JSON arrays to be rendered in multi-line format.
     * <p>
     * By default, JSON arrays ar rendered in a single line.
     */
    FORMAT_ARRAY
}
