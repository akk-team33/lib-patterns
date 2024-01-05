package de.team33.patterns.normal.iocaste;

import java.lang.reflect.Array;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class NormalArray {

    static List<Byte> of(final byte[] bytes) {
        final Object result = Array.newInstance(Byte.class, Array.getLength(bytes));
        IntStream.range(0, Array.getLength(bytes))
                        .forEach(index -> Array.set(result, index, Array.get(bytes, index) ));
    }
}
