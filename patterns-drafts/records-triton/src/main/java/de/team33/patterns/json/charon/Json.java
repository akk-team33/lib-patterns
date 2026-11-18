package de.team33.patterns.json.charon;

import de.team33.patterns.records.triton.RenderOption;

import java.util.Set;

public class Json {

    public static String render(final JsonValue value, final Set<RenderOption> options) {
        return new Renderer(options).add(value, 0)
                                    .toString();
    }

    public static JsonValue parse(final String source) {
        return new Parser(source).parseRoot();
    }
}
