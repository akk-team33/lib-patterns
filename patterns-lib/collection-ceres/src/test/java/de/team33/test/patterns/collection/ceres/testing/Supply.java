package de.team33.test.patterns.collection.ceres.testing;

import de.team33.patterns.arbitrary.mimas.Generator;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.singletonMap;

public class Supply implements Generator {

    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz-ABCDEFGHIJKLMNOPQRSTUVWXYZ_0123456789";
    private static final Random RANDOM = new SecureRandom();

    @Override
    public final BigInteger anyBits(final int numBits) {
        return new BigInteger(numBits, RANDOM);
    }

    public final int anyLength() {
        return anyInt(24);
    }

    public final String anyString() {
        return anyString(anyLength(), CHARACTERS);
    }

    public final List<String> anyStringList(final int size) {
        return Stream.generate(this::anyString)
                     .limit(size)
                     .collect(Collectors.toList());
    }

    public final Set<String> anyStringSet(final int size) {
        return Stream.generate(this::anyString)
                     .distinct()
                     .limit(size)
                     .collect(Collectors.toSet());
    }

    public final Map<String, String> anyMap(final int size) {
        return Stream.generate(() -> anyStringList(2))
                     .limit(size)
                     .collect(HashMap::new, (map, list) -> map.put(list.get(0), list.get(1)), Map::putAll);
    }

    public final Map<String, List<String>> anyStringListMap(final int size) {
        return Stream.generate(this::anyString)
                     .limit(size)
                     .map(key -> singletonMap(key, anyStringList(anyInt(size))))
                     .flatMap(map -> map.entrySet().stream())
                     .collect(HashMap::new, (map, entry) -> map.put(entry.getKey(), entry.getValue()), Map::putAll);
    }

    public final String anyStringExcluding(final Collection<?> obsolete) {
        final String result = anyString();
        return obsolete.contains(result) ? anyStringExcluding(obsolete) : result;
    }
}
