package de.team33.patterns.tree.styx;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Branch<K, V> {

    private final Type type;
    private final Object backing;

    private Branch(final Type type, final Object backing) {
        this.type = type;
        this.backing = backing;
    }

    public static <K, V> Branch<K, V> of(final V value) {
        return new Branch<>(Type.LEAF, value);
    }

    public static <K, V> Branch<K, V> of(final Collection<? extends Branch<K, V>> origin) {
        final List<Branch<K, V>> backing = new ArrayList<>(origin);
        return new Branch<>(Type.SEQUENCE, backing);
    }

    public static <K, V> Branch<K, V> of(final Map<? extends K, ? extends Branch<K, V>> origin) {
        final Map<K, Branch<K, V>> backing = new HashMap<>(origin);
        return new Branch<>(Type.COMPLEX, backing);
    }

    public final Type type() {
        return type;
    }

    public final boolean isLeaf() {
        return Type.LEAF == type();
    }

    public final boolean isSequence() {
        return Type.SEQUENCE == type();
    }

    public final boolean isComplex() {
        return Type.COMPLEX == type();
    }

    public final V asLeaf() {
        return type().asLeaf(this);
    }

    public final List<Branch<K, V>> asSequence() {
        return type().asSequence(this);
    }

    public final Map<K, Branch<K, V>> asComplex() {
        return type().asComplex(this);
    }

    public enum Type {

        LEAF {
            @SuppressWarnings("unchecked")
            @Override
            <V> V asLeaf(final Branch<?, V> branch) {
                return (V) branch.backing;
            }
        },

        SEQUENCE {
            @SuppressWarnings("unchecked")
            @Override
            <K, V> List<Branch<K, V>> asSequence(final Branch<K, V> branch) {
                return (List<Branch<K, V>>) branch.backing;
            }
        },

        COMPLEX {
            @SuppressWarnings("unchecked")
            @Override
            <K, V> Map<K, Branch<K, V>> asComplex(final Branch<K, V> branch) {
                return (Map<K, Branch<K, V>>) branch.backing;
            }
        };

        <V> V asLeaf(final Branch<?, V> branch) {
            throw new UnsupportedOperationException("branch is not a leaf: " + branch);
        }

        <K, V> List<Branch<K, V>> asSequence(final Branch<K, V> branch) {
            throw new UnsupportedOperationException("branch is not a sequence: " + branch);
        }

        <K, V> Map<K, Branch<K, V>> asComplex(final Branch<K, V> branch) {
            throw new UnsupportedOperationException("branch is not a complex: " + branch);
        }
    }
}
