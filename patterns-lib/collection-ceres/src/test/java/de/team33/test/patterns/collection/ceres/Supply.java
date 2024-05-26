package de.team33.test.patterns.collection.ceres;

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
    public final BigInteger nextBits(final int numBits) {
        return new BigInteger(numBits, RANDOM);
    }

    public final int nextLength() {
        return nextInt(24);
    }

    public final String nextString() {
        return nextString(nextLength(), CHARACTERS);
    }

    public final List<String> nextStringList(final int size) {
        return Stream.generate(this::nextString)
                     .limit(size)
                     .collect(Collectors.toList());
    }

    public final Set<String> nextStringSet(final int size) {
        return Stream.generate(this::nextString)
                     .distinct()
                     .limit(size)
                     .collect(Collectors.toSet());
    }

    public final Map<String, String> nextMap(final int size) {
        return Stream.generate(() -> nextStringList(2))
                     .limit(size)
                     .collect(HashMap::new, (map, list) -> map.put(list.get(0), list.get(1)), Map::putAll);
    }

    public final Map<String, List<String>> nextStringListMap(final int size) {
        return Stream.generate(this::nextString)
                     .limit(size)
                     .map(key -> singletonMap(key, nextStringList(nextInt(size))))
                     .flatMap(map -> map.entrySet().stream())
                     .collect(HashMap::new, (map, entry) -> map.put(entry.getKey(), entry.getValue()), Map::putAll);
    }

    public final String nextStringExcluding(final Collection<?> obsolete) {
        final String result = nextString();
        return obsolete.contains(result) ? nextStringExcluding(obsolete) : result;
    }
}
