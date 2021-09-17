package de.team33.patterns.properties.e1;

interface Property<T> {

    String name();

    Object valueOf(T subject);

    void setValueOf(T subject, Object value);
}
