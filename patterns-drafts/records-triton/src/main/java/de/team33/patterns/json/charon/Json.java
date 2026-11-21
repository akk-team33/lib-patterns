package de.team33.patterns.json.charon;

import de.team33.patterns.records.triton.RenderOption;

import java.util.Set;

public class Json {

    public static final JsonItem NULL = JsonNull.INSTANCE;

    public static String render(final JsonItem value, final Set<RenderOption> options) {
        return new Renderer(options).add(value, 0)
                                    .toString();
    }

    public static JsonItem parse(final String source) {
        return new Parser(source).parseRoot();
    }

    /**
     * Identifies the type of a JSON value.
     */
    public enum Type {

        /**
         * Identifies a JSON null.
         */
        NULL,

        /**
         * Identifies a JSON boolean.
         */
        BOOLEAN,

        /**
         * Identifies a JSON number.
         */
        NUMBER,

        /**
         * Identifies a JSON string.
         */
        STRING,

        /**
         * Identifies a JSON array.
         */
        ARRAY,

        /**
         * Identifies a JSON object.
         */
        OBJECT;
    }
}
