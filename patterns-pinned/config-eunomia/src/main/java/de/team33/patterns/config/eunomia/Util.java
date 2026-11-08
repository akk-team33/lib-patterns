package de.team33.patterns.config.eunomia;

final class Util {

    static IllegalStateException shouldNotHappen() {
        return shouldNotHappen(null);
    }

    static IllegalStateException shouldNotHappen(final Throwable cause) {
        return new IllegalStateException("should not happen at all", cause);
    }
}
