package de.team33.patterns.properties.e1;

import java.lang.reflect.Field;

class FieldProperty<T> implements Property<T> {

    private static final String CANNOT_GET_VALUE = "cannot get value of field from a given subject:%n" +
            "- field: %s%n" +
            "- class of subject: %s%n" +
            "- value of subject: %s%n";

    private final Field field;

    FieldProperty(final Field field) {
        this.field = field;
    }

    @Override
    public final String name() {
        return field.getName();
    }

    @Override
    public final Object valueOf(final T subject) {
        try {
            return field.get(subject);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(String.format(CANNOT_GET_VALUE, field, subject.getClass(), subject), e);
        }
    }

    @Override
    public final void setValueOf(final T subject, final Object value) {
        throw new UnsupportedOperationException("not yet implemented");
    }
}
