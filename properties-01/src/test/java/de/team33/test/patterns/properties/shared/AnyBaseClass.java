package de.team33.test.patterns.properties.shared;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AnyBaseClass {

    private long aLong;
    private BigDecimal aBigDecimal;
    private List<?> aList;

    AnyBaseClass() {
    }

    AnyBaseClass(final Random random) {
        aLong = random.nextLong();
        //noinspection AssignmentToNull
        aBigDecimal = (0 == random.nextInt(16)) ? null : BigDecimal.valueOf(random.nextDouble());
        aList = Stream.of(random.nextInt(),
                          random.nextDouble(),
                          new BigInteger(100, random).toString(Character.MAX_RADIX),
                          new Date(System.currentTimeMillis() + random.nextInt()))
                      .limit(random.nextInt(4))
                      .collect(Collectors.toList());
    }

    AnyBaseClass(final AnyBaseClass origin) {
        aLong = origin.aLong;
        aBigDecimal = origin.aBigDecimal;
        aList = origin.aList;
    }

    public final long getALong() {
        return aLong;
    }

    @SuppressWarnings("DesignForExtension")
    public AnyBaseClass setALong(final long aLong) {
        this.aLong = aLong;
        return this;
    }

    public final BigDecimal getABigDecimal() {
        return aBigDecimal;
    }

    @SuppressWarnings("DesignForExtension")
    public AnyBaseClass setABigDecimal(final BigDecimal aBigDecimal) {
        this.aBigDecimal = aBigDecimal;
        return this;
    }

    public final List<?> getAList() {
        //noinspection ReturnOfNull
        return (null == aList) ? null : Collections.unmodifiableList(aList);
    }

    @SuppressWarnings("DesignForExtension")
    public AnyBaseClass setAList(final List<?> aList) {
        //noinspection AssignmentToNull
        this.aList = (null == aList) ? null : new ArrayList<>(aList);
        return this;
    }
}
