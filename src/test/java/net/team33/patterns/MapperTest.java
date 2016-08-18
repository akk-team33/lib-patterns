package net.team33.patterns;

import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class MapperTest {

    @Test
    public final void put() {
        final Map<?, ?> subject = Mapper
                .wrap(new HashMap<Integer, Integer>(0))
                .put(0, 0)
                .put(1, 1)
                .put(2, 4)
                .put(3, 9)
                .unwrap();
        Assert.assertEquals(0, subject.get(0));
        Assert.assertEquals(1, subject.get(1));
        Assert.assertEquals(4, subject.get(2));
        Assert.assertEquals(9, subject.get(3));
    }

    @Test
    public final void putAll() {
        final Map<?, ?> subject = Mapper
                .wrap(new TreeMap<Integer, Integer>())
                .putAll(Mapper
                        .wrap(new HashMap<Integer, Integer>(0))
                        .put(0, 0)
                        .put(1, 1)
                        .put(2, 4)
                        .put(3, 9)
                        .unwrap())
                .unwrap();
        Assert.assertEquals(0, subject.get(0));
        Assert.assertEquals(1, subject.get(1));
        Assert.assertEquals(4, subject.get(2));
        Assert.assertEquals(9, subject.get(3));
    }

    @Test
    public final void remove() {
        final Map<?, ?> subject = Mapper
                .wrap(new TreeMap<>(Mapper
                        .wrap(new HashMap<Integer, Integer>(0))
                        .put(0, 0)
                        .put(1, 1)
                        .put(2, 4)
                        .put(3, 9)
                        .unwrap()))
                .remove(1)
                .remove(3)
                .unwrap();
        Assert.assertEquals(0, subject.get(0));
        Assert.assertNull(subject.get(1));
        Assert.assertEquals(4, subject.get(2));
        Assert.assertNull(subject.get(3));
    }

    @Test
    public final void clear() {
        final Map<?, ?> subject = Mapper
                .wrap(new TreeMap<>(Mapper
                        .wrap(new HashMap<Integer, Integer>(0))
                        .put(0, 0)
                        .put(1, 1)
                        .put(2, 4)
                        .put(3, 9)
                        .unwrap()))
                .clear()
                .unwrap();
        Assert.assertEquals(Collections.emptyMap(), subject);
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void unmodifiable() {
        Mapper
                .wrap(new HashMap<Integer, Integer>(0))
                .put(0, 0)
                .put(1, 1)
                .put(2, 4)
                .put(3, 9)
                .unmodifiable()
                .put(4, 16);
    }
}