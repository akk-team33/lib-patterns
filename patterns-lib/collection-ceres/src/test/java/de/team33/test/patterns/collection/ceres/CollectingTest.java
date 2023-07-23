package de.team33.test.patterns.collection.ceres;

import de.team33.patterns.collection.ceres.Collecting;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
class CollectingTest {

    private static final Supply SUPPLY = new Supply();

    private static final String STRAIGHT_NOT_FAILED = "straight call did not fail -> test is not significant";
    private static final String NULL_ELEMENT = null;

    @SuppressWarnings("MultipleVariablesInDeclaration")
    private String sample1, sample2, sample3;
    @SuppressWarnings("MultipleVariablesInDeclaration")
    private List<String> samples, duplicated, samplesAndNull;
    private Object noString;
    private List<Object> samplesAndIncompatible;

    @BeforeEach
    public final void before() {
        sample1 = SUPPLY.nextString();
        sample2 = SUPPLY.nextString();
        sample3 = SUPPLY.nextString();
        samples = asList(sample1, sample2, sample3);
        samplesAndNull = asList(sample1, NULL_ELEMENT, sample2, NULL_ELEMENT, sample3);
        noString = SUPPLY.nextInt();
        samplesAndIncompatible = asList(sample1, sample2, noString, sample3);
        duplicated = asList(sample1, sample2, sample3, sample1, sample3, sample2);
    }

    @SuppressWarnings({"rawtypes", "unchecked", "UnnecessaryLocalVariable"})
    @Test
    public final void add_single() {
        final Set<String> subject = new TreeSet<>();
        assertEquals(singleton(sample1), Collecting.add(subject, sample1));
        assertThrows(NullPointerException.class, () -> Collecting.add(subject, null));
        final Set rawSubject = subject;
        assertThrows(ClassCastException.class, () -> Collecting.add(rawSubject, SUPPLY.nextInt()));
    }

    @SuppressWarnings({"rawtypes", "unchecked", "UnnecessaryLocalVariable"})
    @Test
    public final void add_more() {
        final Set<String> subject = new TreeSet<>();
        assertEquals(new HashSet<>(samples), Collecting.add(subject, sample1, sample2, sample3));
        assertThrows(NullPointerException.class, () -> Collecting.add(subject, sample1, null, sample3));
        final Set rawSubject = subject;
        assertThrows(ClassCastException.class, () -> Collecting.add(rawSubject, sample1, SUPPLY.nextInt(), sample3));
    }

    @SuppressWarnings({"rawtypes", "unchecked", "UnnecessaryLocalVariable"})
    @Test
    public final void addAll_Collection() {
        final Set<String> subject = new TreeSet<>();
        assertEquals(new HashSet<>(samples), Collecting.addAll(subject, samples));
        assertThrows(NullPointerException.class, () -> Collecting.addAll(subject, samplesAndNull));
        final Set rawSubject = subject;
        assertThrows(ClassCastException.class, () -> Collecting.addAll(rawSubject, samplesAndIncompatible));
    }

    @SuppressWarnings({"rawtypes", "unchecked", "UnnecessaryLocalVariable"})
    @Test
    public final void addAll_Stream() {
        final Set<String> subject = new TreeSet<>();
        assertEquals(new HashSet<>(samples), Collecting.addAll(subject, samples.stream()));
        assertThrows(NullPointerException.class, () -> Collecting.addAll(subject, samplesAndNull.stream()));
        final Set rawSubject = subject;
        assertThrows(ClassCastException.class, () -> Collecting.addAll(rawSubject, samplesAndIncompatible.stream()));
    }

    @Test
    public final void addAll_Iterable() {
        final Iterable<String> other1 = () -> samplesAndNull.iterator();
        assertEquals(samplesAndNull, Collecting.addAll(new LinkedList<>(), other1)); // not a collection
        final Iterable<String> other2 = samplesAndNull;
        assertEquals(samplesAndNull, Collecting.addAll(new ArrayList<>(), other2)); // is a collection
    }

    @Test
    public final void addAll_array() {
        final String[] other = samples.stream().toArray(String[]::new);
        assertEquals(samples, Collecting.addAll(new ArrayList<>(), other));
    }

    @Test
    public final void clear() {
        assertTrue(Collecting.clear(new ArrayList<>(samples)).isEmpty());
    }

    @Test
    public final void remove_single() {
        final LinkedList<String> subject = new LinkedList<>(duplicated);
        assertTrue(subject.contains(sample2));
        assertFalse(Collecting.remove(subject, sample2).contains(sample2));

        assertEquals(new HashSet<>(samples), Collecting.remove(new TreeSet<>(samples), null));
        assertThrows(NullPointerException.class, () -> Collecting.remove(null, sample1));

        assertEquals(new HashSet<>(samples), Collecting.remove(new TreeSet<>(samples), noString));
    }

    @Test
    public final void remove_more() {
        final LinkedList<String> subject = new LinkedList<>(duplicated);
        assertEquals(emptyList(), Collecting.remove(subject, sample1, sample2, sample3));

        assertEquals(new HashSet<>(samples), Collecting.remove(new TreeSet<>(samples), null));
        assertThrows(NullPointerException.class, () -> Collecting.remove(null, sample1));

        assertEquals(new HashSet<>(samples), Collecting.remove(new TreeSet<>(samples), noString));
    }

    @Test
    public final void removeNull() {
        final TreeSet<String> subject = new TreeSet<>(samples);
        try {
            subject.remove(null);
            fail(STRAIGHT_NOT_FAILED);
        } catch (final NullPointerException ignored) {
            assertFalse(
                    Collecting.contains(
                            Collecting.remove(subject, NULL_ELEMENT),
                            NULL_ELEMENT
                    )
            );
        }
    }

    @Test
    public final void removeIncompatibleType() {
        final TreeSet<String> subject = new TreeSet<>(samples);
        try {
            //noinspection SuspiciousMethodCalls
            subject.remove(noString);
            fail(STRAIGHT_NOT_FAILED);
        } catch (final ClassCastException ignored) {
            assertFalse(
                    Collecting.contains(
                            Collecting.remove(subject, noString),
                            noString
                    )
            );
        }
    }

    @Test
    public final void removeArray() {
        assertTrue(Collecting.remove(new ArrayList<>(duplicated), sample1, sample3, sample2).isEmpty());
    }

    @Test
    public final void testRemoveAll() {
        assertTrue(Collecting.removeAll(new ArrayList<>(duplicated), samples).isEmpty());
    }

    @Test
    public final void removeAllNull() {
        final Collection<String> treeSet = new TreeSet<>(samples);
        try {
            boolean result = new ArrayList<>(samplesAndNull).removeAll(treeSet);
            fail("expected to fail - but was " + result);
        } catch (final NullPointerException ignored) {
            assertEquals(
                    asList(NULL_ELEMENT, NULL_ELEMENT),
                    Collecting.removeAll(
                            new ArrayList<>(samplesAndNull),
                            treeSet)
            );
        }
    }

    @Test
    public final void removeAllIncompatibleType() {
        final Collection<String> treeSet = new TreeSet<>(samples);
        try {
            new ArrayList<>(samplesAndIncompatible).removeAll(treeSet);
            fail(STRAIGHT_NOT_FAILED);
        } catch (final ClassCastException ignored) {
            assertEquals(
                    singletonList(noString),
                    Collecting.removeAll(
                            new ArrayList<>(samplesAndIncompatible),
                            treeSet)
            );
        }
    }

    @Test
    public final void retainArray() {
        assertEquals(
                asList(sample1, sample2, sample1, sample2),
                Collecting.retainAll(new ArrayList<>(duplicated), sample1, sample2));
    }

    @Test
    public final void retainAll() {
        assertEquals(
                asList(sample1, sample1),
                Collecting.retainAll(new ArrayList<>(duplicated), singleton(sample1)));
    }

    @Test
    public final void retainAllNull() {
        final Collection<String> treeSet = new TreeSet<>(samples);
        try {
            new ArrayList<>(samplesAndNull).retainAll(treeSet);
            fail(STRAIGHT_NOT_FAILED);
        } catch (final NullPointerException ignored) {
            assertEquals(
                    samples,
                    Collecting.retainAll(
                            new ArrayList<>(samplesAndNull),
                            treeSet)
            );
        }
    }

    @Test
    public final void retainAllIncompatibleType() {
        final Collection<String> treeSet = new TreeSet<>(samples);
        try {
            new ArrayList<>(samplesAndIncompatible).retainAll(treeSet);
            fail(STRAIGHT_NOT_FAILED);
        } catch (final ClassCastException ignored) {
            assertEquals(
                    samples,
                    Collecting.retainAll(
                            new ArrayList<>(samplesAndIncompatible),
                            treeSet)
            );
        }
    }

    @Test
    public final void contains() {
        assertTrue(Collecting.contains(samples, sample1));
        assertTrue(Collecting.contains(samples, sample2));
        assertTrue(Collecting.contains(samples, sample3));

        assertFalse(Collecting.contains(samples, SUPPLY.nextString()));
        assertFalse(Collecting.contains(samples, noString));
        assertFalse(Collecting.contains(samples, NULL_ELEMENT));

        final Collection<String> treeSet = new TreeSet<>(samples);
        try {
            treeSet.contains(NULL_ELEMENT);
            fail(STRAIGHT_NOT_FAILED);
        } catch (final NullPointerException ignored) {
            assertFalse(Collecting.contains(treeSet, NULL_ELEMENT));
        }
        try {
            //noinspection SuspiciousMethodCalls
            treeSet.contains(noString);
            fail(STRAIGHT_NOT_FAILED);
        } catch (final ClassCastException ignored) {
            assertFalse(Collecting.contains(treeSet, noString));
        }
    }

    @Test
    public final void containsArray() {
        assertTrue(Collecting.contains(samples, sample1, sample2, sample3));
        assertFalse(Collecting.contains(samples, SUPPLY.nextString(), noString, NULL_ELEMENT));
    }

    @Test
    public final void testContainsAll() {
        assertTrue(Collecting.containsAll(samplesAndNull, samples));
        assertFalse(Collecting.containsAll(samples, samplesAndIncompatible));

        final Collection<String> treeSet = new TreeSet<>(samples);
        try {
            treeSet.containsAll(samplesAndNull);
            fail(STRAIGHT_NOT_FAILED);
        } catch (final NullPointerException ignored) {
            assertFalse(Collecting.containsAll(treeSet, samplesAndNull));
        }
        try {
            treeSet.containsAll(samplesAndIncompatible);
            fail(STRAIGHT_NOT_FAILED);
        } catch (final ClassCastException ignored) {
            assertFalse(Collecting.contains(treeSet, samplesAndIncompatible));
        }
    }
}
