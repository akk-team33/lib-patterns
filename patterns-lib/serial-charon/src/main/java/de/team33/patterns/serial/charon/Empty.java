package de.team33.patterns.serial.charon;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

final class Empty<E> extends Series<E> {

    private Empty() {
    }

    @SuppressWarnings("rawtypes")
    static final Empty INSTANCE = new Empty();

    @Override
    public final E head() {
        throw new NoSuchElementException("this Series is empty");
    }

    @Override
    public final Series<E> tail() {
        //noinspection unchecked
        return INSTANCE;
    }

    @Override
    public final int size() {
        return 0;
    }

    @Override
    public final List<E> asList() {
        //noinspection unchecked,CollectionsFieldAccessReplaceableByMethodCall,AssignmentOrReturnOfFieldWithMutableType
        return Collections.EMPTY_LIST;
    }
}
