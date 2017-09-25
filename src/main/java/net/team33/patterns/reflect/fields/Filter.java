package net.team33.patterns.reflect.fields;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.function.Predicate;

/**
 * Provides some predefined {@linkplain Predicate filters} for {@link Field Fields}.
 */
public enum Filter implements Predicate<Field> {

    /**
     * Defines a filter accepting all fields (including static fields).
     */
    ALL {
        @Override
        public boolean test(final Field field) {
            return true;
        }
    },

    /**
     * Defines a filter accepting all public fields.
     */
    STATIC {
        @Override
        public boolean test(final Field field) {
            return Modifier.isStatic(field.getModifiers());
        }
    },

    /**
     * Defines a filter accepting all public fields.
     */
    PUBLIC {
        @Override
        public boolean test(final Field field) {
            return Modifier.isPublic(field.getModifiers());
        }
    },

    /**
     * Defines a filter accepting all public fields.
     */
    TRANSIENT {
        @Override
        public boolean test(final Field field) {
            return Modifier.isTransient(field.getModifiers());
        }
    },

    /**
     * Defines a filter accepting all instance-fields (non-static fields).
     */
    INSTANCE {
        @Override
        public boolean test(final Field field) {
            return STATIC.negate().test(field);
        }
    },

    /**
     * Defines a filter accepting all but static or transient fields.
     * Those fields should be significant for a type with value semantics.
     */
    SIGNIFICANT {
        @Override
        public boolean test(final Field field) {
            return STATIC.or(TRANSIENT).negate().test(field);
        }
    }
}
