package de.team33.patterns.json.charon;

import java.util.Set;

public final class Json {

    /**
     * A singleton representation of JSON {@code null} as regular {@link JsonValue}.
     */
    public static final JsonValue NULL = JsonNull.INSTANCE;

    private Json() {}

    /**
     * Returns a {@link JsonValue} that is parsed from a given JSON <em>source</em>.
     */
    public static JsonValue parse(final String source) {
        return new Parser(source).parse();
    }

    /**
     * Returns a JSON string from a given {@link JsonValue} applying the given <em>options</em>.
     */
    public static String render(final JsonValue value, final Set<RenderOption> options) {
        return new Renderer(options).render(value, 0)
                                    .toString();
    }
}
