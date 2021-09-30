package de.team33.test.patterns.properties.shared;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import static java.lang.System.identityHashCode;
import static java.lang.System.nanoTime;

public class AnyClass extends AnyBaseClass {

    private int anInt;
    private Double aDouble;
    private String aString;
    private Date aDate;

    private transient List<Object> aTransient = new LinkedList<>(Arrays.asList(identityHashCode(this), nanoTime()));

    public AnyClass() {
    }

    static List<Object> toList(final AnyClass any) {
        return Arrays.asList(AnyBaseClass.toList(any), any.anInt, any.aDouble, any.aString, any.aDate);
    }

    public AnyClass(final Random random) {
        super((random));
        anInt = random.nextInt();
        aDouble = nullable(random, random::nextDouble);
        aString = nullable(random, () -> new BigInteger(8 + random.nextInt(128), random).toString(Character.MAX_RADIX));
        aDate = nullable(random, () -> new Date(System.currentTimeMillis() + random.nextInt()));
    }

    public AnyClass(final AnyClass origin, final boolean deep) {
        super(origin, deep);
        anInt = origin.anInt;
        aDouble = origin.aDouble;
        aString = origin.aString;
        setADate(origin.getADate());
    }

    @Override
    public final AnyClass setALong(final long aLong) {
        return (AnyClass) super.setALong(aLong);
    }

    @Override
    public final AnyClass setABigDecimal(final BigDecimal aBigDecimal) {
        return (AnyClass) super.setABigDecimal(aBigDecimal);
    }

    @Override
    public final AnyClass setAList(final List<?> aList) {
        return (AnyClass) super.setAList(aList);
    }

    public final int getAnInt() {
        return anInt;
    }

    public final AnyClass setAnInt(final int anInt) {
        this.anInt = anInt;
        return this;
    }

    public final Double getADouble() {
        return aDouble;
    }

    public final AnyClass setADouble(final Double aDouble) {
        this.aDouble = aDouble;
        return this;
    }

    public final String getAString() {
        return aString;
    }

    public final AnyClass setAString(final String aString) {
        this.aString = aString;
        return this;
    }

    public final Date getADate() {
        return (null == aDate) ? null : new Date(aDate.getTime());
    }

    public final AnyClass setADate(final Date aDate) {
        this.aDate = (null == aDate) ? null : new Date(aDate.getTime());
        return this;
    }

    @Override
    public final int hashCode() {
        return toList(this).hashCode();
    }

    @Override
    public final boolean equals(final Object obj) {
        return (this == obj) || ((obj instanceof AnyClass) && toList(this).equals(toList((AnyClass) obj)));
    }

    @Override
    public final String toString() {
        return toList(this).toString();
    }
}
