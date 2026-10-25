package de.team33.patterns.typing.proteus.sample;

import java.util.List;
import java.util.Map;

public class TypeTestSubject<T extends CharSequence> {

    String string;

    int primitive;

    String[] array;

    T variable;

    T[] variableArray;

    List<T> parameterized;

    Map<String, T> parameterizedMultiple;

    List<?> wildcard;

    List<? extends T> upperBounded;

    List<? super T> lowerBounded;

    List<List<T>> nested;

    Map<String, List<? extends T>> nestedWildcard;
}
