package de.team33.samples.patterns.production.narvi;

import de.team33.patterns.random.tarvos.Charger;
import de.team33.patterns.random.tarvos.Producer;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class XRandom extends Random implements Producer {

    @SuppressWarnings("SpellCheckingInspection")
    private static final String CHARS = "abcdefghijklmnopqrstuvwxyz";

    private final Charger charger = Charger.builder()
                                           .on(boolean.class).apply(this::nextBoolean)
                                           .on(int.class).apply(() -> nextInt(-5, 5))
                                           .on(Long.class).apply(() -> nextLong(-5, 5))
                                           .on(String.class).apply(this::nextString)
                                           .on(List.class).apply(Collections::emptyList)
                                           .on(Template.STRINGS).apply(this::nextStrings)
                                           .on(Template.LONGS).apply(this::nextLongs)
                                           .build();

    public final List<Long> nextLongs() {
        return Stream.generate(this::nextLong)
                     .limit(nextInt(5))
                     .collect(Collectors.toList());
    }

    public final List<String> nextStrings() {
        return Stream.generate(this::nextString)
                     .limit(nextInt(5))
                     .collect(Collectors.toList());
    }

    private String nextString() {
        return nextString(5, CHARS);
    }

    public final Sample nextSample() {
        return charger.initBean(new Sample().setStringList(Template.STRINGS)
                                            .setLongList(Template.LONGS));
    }

    @Override
    public final BigInteger nextBits(final int numBits) {
        return new BigInteger(numBits, this);
    }

    public Buildable nextBuildable() {
        return charger.initBean(Buildable.builder()).build();
    }

    private static final class Template {
        static final List<String> STRINGS = Collections.singletonList("abc");
        static final List<Long> LONGS = Collections.singletonList(6L);
    }
}
