package de.team33.patterns.config.eunomia;

import de.team33.patterns.io.thalassa.IO;

final class DefaultIO<T> implements IO<T> {

    private T value = null;

    @Override
    public final T read() {
        return value;
    }

    @Override
    public final void write(final T value) {
        this.value = value;
    }
}
