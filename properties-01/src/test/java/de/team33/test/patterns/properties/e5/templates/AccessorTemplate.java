package de.team33.test.patterns.properties.e5.templates;

import de.team33.patterns.properties.e5.Accessor;
import de.team33.test.patterns.properties.shared.AnyClass;

import java.lang.reflect.Field;

class AccessorTemplate implements Accessor<AnyClass> {

    private final Field field;

    AccessorTemplate(final Field field) {
        this.field = field;
    }

    @Override
    public final void accept(final AnyClass anyClass, final Object s) {
        try {
            field.set(anyClass, s);
        } catch (final IllegalAccessException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    @Override
    public final Object apply(final AnyClass anyClass) {
        try {
            return field.get(anyClass);
        } catch (final IllegalAccessException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }
}
