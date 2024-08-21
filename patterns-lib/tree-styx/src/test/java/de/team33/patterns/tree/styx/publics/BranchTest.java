package de.team33.patterns.tree.styx.publics;

import de.team33.patterns.tree.styx.Branch;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class BranchTest {

    private static final TestGenerator GEN = new TestGenerator();

    @ParameterizedTest
    @EnumSource
    void type(final TypeCase testCase) {
        final Branch<String, Integer> branch = testCase.newBranch.get();
        assertEquals(testCase.expectedType, branch.type());
    }

    @Test
    void isLeaf() {
    }

    @Test
    void isSequence() {
    }

    @Test
    void isComplex() {
    }

    @Test
    void asLeaf() {
    }

    @Test
    void asSequence() {
    }

    @Test
    void asComplex() {
    }

    enum TypeCase {
        LEAF(() -> Branch.of(GEN.anyInt()), Branch.Type.LEAF),
        SEQUENCE(() -> Branch.of(GEN.anyCollection()), Branch.Type.SEQUENCE),
        COMPLEX(() -> Branch.of(GEN.anyMap()), Branch.Type.COMPLEX);

        final Supplier<Branch<String, Integer>> newBranch;
        final Branch.Type expectedType;

        TypeCase(final Supplier<Branch<String, Integer>> newBranch, final Branch.Type expectedType) {
            this.newBranch = newBranch;
            this.expectedType = expectedType;
        }
    }
}