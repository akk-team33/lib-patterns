package de.team33.patterns.normal.iocaste;

/**
 * A tool to generate normalized representations of data objects.
 */
public class Normalizer {

    public static Builder builder() {
        return new Builder();
    }

    public final Object normal(final Object origin) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    public static class Builder {

        public final Normalizer build() {
            throw new UnsupportedOperationException("not yet implemented");
        }
    }
}
