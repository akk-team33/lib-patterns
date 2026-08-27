package de.team33.patterns.json.jota;

import java.math.BigDecimal;

public final class JsonType<R> {

    public static final JsonType<Boolean> BOOLEAN = new JsonType<>("BOOLEAN", Boolean.class);
    public static final JsonType<BigDecimal> NUMBER = new JsonType<>("NUMBER", BigDecimal.class);
    public static final JsonType<String> STRING = new JsonType<>("STRING", String.class);
    public static final JsonType<ArrayStage> ARRAY = new JsonType<>("ARRAY", ArrayStage.class);
    public static final JsonType<ObjectStage> OBJECT = new JsonType<>("OBJECT", ObjectStage.class);
    private static final String SIMPLE_NAME = JsonType.class.getSimpleName();
    private final String name;
    private final Class<R> stageClass;

    private JsonType(final String name, final Class<R> stageClass) {
        this.name = "%s.%s".formatted(SIMPLE_NAME, name);
        this.stageClass = stageClass;
    }

    @Override
    public final String toString() {
        return name;
    }
}
