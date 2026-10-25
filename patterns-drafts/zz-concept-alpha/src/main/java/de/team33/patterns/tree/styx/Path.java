package de.team33.patterns.tree.styx;

public class Path<K> {

    public static <K> Path<K> of(final Slice<K>... slices) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    public static <K> Slice<K> slice(final int index) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    public static <K> Slice<K> slice(final K key) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    public static class Slice<K> {

        public final Type type() {
            throw new UnsupportedOperationException("not yet implemented");
        }

        public enum Type {

            KEY,
            INDEX
        }
    }
}
