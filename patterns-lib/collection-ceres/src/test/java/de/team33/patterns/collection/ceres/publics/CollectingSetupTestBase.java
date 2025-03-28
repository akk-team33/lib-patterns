package de.team33.patterns.collection.ceres.publics;

import de.team33.patterns.collection.ceres.Collecting;
import de.team33.patterns.collection.ceres.testing.Supply;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

abstract class CollectingSetupTestBase<S extends Collecting.Setup<String, List<String>, S>> {

    private static final Supply SUPPLY = new Supply();
    private static final String[] EMPTY_STRING_ARRAY = {};

    abstract S setup();

    abstract List<String> resultOf(S setup);

    @Test
    final void add_single() {
        final List<String> expected = SUPPLY.anyStringList(3);
        final List<String> result = resultOf(setup().add(expected.get(0))
                                                    .add(expected.get(1))
                                                    .add(expected.get(2)));
        assertEquals(expected, result);
    }

    @Test
    final void add_more() {
        final List<String> original = SUPPLY.anyStringList(9);
        final List<String> expected = new ArrayList<>(original) {{
            addAll(2, Arrays.asList(null, null, null, null, null));
        }};
        final List<String> result = resultOf(setup().add(original.get(0), original.get(1))
                                                    .add(null, null, (String) null)
                                                    .add(null, null, (String[]) null)
                                                    .add(original.get(2), original.get(3), original.get(4))
                                                    .add(original.get(5),
                                                         original.get(6),
                                                         original.get(7),
                                                         original.get(8)));
        assertEquals(expected, result);
    }

    @SuppressWarnings("Convert2MethodRef")
    @Test
    final void addAll() {
        final List<String> expected = SUPPLY.anyStringList(20);
        final Collection<String> head = expected.subList(0, 4);
        final Stream<String> stream = expected.stream().skip(4).limit(4);
        final Iterable<String> iterable1 = expected.subList(8, 11);
        final Iterable<String> iterable2 = () -> expected.subList(11, 14).iterator();
        final Iterator<String> iterator = expected.subList(14, 16).iterator();
        final String[] array = expected.subList(16, 20).toArray(new String[4]);
        final List<String> result = resultOf(setup().addAll((Collection<String>) null)
                                                    .addAll((Stream<String>) null)
                                                    .addAll((Iterable<String>) null)
                                                    .addAll((Iterator<String>) null)
                                                    .addAll((String[]) null)
                                                    .addAll(head)
                                                    .addAll(stream)
                                                    .addAll(iterable1)
                                                    .addAll(iterable2)
                                                    .addAll(iterator)
                                                    .addAll(array));
        assertEquals(expected, result);
    }

    @Test
    final void remove_single() {
        final List<String> original = SUPPLY.anyStringList(20);
        final List<String> expected = new ArrayList<>(original) {{
            removeAll(original.subList(0, 1));
            removeAll(original.subList(3, 4));
            removeAll(original.subList(7, 8));
        }};
        final List<String> result = resultOf(setup().addAll(original)
                                                    .remove(original.get(0))
                                                    .remove(original.get(3))
                                                    .remove(original.get(7)));
        assertEquals(expected, result);
    }

    @Test
    final void remove_more() {
        final List<String> original = SUPPLY.anyStringList(20);
        final List<String> expected = new ArrayList<>(original) {{
            removeAll(original.subList(1, 3));
            removeAll(original.subList(4, 6));
            removeAll(original.subList(9, 10));
            removeAll(original.subList(17, 18));
        }};
        final List<String> result = resultOf(setup().addAll(original)
                                                    .remove(original.get(1), original.get(5))
                                                    .remove(original.get(4), original.get(9), (Object) null)
                                                    .remove(original.get(5), original.get(17), (Object[]) null)
                                                    .remove(original.get(4), original.get(9), original.get(17))
                                                    .remove(original.get(5), original.get(4), original.get(9), original.get(2)));
        assertEquals(expected, result);
    }

    @SuppressWarnings({"Convert2MethodRef", "FunctionalExpressionCanBeFolded"})
    @Test
    final void removeAll() {
        final List<String> original = SUPPLY.anyStringList(36);
        final Collection<String> head = original.subList(0, 5);
        final Stream<String> stream = original.stream().skip(4).limit(6);
        final Iterable<String> iterable1 = original.subList(8, 15);
        final List<String> subList1216 = original.subList(12, 19);
        final Iterable<String> iterable2 = () -> subList1216.iterator();
        final Iterator<String> iterator = subList1216.iterator();
        final String[] array = original.subList(16, 24).toArray(new String[6]);

        final List<String> expected = new ArrayList<>(original) {{
            removeAll(original.subList(0, 24));
        }};
        final List<String> result = resultOf(setup().addAll(original)
                                                    .removeAll(head)
                                                    .removeAll(stream)
                                                    .removeAll(iterable1)
                                                    .removeAll(iterable2)
                                                    .removeAll(iterator)
                                                    .removeAll(array));
        assertEquals(expected, result);
    }

    @Test
    final void removeIf() {
        final List<String> original = SUPPLY.anyStringList(36);
        final Collection<String> removable = original.subList(0, 18);

        final List<String> expected = new ArrayList<>(original) {{
            removeAll(removable);
        }};
        final List<String> result = resultOf(setup().addAll(original)
                                                    .removeIf(removable::contains));
        assertEquals(expected, result);
    }

    @Test
    final void removeAll_null() {
        final List<String> original = SUPPLY.anyStringList(36);
        final List<String> result = resultOf(setup().addAll(original)
                                                    .removeAll((Collection<?>) null)
                                                    .removeAll((Stream<?>) null)
                                                    .removeAll((Iterable<?>) null)
                                                    .removeAll((Iterator<?>) null)
                                                    .removeAll((Object[]) null));
        assertEquals(original, result);
    }

    @Test
    final void retain_single() {
        final List<String> original = SUPPLY.anyStringList(20);
        final List<String> expected = new ArrayList<>(original.subList(0, 1));
        final List<String> result = resultOf(setup().addAll(original)
                                                    .retain(original.get(0)));
        assertEquals(expected, result);
    }

    @Test
    final void retain_more() {
        final List<String> original = SUPPLY.anyStringList(20);
        final List<String> expected = new ArrayList<>(original.subList(7, 10));
        final List<String> result = resultOf(setup().addAll(original)
                                                    .retain(original.get(8), original.get(7),
                                                            original.get(9), SUPPLY.anyString()));
        assertEquals(expected, result);
    }

    @SuppressWarnings({"Convert2MethodRef", "FunctionalExpressionCanBeFolded"})
    @Test
    final void retainAll() {
        final List<String> original = SUPPLY.anyStringList(36);
        final Collection<String> head = original.subList(0, 25);
        final Stream<String> stream = original.stream().skip(10);
        final Iterable<String> iterable1 = original.subList(3, 28);
        final List<String> subList1216 = original.subList(6, 31);
        final Iterable<String> iterable2 = () -> subList1216.iterator();
        final Iterator<String> iterator = subList1216.iterator();
        final String[] array = original.subList(9, 26).toArray(EMPTY_STRING_ARRAY);

        final List<String> expected = new ArrayList<>(original) {{
            retainAll(original.subList(0, 25));
            retainAll(original.subList(10, 36));
            retainAll(original.subList(3, 28));
            retainAll(original.subList(6, 31));
            retainAll(original.subList(9, 26));
        }};
        final List<String> result = resultOf(setup().addAll(original)
                                                    .retainAll(head)
                                                    .retainAll(stream)
                                                    .retainAll(iterable1)
                                                    .retainAll(iterable2)
                                                    .retainAll(iterator)
                                                    .retainAll(array));
        assertEquals(expected, result);
    }

    @Test
    final void retainAll_null() {
        final List<String> original = SUPPLY.anyStringList(36);
        final List<String> expected = Collections.emptyList();
        final List<String> result = resultOf(setup().addAll(original)
                                                    .retainAll((Collection<?>) null)
                                                    .retainAll((Stream<?>) null)
                                                    .retainAll((Iterable<?>) null)
                                                    .retainAll((Iterator<?>) null)
                                                    .retainAll((Object[]) null));
        assertEquals(expected, result);
    }

    @Test
    final void clear() {
        final List<String> expected = Collections.emptyList();
        final List<String> result = resultOf(setup().addAll(SUPPLY.anyStringList(20))
                                                    .clear());
        assertEquals(expected, result);
    }
}
