package de.team33.test.patterns.properties.e1;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AnyBaseClass {

    private long aLong;
    private BigDecimal aBigDecimal;
    private List<?> aList;

    public AnyBaseClass() {
    }

    public AnyBaseClass(final Random random) {
        aLong = random.nextLong();
        aBigDecimal = (0 == random.nextInt(100)) ? null : BigDecimal.valueOf(random.nextDouble());
        aList = Stream.of(random.nextInt(),
                          random.nextDouble(),
                          new BigInteger(100, random).toString(Character.MAX_RADIX),
                          new Date(System.currentTimeMillis() + random.nextInt()))
                      .limit(random.nextInt(4))
                      .collect(Collectors.toList());
    }

    public AnyBaseClass(final AnyBaseClass origin) {
        aLong = origin.aLong;
        aBigDecimal = origin.aBigDecimal;
        aList = origin.aList;
    }

    public long getALong() {
        return aLong;
    }

    public AnyBaseClass setALong(final long aLong) {
        this.aLong = aLong;
        return this;
    }

    public BigDecimal getABigDecimal() {
        return aBigDecimal;
    }

    public AnyBaseClass setABigDecimal(final BigDecimal aBigDecimal) {
        this.aBigDecimal = aBigDecimal;
        return this;
    }

    public List<?> getAList() {
        return (null == aList) ? null : Collections.unmodifiableList(aList);
    }

    public AnyBaseClass setAList(final List<?> aList) {
        this.aList = (null == aList) ? null : new ArrayList<>(aList);
        return this;
    }
}
