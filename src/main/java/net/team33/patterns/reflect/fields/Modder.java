package net.team33.patterns.reflect.fields;

import java.lang.reflect.Field;
import java.util.function.Consumer;

public enum Modder implements Consumer<Field> {

    SET_ACCESSIBLE {
        @Override
        public void accept(final Field field) {
            field.setAccessible(true);
        }
    }
}
