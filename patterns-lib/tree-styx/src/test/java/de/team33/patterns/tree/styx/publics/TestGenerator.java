package de.team33.patterns.tree.styx.publics;

import de.team33.patterns.arbitrary.mimas.Generator;
import de.team33.patterns.collection.ceres.Collecting;
import de.team33.patterns.collection.ceres.Mapping;
import de.team33.patterns.tree.styx.Branch;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Supplier;

public class TestGenerator implements Generator {

    private static final Supplier<Set<Branch<String, Integer>>> NEW_SET = HashSet::new;
    private static final Supplier<Map<String, Branch<String, Integer>>> NEW_MAP = HashMap::new;

    private final Random random = new SecureRandom();

    @Override
    public BigInteger anyBits(final int numBits) {
        return new BigInteger(numBits, random);
    }

    public Collection<Branch<String, Integer>> anyCollection() {
        return Collecting.builder(NEW_SET)
                         .add(anyBranch())
                         .add(anyBranch())
                         .add(anyBranch())
                         .build();
    }

    public Map<String, Branch<String, Integer>> anyMap() {
        return Mapping.builder(NEW_MAP)
                      .put(anyString(), anyBranch())
                      .put(anyString(), anyBranch())
                      .put(anyString(), anyBranch())
                      .build();
    }

    private Branch<String, Integer> anyBranch() {
        final Branch.Type type = anyOf(Branch.Type.class);
        switch (type) {
        case LEAF:
            return Branch.of(anyInt());
        case SEQUENCE:
            return Branch.of(anyCollection());
        case COMPLEX:
            return Branch.of(anyMap());
        default:
            throw new IllegalStateException("unknown type: " + type);
        }
    }
}
