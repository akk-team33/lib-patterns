package de.team33.test.patterns.collection.ceres;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.TreeSet;
import java.util.Vector;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SuperfluousExceptionsTrial {

    private static final Supply SUPPLY = new Supply();

    @ParameterizedTest
    @EnumSource
    final void contains_null(final ContainsCase testCase) {
        final Collection<String> subject = testCase.toSubject(SUPPLY.nextStringList(16));
        try {
            final boolean result = subject.contains(null);
            assertFalse(testCase.fail,  () -> "expected to fail: " + testCase.subjectClass);
            assertFalse(result);
        } catch (final NullPointerException e) {
            // e.printStackTrace();
            assertTrue(testCase.fail, "failed unexpectedly: " + testCase.subjectClass);
        }
    }

    @ParameterizedTest
    @EnumSource
    final void contains_foreign(final ContainsCase testCase) {
        final Collection<String> subject = testCase.toSubject(SUPPLY.nextStringList(16));
        try {
            final boolean result = subject.contains(SUPPLY.nextInt());
            assertFalse(testCase.fail, () -> "expected to fail: " + testCase.subjectClass);
            assertFalse(result);
        } catch (final ClassCastException e) {
            // e.printStackTrace();
            assertTrue(testCase.fail, "failed unexpectedly: " + testCase.subjectClass);
        }
    }

    @ParameterizedTest
    @EnumSource
    final void containsAll_null(final ContainsCase testCase) {
        final Collection<String> subject = testCase.toSubject(SUPPLY.nextStringList(16));
        final Collection<Object> other = Collections.singleton(null);
        try {
            final boolean result = subject.containsAll(other);
            assertFalse(testCase.fail,  () -> "expected to fail: " + testCase.subjectClass);
            assertFalse(result);
        } catch (final NullPointerException e) {
            // e.printStackTrace();
            assertTrue(testCase.fail, "failed unexpectedly: " + testCase.subjectClass);
        }
    }

    @ParameterizedTest
    @EnumSource
    final void containsAll_foreign(final ContainsCase testCase) {
        final Collection<String> subject = testCase.toSubject(SUPPLY.nextStringList(16));
        final Collection<Object> other = Collections.singleton(SUPPLY.nextInt());
        try {
            final boolean result = subject.containsAll(other);
            assertFalse(testCase.fail,  () -> "expected to fail: " + testCase.subjectClass);
            assertFalse(result);
        } catch (final ClassCastException e) {
            // e.printStackTrace();
            assertTrue(testCase.fail, "failed unexpectedly: " + testCase.subjectClass);
        }
    }

    @ParameterizedTest
    @EnumSource
    final void remove_null(final ContainsCase testCase) {
        final Collection<String> subject = testCase.toSubject(SUPPLY.nextStringList(16));
        try {
            final boolean result = subject.remove(null);
            assertFalse(testCase.fail,  () -> "expected to fail: " + testCase.subjectClass);
            assertFalse(result);
        } catch (final NullPointerException e) {
            // e.printStackTrace();
            assertTrue(testCase.fail, "failed unexpectedly: " + testCase.subjectClass);
        }
    }

    @ParameterizedTest
    @EnumSource
    final void remove_foreign(final ContainsCase testCase) {
        final Collection<String> subject = testCase.toSubject(SUPPLY.nextStringList(16));
        try {
            final boolean result = subject.remove(SUPPLY.nextInt());
            assertFalse(testCase.fail,  () -> "expected to fail: " + testCase.subjectClass);
            assertFalse(result);
        } catch (final ClassCastException e) {
            // e.printStackTrace();
            assertTrue(testCase.fail, "failed unexpectedly: " + testCase.subjectClass);
        }
    }

    @ParameterizedTest
    @EnumSource
    final void removeAll_null(final ContainsCase testCase) {
        final Collection<String> subject = testCase.toSubject(SUPPLY.nextStringList(16));
        final Collection<Object> other = Collections.singleton(null);
        try {
            final boolean result = subject.removeAll(other);
            assertFalse(testCase.fail,  () -> "expected to fail: " + testCase.subjectClass);
            assertFalse(result);
        } catch (final NullPointerException e) {
            // e.printStackTrace();
            assertTrue(testCase.fail, "failed unexpectedly: " + testCase.subjectClass);
        }
    }

    @ParameterizedTest
    @EnumSource
    final void removeAll_foreign(final ContainsCase testCase) {
        final Collection<String> subject = testCase.toSubject(SUPPLY.nextStringList(16));
        final Collection<Object> other = Collections.singleton(SUPPLY.nextInt());
        try {
            final boolean result = subject.removeAll(other);
            assertFalse(testCase.fail,  () -> "expected to fail: " + testCase.subjectClass);
            assertFalse(result);
        } catch (final ClassCastException e) {
            // e.printStackTrace();
            assertTrue(testCase.fail, "failed unexpectedly: " + testCase.subjectClass);
        }
    }

    @ParameterizedTest
    @EnumSource
    final void retainAll_null(final ContainsCase testCase) {
        final Collection<Object> subject = new HashSet<>(Collections.singleton(null));
        final Collection<String> other = testCase.toSubject(SUPPLY.nextStringList(16));
        try {
            final boolean result = subject.retainAll(other);
            assertFalse(testCase.fail, () -> "expected to fail: " + testCase.subjectClass);
            assertTrue(result);
        } catch (final NullPointerException e) {
            // e.printStackTrace();
            assertTrue(testCase.fail, "failed unexpectedly: " + testCase.subjectClass);
        }
    }

    @ParameterizedTest
    @EnumSource
    final void retainAll_foreign(final ContainsCase testCase) {
        final Collection<Object> subject = new HashSet<>(Collections.singleton(SUPPLY.nextInt()));
        final Collection<String> other = testCase.toSubject(SUPPLY.nextStringList(16));
        try {
            final boolean result = subject.retainAll(other);
            assertFalse(testCase.fail,  () -> "expected to fail: " + testCase.subjectClass);
            assertTrue(result);
        } catch (final ClassCastException e) {
            // e.printStackTrace();
            assertTrue(testCase.fail, "failed unexpectedly: " + testCase.subjectClass);
        }
    }

    enum ContainsCase {

        ARRAY_LIST(ArrayList.class, ArrayList::new, false),
        LINKED_LIST(LinkedList.class, LinkedList::new, false),
        VECTOR(Vector.class, Vector::new, false),
        HASH_SET(HashSet.class, HashSet::new, false),
        TREE_SET(TreeSet.class, TreeSet::new, true),
        LINKED_HASH_SET(LinkedHashSet.class, LinkedHashSet::new, false);

        private final Class<?> subjectClass;
        @SuppressWarnings("rawtypes")
        private final Function<Collection, Collection> toSubject;
        private final boolean fail;

        @SuppressWarnings("rawtypes")
        ContainsCase(final Class<?> subjectClass,
                     final Function<Collection, Collection> toSubject,
                     final boolean fail) {
            this.subjectClass = subjectClass;
            this.toSubject = toSubject;
            this.fail = fail;
        }

        @SuppressWarnings("unchecked")
        public <E> Collection<E> toSubject(final Collection<E> origin) {
            return toSubject.apply(origin);
        }
    }
}
