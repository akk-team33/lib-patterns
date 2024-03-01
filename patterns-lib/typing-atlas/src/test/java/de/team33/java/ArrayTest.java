package de.team33.java;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class ArrayTest {

    @Test
    final void primitive() {
        assertSame(int.class, int[].class.getComponentType());
        assertSame(double.class, double[].class.getComponentType());
    }

    @Test
    final void simple() {
        assertSame(String.class, String[].class.getComponentType());
        assertSame(Date.class, Date[].class.getComponentType());
    }

    @Test
    final void generic() {
        final List<String>[] sample = array(asList("abc", "def", "ghi"),
                                            asList("jkl", "mno", "pqr"),
                                            asList("stu", "vwx", "yz"));
        assertSame(List.class, sample.getClass().getComponentType());
    }

    @Test
    final void generic_2() throws NoSuchFieldException {
        final Base<Integer, List<Integer>, Map<Integer, List<Integer>>> sample = new Base<>(
                array(1, 2, 3),
                array(asList(1,2,3), asList(4,5,6), asList(7,8,9)),
                array(singletonMap(1, asList(2,3,4)),
                      singletonMap(5, asList(6, 7, 8)),
                      singletonMap(9, asList(10,11,12))));
        assertEquals(Object[].class, sample.getClass().getField("tArray").getType());
        assertEquals(Integer[].class, sample.tArray.getClass());
    }

    @SafeVarargs
    static <T> T[] array(final T... values) {
        return Arrays.copyOf(values, values.length);
    }

    public static class Base<T, U, V> {

        public final T[] tArray;
        public final U[] uArray;
        public final V[] vArray;

        Base(final T[] tArray, final U[] uArray, final V[] vArray) {
            this.tArray = tArray;
            this.uArray = uArray;
            this.vArray = vArray;
        }
    }

    public static class Qualified extends Base<String, List<String>, Map<String, List<String>>> {

        Qualified(String[] tArray, List<String>[] uArray, Map<String, List<String>>[] vArray) {
            super(tArray, uArray, vArray);
        }
    }
}
