package net.team33.patterns;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class CollectorTest {

    private static final List<Integer> INTEGERS = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

    @Test
    public final void add() {
        final List<Integer> subject = Collector.wrap(new ArrayList<Integer>(0))
                .add(1)
                .add(2, 3)
                .add(4, 5, 6)
                .add(7, 8, 9, 10)
                .unwrap();
        Assert.assertEquals(INTEGERS, subject);
    }

    @Test
    public final void addAll() {
        final List<Integer> subject = Collector.wrap(new ArrayList<Integer>(0))
                .addAll(Arrays.asList(1, 2, 3))
                .addAll(new Integers(4, 5, 6))
                .addAll(Arrays.asList(7, 8, 9, 10).iterator())
                .unwrap();
        Assert.assertEquals(INTEGERS, subject);
    }

    @Test
    public final void remove() {
        final List<Integer> subject = Collector.wrap(new ArrayList<>(INTEGERS)).addAll(INTEGERS)
                .remove(1)
                .remove(2, 3)
                .remove(4, 5, 6)
                .remove(7, 8, 9, 10)
                .unwrap();
        Assert.assertEquals(Collections.emptyList(), subject);
    }

    @Test
    public final void removeAll() {
        final List<Integer> subject = Collector.wrap(new ArrayList<>(INTEGERS)).addAll(INTEGERS)
                .removeAll(Arrays.asList(1, 2, 3, 4, 5))
                .removeAll(new Integers(4, 5, 6, 7, 8))
                .removeAll(Arrays.asList(7, 8, 9, 10, 11).iterator())
                .unwrap();
        Assert.assertEquals(Collections.emptyList(), subject);
    }

    @Test
    public final void retain() {
        final List<Integer> subject = Collector.wrap(new ArrayList<>(INTEGERS)).addAll(INTEGERS)
                .retain(1, 2, 3, 4, 5)
                .retain(4, 5, 6, 7, 8)
                .retain(7, 8, 9, 10, 11)
                .unwrap();
        Assert.assertEquals(Collections.emptyList(), subject);
    }

    @Test
    public final void retainAll() {
        final List<Integer> subject = Collector.wrap(new ArrayList<>(INTEGERS)).addAll(INTEGERS)
                .retainAll(Arrays.asList(1, 2, 3, 4, 5))
                .retainAll(new Integers(4, 5, 6, 7, 8))
                .retainAll(Arrays.asList(7, 8, 9, 10, 11).iterator())
                .unwrap();
        Assert.assertEquals(Collections.emptyList(), subject);
    }

    @Test
    public final void clear() {
        final List<Integer> subject = Collector.wrap(new ArrayList<>(INTEGERS)).addAll(INTEGERS)
                .clear()
                .unwrap();
        Assert.assertEquals(Collections.emptyList(), subject);
    }

    private static class Integers implements Iterable<Integer> {
        private final Collection<Integer> core;

        private Integers(final Integer... values) {
            core = Arrays.asList(values);
        }

        @Override
        public final Iterator<Integer> iterator() {
            return core.iterator();
        }
    }
}