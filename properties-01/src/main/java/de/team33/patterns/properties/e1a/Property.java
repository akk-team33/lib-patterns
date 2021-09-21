package de.team33.patterns.properties.e1a;

interface Property<T> {

    String name();

    Object valueOf(T subject);
}
