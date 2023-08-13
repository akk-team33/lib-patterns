package de.team33.test.patterns.properties.methods;

import java.util.AbstractCollection;
import java.util.HashSet;

import static java.util.Collections.singleton;

public class BaseClass implements X1 {

    private static final String CLASS_NAME = BaseClass.class.getCanonicalName();

    private AbstractCollection<String> first = new HashSet<>(singleton(CLASS_NAME + ".getFirst()"));
    private String second = CLASS_NAME;

    @SuppressWarnings("DesignForExtension")
    @Override
    public AbstractCollection<String> getFirst() {
        return first;
    }

    @SuppressWarnings("DesignForExtension")
    public BaseClass setFirst(final AbstractCollection<String> value) {
        first = new HashSet<>(value);
        return this;
    }

    @Override
    public final X1 setFirst(final Object value) {
        //noinspection unchecked
        return setFirst((AbstractCollection<String>) value);
    }

    @SuppressWarnings("DesignForExtension")
    @Override
    public String getSecond() {
        return second;
    }

    @SuppressWarnings("DesignForExtension")
    public void setSecond(final String value) {
        second = value;
    }

    @SuppressWarnings("DesignForExtension")
    public Comparable<?> getThird() {
        return CLASS_NAME;
    }

    @Override
    public final void setThird(final Object value) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    public final void setThird(final Comparable<?> value) {
        throw new UnsupportedOperationException("not yet implemented");
    }
}
