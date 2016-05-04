package net.team33.patterns;

import org.junit.Test;

import java.util.TreeMap;

public class SortedMapperTest {

    @Test(expected = UnsupportedOperationException.class)
    public final void unmodifiable() {
        SortedMapper
                .map(new TreeMap<Integer, Integer>())
                .put(0, 0)
                .put(1, 1)
                .put(2, 4)
                .put(3, 9)
                .unmodifiable()
                .put(4, 16);
    }
}