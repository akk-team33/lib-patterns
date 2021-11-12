package de.team33.patterns.random.e1;

import java.util.Random;
import java.util.function.IntUnaryOperator;
import java.util.stream.IntStream;

@FunctionalInterface
interface StringProvider {

    /**
     * Returns a {@link StringProvider}
     */
    static StringProvider using(final IntUnaryOperator operator) {
        return (characters, length) -> IntStream.generate(() -> operator.applyAsInt(characters.length()))
                                                .limit(length)
                                                .collect(StringBuilder::new,
                                                         (sb, index) -> sb.append(characters.charAt(index)),
                                                         StringBuilder::append)
                                                .toString();
    }

    static StringProvider using(final Random random) {
        return using(random::nextInt);
    }

    String any(final String characters, final int length);
}
