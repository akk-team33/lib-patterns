package de.team33.test.patterns.collection.ceres;

import de.team33.patterns.collection.ceres.Collecting;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


class CollectingTest {

    private static final Supply SUPPLY = new Supply();
    private static final String[] EMPTY_STRING_ARRAY = new String[0];
    private static final String[] NULL_STRING_ARRAY = null;

    @Test
    final void add_single() {
        final String element = SUPPLY.anyString();
        final List<String> expected = Collections.singletonList(element);
        final List<String> result = Collecting.add(new LinkedList<>(), element);
        assertEquals(expected, result);
    }

    @Test
    final void add_more() {
        final List<String> expected = SUPPLY.anyStringList(4);
        final List<String> result = Collecting.add(new LinkedList<>(),
                                                   expected.get(0),
                                                   expected.get(1),
                                                   expected.get(2),
                                                   expected.get(3));
        assertEquals(expected, result);
    }

    @Test
    final void add_more_null() {
        final LinkedList<String> subject = new LinkedList<>();
        final String element0 = SUPPLY.anyString();
        final String element1 = SUPPLY.anyString();
        try {
            final List<String> result = Collecting.add(subject, element0, element1, NULL_STRING_ARRAY);
            fail("expected to fail - but was " + result);
        } catch (final NullPointerException ignored) {
            // as expected
        }
    }

    @Test
    final void addAll_Collection() {
        final List<String> expected = SUPPLY.anyStringList(4);
        final List<String> result = Collecting.addAll(new LinkedList<>(), expected);
        assertEquals(expected, result);
    }

    @Test
    final void addAll_Stream() {
        final List<String> expected = SUPPLY.anyStringList(4);
        final List<String> result = Collecting.addAll(new LinkedList<>(), expected.stream());
        assertEquals(expected, result);
    }

    @Test
    final void addAll_array() {
        final List<String> expected = SUPPLY.anyStringList(4);
        final String[] elements = expected.toArray(EMPTY_STRING_ARRAY);
        final List<String> result = Collecting.addAll(new LinkedList<>(), elements);
        assertEquals(expected, result);
    }

    @Test
    final void addAll_Iterable_noCollection() {
        final List<String> expected = SUPPLY.anyStringList(4);
        @SuppressWarnings({"Convert2MethodRef", "FunctionalExpressionCanBeFolded"})
        final Iterable<String> elements = () -> expected.iterator();
        final List<String> result = Collecting.addAll(new LinkedList<>(), elements);
        assertEquals(expected, result);
    }

    @Test
    final void addAll_Iterable_Collection() {
        final Iterable<String> expected = SUPPLY.anyStringList(4);
        final List<String> result = Collecting.addAll(new LinkedList<>(), expected);
        assertEquals(expected, result);
    }

    @Test
    final void clear() {
        final List<String> origin = SUPPLY.anyStringList(4);
        final List<String> result = Collecting.clear(new ArrayList<>(origin));
        assertEquals(Collections.emptyList(), result);
    }

    @ParameterizedTest
    @EnumSource
    final void remove_single(final RemoveCase rmCase) {
        final List<String> origin = SUPPLY.anyStringList(4);
        final Object obsolete = rmCase.obsolete.apply(origin);
        final Set<String> expected = new HashSet<String>(origin) {{
            remove(obsolete);
        }};
        final Set<String> result = Collecting.remove(new TreeSet<>(origin), obsolete);
        assertEquals(expected, result);
        assertThrows(NullPointerException.class, () -> Collecting.remove(null, obsolete));
    }

    @Test
    final void remove_more() {
        final List<String> origin = SUPPLY.anyStringList(8);
        final String[] obsolete = {origin.get(1), origin.get(6), origin.get(3)};
        final List<String> expected = new ArrayList<String>(origin) {{
            removeAll(Arrays.asList(obsolete));
        }};
        final List<String> result = Collecting.remove(new ArrayList<>(origin),
                                                      obsolete[0],
                                                      obsolete[1],
                                                      obsolete[2]);
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @EnumSource
    final void removeAll_Collection(final RemoveCase rmCase) {
        final List<String> origin = SUPPLY.anyStringList(8);
        final List<?> obsolete = rmCase.obsoleteList.apply(origin);
        final Set<String> expected = new HashSet<String>(origin) {{
            //noinspection SlowAbstractSetRemoveAll
            removeAll(obsolete);
        }};
        final Set<String> result = Collecting.removeAll(new TreeSet<>(origin), obsolete);
        assertEquals(expected, result);

        assertThrows(NullPointerException.class, () -> Collecting.removeAll(null, obsolete));

        final TreeSet<Object> subject = new TreeSet<>();
        assertThrows(NullPointerException.class, () -> Collecting.removeAll(subject, (Collection<?>) null));
    }

    @Test
    final void removeAll_Stream() {
        final List<String> origin = SUPPLY.anyStringList(8);
        final List<String> obsolete = Arrays.asList(origin.get(1), origin.get(2), origin.get(5));
        final List<String> expected = new ArrayList<String>(origin) {{
            removeAll(obsolete);
        }};
        final List<String> result = Collecting.removeAll(new ArrayList<>(origin), obsolete.stream());
        assertEquals(expected, result);
    }

    @Test
    final void removeAll_array() {
        final List<String> origin = SUPPLY.anyStringList(8);
        final String[] obsolete = {origin.get(1), origin.get(3), origin.get(4)};
        final List<String> expected = new ArrayList<String>(origin) {{
            removeAll(Arrays.asList(obsolete));
        }};
        final List<String> result = Collecting.removeAll(new ArrayList<>(origin), obsolete);
        assertEquals(expected, result);
    }

    @Test
    final void removeAll_Iterable() {
        final List<String> origin = SUPPLY.anyStringList(8);
        final Iterable<String> obsolete1 = Arrays.asList(origin.get(1), origin.get(3), origin.get(4));
        @SuppressWarnings({"Convert2MethodRef", "FunctionalExpressionCanBeFolded"})
        final Iterable<String> obsolete2 = () -> obsolete1.iterator();
        final List<String> expected = new ArrayList<String>(origin) {{
            obsolete1.forEach(item -> removeAll(Collections.singleton(item)));
        }};
        final List<String> result1 = Collecting.removeAll(new ArrayList<>(origin), obsolete1);
        assertEquals(expected, result1);
        final List<String> result2 = Collecting.removeAll(new ArrayList<>(origin), obsolete2);
        assertEquals(expected, result2);
    }

    @ParameterizedTest
    @EnumSource
    final void removeIf(final RemoveCase rmCase) {
        final List<String> origin = SUPPLY.anyStringList(8);
        final List<?> obsolete = rmCase.obsoleteList.apply(origin);
        final Set<String> expected = new HashSet<String>(origin) {{
            //noinspection SlowAbstractSetRemoveAll
            removeAll(obsolete);
        }};
        final Set<String> result = Collecting.removeIf(new TreeSet<>(origin), obsolete::contains);
        assertEquals(expected, result);

        //noinspection DataFlowIssue
        assertThrows(NullPointerException.class, () -> Collecting.removeIf(null, obsolete::contains));

        final TreeSet<Object> subject = new TreeSet<>();
        assertThrows(NullPointerException.class, () -> Collecting.removeIf(subject, null));
    }

    @ParameterizedTest
    @EnumSource
    final void retainAll_Collection(final RemoveCase rmCase) {
        final List<String> origin = SUPPLY.anyStringList(8);
        final List<?> relevant = rmCase.obsoleteList.apply(origin);
        final Set<String> expected = new TreeSet<String>(origin) {{
            retainAll(relevant);
        }};
        final Set<String> result = Collecting.retainAll(new TreeSet<>(origin), relevant);
        assertEquals(expected, result);

        assertThrows(NullPointerException.class, () -> Collecting.retainAll(null, relevant));

        final TreeSet<Object> subject = new TreeSet<>();
        assertThrows(NullPointerException.class, () -> Collecting.retainAll(subject, (Collection<?>) null));
    }

    @Test
    final void retainAll_Stream() {
        final List<String> origin = SUPPLY.anyStringList(8);
        final List<String> relevant = Arrays.asList(origin.get(1), origin.get(5), origin.get(3));
        final List<String> expected = new ArrayList<String>(origin) {{
            retainAll(relevant);
        }};
        final List<String> result = Collecting.retainAll(new ArrayList<>(origin), relevant.stream());
        assertEquals(expected, result);
    }

    @Test
    final void retainAll_array() {
        final List<String> origin = SUPPLY.anyStringList(8);
        final String[] relevant = {origin.get(1), origin.get(3), origin.get(4)};
        final List<String> expected = new ArrayList<String>(origin) {{
            retainAll(Arrays.asList(relevant));
        }};
        final List<String> result = Collecting.retainAll(new ArrayList<>(origin), relevant);
        assertEquals(expected, result);
    }

    @Test
    final void retainAll_Iterable() {
        final List<String> origin = SUPPLY.anyStringList(8);
        final List<String> relevant0 = Arrays.asList(origin.get(1), origin.get(3), origin.get(4));
        @SuppressWarnings("UnnecessaryLocalVariable")
        final Iterable<String> relevant1 = relevant0;
        @SuppressWarnings({"Convert2MethodRef", "FunctionalExpressionCanBeFolded"})
        final Iterable<String> relevant2 = () -> relevant0.iterator();
        final List<String> expected = new ArrayList<String>(origin) {{
            retainAll(relevant0);
        }};
        final List<String> result1 = Collecting.retainAll(new ArrayList<>(origin), relevant1);
        assertEquals(expected, result1);
        final List<String> result2 = Collecting.retainAll(new ArrayList<>(origin), relevant2);
        assertEquals(expected, result2);
    }

    @Test
    final void contains_single() {
        final List<String> origin = SUPPLY.anyStringList(8);
        final Set<String> subject = new TreeSet<>(origin);
        final String item1 = origin.get(2);
        final String item2 = SUPPLY.anyStringExcluding(subject);
        final String noItem = null;
        final int foreign = SUPPLY.anyInt();
        assertTrue(Collecting.contains(subject, item1));
        assertFalse(Collecting.contains(subject, item2));
        assertFalse(Collecting.contains(subject, noItem));
        assertFalse(Collecting.contains(subject, foreign));

        final String element = SUPPLY.anyString();
        assertThrows(NullPointerException.class, () -> Collecting.contains(null, element));
    }

    @Test
    final void contains_more() {
        final List<String> origin = SUPPLY.anyStringList(8);
        final Set<String> subject = new TreeSet<>(origin);
        final String item1 = origin.get(2);
        final String item2 = SUPPLY.anyStringExcluding(subject);
        final String noItem = null;
        final int foreign = SUPPLY.anyInt();
        assertTrue(Collecting.contains(subject, item1, item1, item1, item1));
        assertFalse(Collecting.contains(subject, item1, item1, item2, item1));
        assertFalse(Collecting.contains(subject, item1, item1, noItem, item1));
        assertFalse(Collecting.contains(subject, item1, item1, foreign, item1));
    }

    @SuppressWarnings({"MismatchedQueryAndUpdateOfCollection"})
    @Test
    final void containsAll_Collection() {
        final List<String> origin = SUPPLY.anyStringList(8);
        final Set<String> subject = new TreeSet<>(origin);
        final List<?> items1 = origin.subList(2, 6);
        final List<?> items2 = Collecting.add(new ArrayList<>(items1), SUPPLY.anyStringExcluding(origin));
        final List<?> items3 = Collecting.add(new ArrayList<>(items1), null);
        final List<?> items4 = Collecting.add(new ArrayList<>(items1), SUPPLY.anyInt());
        assertTrue(Collecting.containsAll(subject, items1));
        assertFalse(Collecting.containsAll(subject, items2));
        assertFalse(Collecting.containsAll(subject, items3));
        assertFalse(Collecting.containsAll(subject, items4));

        final Collection<String> missing = null;
        final Collection<String> empty = new ArrayList<>(0);
        final List<String> elements = SUPPLY.anyStringList(4);
        assertThrows(NullPointerException.class, () -> Collecting.containsAll(missing, elements));
        assertThrows(NullPointerException.class, () -> Collecting.containsAll(missing, empty));
        assertThrows(NullPointerException.class, () -> Collecting.containsAll(elements, missing));
        assertThrows(NullPointerException.class, () -> Collecting.containsAll(empty, missing));
    }

    @SuppressWarnings({"MismatchedQueryAndUpdateOfCollection"})
    @Test
    final void containsAll_array() {
        final List<String> origin = SUPPLY.anyStringList(8);
        final Set<String> subject = new TreeSet<>(origin);
        final List<?> items1 = origin.subList(2, 6);
        final List<?> items2 = Collecting.add(new ArrayList<>(items1), SUPPLY.anyStringExcluding(origin));
        final List<?> items3 = Collecting.add(new ArrayList<>(items1), null);
        final List<?> items4 = Collecting.add(new ArrayList<>(items1), SUPPLY.anyInt());
        assertTrue(Collecting.containsAll(subject, items1.toArray()));
        assertFalse(Collecting.containsAll(subject, items2.toArray()));
        assertFalse(Collecting.containsAll(subject, items3.toArray()));
        assertFalse(Collecting.containsAll(subject, items4.toArray()));
    }

    @SuppressWarnings({"MismatchedQueryAndUpdateOfCollection"})
    @Test
    final void containsAll_Iterable() {
        final List<String> origin = SUPPLY.anyStringList(8);
        final Set<String> subject = new TreeSet<>(origin);
        final List<?> items1 = origin.subList(2, 6);
        final List<?> items2 = Collecting.add(new ArrayList<>(items1), SUPPLY.anyStringExcluding(origin));
        final List<?> items3 = Collecting.add(new ArrayList<>(items1), null);
        final List<?> items4 = Collecting.add(new ArrayList<>(items1), SUPPLY.anyInt());
        assertTrue(Collecting.containsAll(subject, toIterable(items1)));
        assertTrue(Collecting.containsAll(subject, asIterable(items1)));
        assertFalse(Collecting.containsAll(subject, toIterable(items2)));
        assertFalse(Collecting.containsAll(subject, asIterable(items2)));
        assertFalse(Collecting.containsAll(subject, toIterable(items3)));
        assertFalse(Collecting.containsAll(subject, asIterable(items3)));
        assertFalse(Collecting.containsAll(subject, toIterable(items4)));
        assertFalse(Collecting.containsAll(subject, asIterable(items4)));
    }

    private <E> Iterable<E> asIterable(final Collection<E> collection) {
        return collection;
    }

    @SuppressWarnings({"Convert2MethodRef", "FunctionalExpressionCanBeFolded"})
    private <E> Iterable<E> toIterable(final Collection<E> collection) {
        return () -> collection.iterator();
    }

    @Test
    final void proxy_Collection() {
        final Collection<String> origin = SUPPLY.anyStringList(4);
        final Collection<String> proxy = Collecting.proxy(origin);
        assertEquals(origin.size(), proxy.size());
        assertEquals(origin.toString(), proxy.toString());
        assertArrayEquals(origin.toArray(), proxy.toArray());
    }

    @Test
    final void proxy_List() {
        final List<String> origin = SUPPLY.anyStringList(4);
        final List<String> copy = new ArrayList<>(origin);
        final List<String> proxy = Collecting.proxy(origin);
        assertEquals(origin.size(), proxy.size());
        assertEquals(origin.toString(), proxy.toString());
        assertEquals(origin.hashCode(), proxy.hashCode());
        assertEquals(origin.equals(copy), proxy.equals(copy));
        assertEquals(copy.equals(origin), copy.equals(proxy));
    }

    @Test
    final void proxy_Set() {
        final Set<String> origin = SUPPLY.anyStringSet(4);
        final Set<String> copy = new HashSet<>(origin);
        final Set<String> proxy = Collecting.proxy(origin);
        assertEquals(origin.size(), proxy.size());
        assertEquals(origin.toString(), proxy.toString());
        assertEquals(origin.hashCode(), proxy.hashCode());
        assertEquals(origin.equals(copy), proxy.equals(copy));
        assertEquals(copy.equals(origin), copy.equals(proxy));
    }

    @SuppressWarnings("Convert2MethodRef")
    enum RemoveCase {
        PRESENT(origin -> origin.get(2),
                origin -> origin.subList(0, 4)),
        ABSENT(origin -> SUPPLY.anyStringExcluding(origin),
               origin -> Arrays.asList(origin.get(1), SUPPLY.anyStringExcluding(origin), origin.get(3))),
        NULL(origin -> null,
             origin -> Arrays.asList(origin.get(1), null, origin.get(3))),
        FOREIGN(origin -> SUPPLY.anyInt(),
                origin -> Arrays.asList(origin.get(1), SUPPLY.anyInt(), origin.get(3)));

        private final Function<List<String>, Object> obsolete;
        private final Function<List<String>, List<?>> obsoleteList;

        RemoveCase(final Function<List<String>, Object> obsolete, Function<List<String>, List<?>> obsoleteList) {
            this.obsolete = obsolete;
            this.obsoleteList = obsoleteList;
        }
    }
}
