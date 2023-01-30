package de.team33.sample.patterns.building.elara;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class Fields {

    private static final int SYNTHETIC = 0x00001000;
    private static final int NOT_SIGNIFICANT = Modifier.STATIC | Modifier.TRANSIENT | SYNTHETIC;

    public static boolean significant(final Field field) {
        return significant(field.getModifiers());
    }

    private static boolean significant(final int modifiers) {
        return 0 == (modifiers & NOT_SIGNIFICANT);
    }
}
