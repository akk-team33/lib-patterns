package de.team33.patterns.json.jota;

import java.math.BigDecimal;

public class Json {

    private static final String SIMPLE_NAME = Json.class.getSimpleName();

    public static final class Type<R> {

        public static final Type<Boolean> BOOLEAN = new Type<>("BOOLEAN", Boolean.class);
        public static final Type<BigDecimal> NUMBER = new Type<>("NUMBER", BigDecimal.class);
        public static final Type<String> STRING = new Type<>("STRING", String.class);
        public static final Type<ArrayStage> ARRAY = new Type<>("ARRAY", ArrayStage.class);
        public static final Type<ObjectStage> OBJECT = new Type<>("OBJECT", ObjectStage.class);

        @SuppressWarnings("InnerClassFieldHidesOuterClassField")
        private static final String SIMPLE_NAME = Type.class.getSimpleName();

        private final String name;

        private Type(final String name, final Class<R> stageClass) {
            this.name = "%s.%s.%s".formatted(Json.SIMPLE_NAME, SIMPLE_NAME, name);
        }
    }
}
